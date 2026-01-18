package com.mascot.app.ui.quest

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mascot.app.data.model.QuestItem
import com.mascot.app.util.LocationHelper
import com.mascot.app.util.getDistanceMeter
import com.mascot.app.ui.theme.*
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons


/**
 * 퀘스트 상세 화면
 * 
 * 기능:
 * - 퀘스트 상세 정보 표시
 * - 현재 위치 확인 및 거리 계산
 * - 퀘스트 완료 처리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    navController: NavController,
    questId: String?,
    viewModel: QuestViewModel = hiltViewModel()
) {


    val regionsMap by viewModel.quests.collectAsState()
    val quest: QuestItem? =
        regionsMap.values.flatten().find { it.id == questId }

    val context = LocalContext.current
    val activity = context as Activity

    var currentLat by remember { mutableStateOf<Double?>(null) }
    var currentLng by remember { mutableStateOf<Double?>(null) }

    var resultMessage by remember { mutableStateOf<String?>(null) }
    var checkingLocation by remember { mutableStateOf(false) }
    var completed by remember { mutableStateOf(false) }

    /* 🔥 위치 들어오면 거리 계산 */
    LaunchedEffect(currentLat, currentLng) {
        if (
            quest != null &&
            currentLat != null &&
            currentLng != null &&
            checkingLocation
        ) {
            val distance = getDistanceMeter(
                lat1 = currentLat!!,
                lng1 = currentLng!!,
                lat2 = quest.coordinates.lat,
                lng2 = quest.coordinates.lng
            )

            if (distance <= 1000) {
                viewModel.completeQuest(quest)
                resultMessage = "🎉 퀘스트 완료!"
                completed = true

                delay(1000)
                navController.navigate("completed_quests") {
                    popUpTo("quest") { inclusive = false }
                }
            } else {
                resultMessage =
                    "❌ 아직 장소에 도착하지 않았어요.\n(거리: ${distance.toInt()}m)"
            }

            checkingLocation = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(quest?.title ?: "퀘스트 상세") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MascotBackground)  // 포켓캠프 스타일 배경
        ) {
            if (quest == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "퀘스트 정보를 찾을 수 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MascotOnSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // 포켓캠프 스타일 카드
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        color = CardBackground,
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = quest.title,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MascotOnBackground
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📍 ",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = quest.location,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MascotOnSurface.copy(alpha = 0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = quest.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MascotOnSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 포켓캠프 스타일 버튼
                    Surface(
                        onClick = {
                            if (checkingLocation || completed) return@Surface
                            val hasPermission =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED

                            if (!hasPermission) {
                                ActivityCompat.requestPermissions(
                                    activity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    1001
                                )
                                resultMessage = "📍 위치 권한을 허용해주세요."
                                return@Button
                            }

                            checkingLocation = true
                            resultMessage = "📍 현재 위치 확인 중…"

                            LocationHelper.getCurrentLocation(context) { lat, lng ->
                                currentLat = lat
                                currentLng = lng
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        color = if (!checkingLocation && !completed) {
                            MascotPrimary
                        } else {
                            MascotSurfaceVariant
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !checkingLocation && !completed
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (checkingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MascotOnPrimary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "확인 중…",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MascotOnPrimary
                                )
                            } else {
                                Text(
                                    "도착 확인",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (completed) {
                                        MascotOnSurface.copy(alpha = 0.5f)
                                    } else {
                                        MascotOnPrimary
                                    }
                                )
                            }
                        }
                    }

                    resultMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // 포켓캠프 스타일 메시지 카드
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = if (completed) {
                                MascotSuccess.copy(alpha = 0.2f)
                            } else {
                                MascotError.copy(alpha = 0.1f)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                color = if (completed) {
                                    MascotSuccess
                                } else {
                                    MascotError
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
