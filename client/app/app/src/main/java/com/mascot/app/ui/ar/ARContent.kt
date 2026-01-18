package com.mascot.app.ui.ar

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.filament.LightManager
import com.google.ar.core.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.LightNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import kotlinx.coroutines.launch

/**
 * AR 콘텐츠 화면
 * 
 * 기능:
 * - ARCore를 사용한 마스코트 배치
 * - ML Kit OCR로 "대전" 텍스트 인식
 * - 평면 인식 및 마스코트 모델 배치
 * - 마스코트 수집 완료 처리
 */
@Composable
fun ARContent(
    viewModel: ARViewModel = hiltViewModel(),
    onCollectionFinished: () -> Unit
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val childNodes = rememberNodes()
    val scope = rememberCoroutineScope()
    val textRecognizer = remember { TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build()) }

    // 마스코트 모델 배치 여부
    var isModelPlaced by remember { mutableStateOf(false) }
    
    // 사용자에게 표시할 디버그 메시지
    var debugMessage by remember { mutableStateOf("카메라로 '대전' 글자를 찾아보세요") }
    
    // OCR 처리 중 여부 (중복 처리 방지)
    var isProcessing by remember { mutableStateOf(false) }

    // 스로틀링: 마지막 OCR 인식 시간 저장 (0.5초 쿨타임)
    var lastProcessTime by remember { mutableStateOf(0L) }

    // AR 씬 조명 설정 (DisposableEffect로 생명주기 관리)
    DisposableEffect(Unit) {
        val lightNode = LightNode(engine = engine, type = LightManager.Type.DIRECTIONAL) {
            color(1.0f, 1.0f, 1.0f) // 흰색 조명
            intensity(100_000.0f)   // 밝기 설정
            direction(0.0f, -1.0f, -1.0f) // 빛의 방향 (위에서 앞쪽으로)
            castShadows(true)       // 그림자 활성화
        }
        childNodes.add(lightNode)

        onDispose {
            // 컴포저블 해제 시 조명 노드 제거 및 정리
            childNodes.remove(lightNode)
            lightNode.destroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            childNodes = childNodes,
            engine = engine,
            modelLoader = modelLoader,
            sessionConfiguration = { _, config ->
                config.focusMode = Config.FocusMode.AUTO // 자동 초점
                config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            },
            onSessionUpdated = { session, frame ->
                val currentTime = System.currentTimeMillis()

                /**
                 * 텍스트 인식 & 마스코트 배치 로직
                 * 
                 * 조건:
                 * - 모델이 아직 배치되지 않음
                 * - OCR 처리 중이 아님
                 * - 카메라 추적 상태가 TRACKING
                 * - 마지막 인식으로부터 0.5초 이상 경과 (스로틀링)
                 */
                if (!isModelPlaced && !isProcessing &&
                    frame.camera.trackingState == TrackingState.TRACKING &&
                    (currentTime - lastProcessTime > 500)
                ) {
                    // ARCore에서 카메라 이미지 가져오기
                    val image = try { frame.acquireCameraImage() } catch (e: Exception) { null }
                    if (image != null) {
                        isProcessing = true
                        lastProcessTime = currentTime

                        // ARCore 이미지를 ML Kit OCR에 전달 (90도 회전 보정)
                        val inputImage = InputImage.fromMediaImage(image, 90)

                        // 한국어 텍스트 인식
                        textRecognizer.process(inputImage).addOnSuccessListener { text ->
                            // "대전" 텍스트가 인식되면 마스코트 배치
                            if (text.text.contains("대전")) {
                                // 화면 중앙 좌표로 히트 테스트
                                val centerX = frame.camera.imageIntrinsics.principalPoint[0]
                                val centerY = frame.camera.imageIntrinsics.principalPoint[1]
                                val hits = frame.hitTest(centerX, centerY)
                                
                                // 평면(바닥/벽)이 인식되면 그곳에 배치, 아니면 공중에 배치
                                val planeHit = hits.firstOrNull { 
                                    it.trackable is Plane && (it.trackable as Plane).isPoseInPolygon(it.hitPose) 
                                }

                                val anchor = if (planeHit != null) {
                                    // 평면 인식 성공: 바닥/벽에 배치
                                    debugMessage = "평면 인식 성공! (바닥/벽에 배치)"
                                    planeHit.createAnchor()
                                } else {
                                    // 평면 미인식: 카메라 앞 50cm 공중에 배치
                                    debugMessage = "공중 배치 (카메라 앞 50cm)"
                                    val camPose = frame.camera.pose
                                    val zAxis = camPose.zAxis
                                    session.createAnchor(Pose(
                                        floatArrayOf(
                                            camPose.tx() - zAxis[0] * 0.5f, 
                                            camPose.ty() - zAxis[1] * 0.5f, 
                                            camPose.tz() - zAxis[2] * 0.5f
                                        ),
                                        floatArrayOf(0f, 0f, 0f, 1f)
                                    ))
                                }

                                // 앵커 노드 생성 및 마스코트 모델 배치
                                val anchorNode = AnchorNode(engine, anchor)
                                scope.launch {
                                    // GLB 모델 로드 (assets/mascot.glb)
                                    val instance = modelLoader.createModelInstance("mascot.glb")
                                    val modelNode = ModelNode(instance, scaleToUnits = 0.3f).apply {
                                        parent = anchorNode

                                        // 카메라를 바라보도록 회전
                                        val camPosition = Position(
                                            frame.camera.pose.tx(), 
                                            frame.camera.pose.ty(), 
                                            frame.camera.pose.tz()
                                        )
                                        lookAt(camPosition)

                                        // 180도 추가 회전 (정면 보기)
                                        rotation = Rotation(rotation.x, rotation.y + 180f, rotation.z)

                                        // 터치 이벤트: 마스코트 수집 완료 처리
                                        onSingleTapConfirmed = {
                                            Toast.makeText(context, "🎉 마스코트 수집 완료!", Toast.LENGTH_SHORT).show()

                                            // 데이터베이스에 마스코트 수집 상태 저장
                                            val detectedMascotId = 1001 // 꿈돌이 ID
                                            viewModel.onMascotCollected(detectedMascotId)

                                            // 수집 완료 콜백 호출 (화면 이동)
                                            onCollectionFinished()

                                            true
                                        }
                                    }
                                    childNodes.add(anchorNode)
                                    isModelPlaced = true
                                }
                            }
                        }.addOnCompleteListener {
                            // 이미지 리소스 해제 (메모리 누수 방지)
                            image.close()
                            isProcessing = false
                        }
                    }
                }
            }
        )

        // 안내 텍스트
        Text(
            text = debugMessage,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp)
        )
    }
}