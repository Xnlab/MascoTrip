package com.mascot.app.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mascot.app.ui.Screen
import com.mascot.app.ui.theme.*

/**
 * 홈 화면
 * 
 * 기능:
 * - 마스코트 방 표시 (퀘스트 완료 시 오브제 표시)
 * - 퀘스트 진행도 표시
 * - 래플 팝업 표시
 * - AR 화면으로 이동
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by viewModel.screenState.collectAsState()
    val objects by viewModel.unlockedObjects.collectAsState()
    val questCount by viewModel.questCount.collectAsState()
    val showRafflePopup by viewModel.showRafflePopup.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (homeState) {
                // 1. 잠김 상태
                HomeState.LOCKED -> {
                    // HomeLockedScreen이 다른 파일에 있다면 자동 import 됩니다.
                    HomeLockedScreen(
                        onGoToAR = {
                            navController.navigate(Screen.AR.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    )
                }

                // 2. 수집 직후 팝업 상태
                HomeState.FIRST_ENTER -> {
                    // ✨ 이미 다른 파일에 만들어두신 NewFriendPopup을 호출합니다.
                    NewFriendPopup(
                        onDismiss = {
                            viewModel.finishFirstEnter()
                        }
                    )
                }

                // 3. 메인 방 (ROOM)
                HomeState.ROOM -> {
                    MascotRoom(
                        objects = objects,
                        onQuestTest = {
                            viewModel.debugProgressQuest()
                        }
                    )

                    // 퀘스트 진행도 UI
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 60.dp)
                    ) {
                        QuestProgressUI(
                            current = questCount,
                            total = 3,
                            onHeaderClick = {
                                if (questCount < 3) {
                                    viewModel.debugProgressQuest()
                                } else {
                                    viewModel.openRafflePopup()
                                }
                            }
                        )
                    }
                }
            }

            // 래플 팝업
            if (showRafflePopup) {
                // ✨ 이미 다른 파일에 만들어두신 RaffleTicketPopup을 호출합니다.
                RaffleTicketPopup(
                    onDismiss = { viewModel.closeRafflePopup() }
                )
            }
        }
    }
}

/**
 * 퀘스트 진행도 UI 컴포넌트
 * 
 * @param current 현재 완료한 퀘스트 수
 * @param total 전체 퀘스트 수
 * @param onHeaderClick 진행도 바 클릭 시 호출되는 콜백
 */
@Composable
fun QuestProgressUI(
    current: Int,
    total: Int,
    onHeaderClick: () -> Unit
) {
    val isComplete = current >= total

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "퀘스트 진행도",
            style = MaterialTheme.typography.headlineMedium,
            color = MascotOnBackground
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 포켓캠프 스타일 진행도 카드
        Surface(
            color = if (isComplete) MascotAccentLight else CardBackground,
            shape = MaterialTheme.shapes.large,
            shadowElevation = if (isComplete) 6.dp else 3.dp,
            onClick = { onHeaderClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = if (total > 0) current / total.toFloat() else 0f,
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp),
                    color = if (isComplete) MascotAccent else MascotPrimary,
                    trackColor = MascotSurfaceVariant,
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "$current / $total",
                    style = MaterialTheme.typography.titleSmall,
                    color = MascotOnSurface
                )
            }
        }
    }
}