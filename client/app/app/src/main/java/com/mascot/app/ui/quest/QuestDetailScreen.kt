package com.mascot.app.ui.quest

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
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
        ) {
            if (quest == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("퀘스트 정보를 찾을 수 없습니다.")
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = quest.title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = quest.location)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = quest.description)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !checkingLocation && !completed,
                        onClick = {
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
                        }
                    ) {
                        if (checkingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("확인 중…")
                        } else {
                            Text("도착 확인")
                        }
                    }

                    resultMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            color = if (completed)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
