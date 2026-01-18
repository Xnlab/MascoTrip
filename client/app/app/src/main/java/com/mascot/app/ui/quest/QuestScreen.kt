package com.mascot.app.ui.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mascot.app.data.model.QuestItem
import com.mascot.app.ui.common.QuestItemCard
import com.mascot.app.ui.theme.*

/**
 * 퀘스트 목록 화면
 * 
 * 기능:
 * - 지역별 퀘스트 목록 표시
 * - 지역 필터링 (전체, 서구, 유성구, 중구, 동구, 대덕구)
 * - 퀘스트 상세 화면으로 이동
 * - 튜토리얼 시작 버튼
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    navController: NavController,
    viewModel: QuestViewModel = hiltViewModel()
) {
/**
 * 퀘스트 목록 화면
 * 
 * 기능:
 * - 지역별 퀘스트 목록 표시
 * - 지역 필터링 (전체, 서구, 유성구, 중구, 동구, 대덕구)
 * - 퀘스트 상세 화면으로 이동
 * - 튜토리얼 시작 버튼
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    navController: NavController,
    viewModel: QuestViewModel = hiltViewModel()
) {

    val regions by viewModel.quests.collectAsState()
    val isGenerating by viewModel.loading.collectAsState()
    val completedQuests by viewModel.completedQuests.collectAsState()

    // 🔹 지역 탭
    val regionTabs = listOf("전체", "서구", "유성구", "중구", "동구", "대덕구")
    var selectedRegion by remember { mutableStateOf("전체") }

    // 🔹 선택된 지역 퀘스트
    val displayedQuests: List<QuestItem> =
        remember(regions, selectedRegion, completedQuests) {

            val allQuests =
                if (selectedRegion == "전체") {
                    regions.values.flatten()
                } else {
                    regions[selectedRegion].orEmpty()
                }

            // ✅ 완료된 퀘스트 제외
            allQuests.filter { quest ->
                completedQuests.none { it.id == quest.id }
            }
        }


    Scaffold(
        floatingActionButton = {
            // 포켓캠프 스타일 FAB
            Surface(
                onClick = { navController.navigate("tutorial_start") },
                shape = MaterialTheme.shapes.large,
                color = MascotPrimary,
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.large)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MascotOnPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "튜토리얼 시작하기",
                        style = MaterialTheme.typography.labelLarge,
                        color = MascotOnPrimary
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MascotBackground)  // 포켓캠프 스타일 배경
                .padding(16.dp)
        ) {

            /* ---------- 상단 타이틀 + 완료된 퀘스트 버튼 ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "퀘스트 목록",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MascotOnBackground,
                    modifier = Modifier.weight(1f)
                )

                // 포켓캠프 스타일 버튼
                Surface(
                    onClick = { navController.navigate("completed_quests") },
                    shape = MaterialTheme.shapes.medium,
                    color = MascotSecondary,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "완료된 퀘스트",
                        style = MaterialTheme.typography.labelLarge,
                        color = MascotOnPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            /* ---------- 지역 선택 (포켓캠프 스타일 칩) ---------- */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                regionTabs.forEach { region ->
                    // 포켓캠프 스타일의 둥근 칩
                    Surface(
                        onClick = { selectedRegion = region },
                        shape = MaterialTheme.shapes.large,
                        color = if (selectedRegion == region) {
                            MascotPrimary
                        } else {
                            CardBackground
                        },
                        modifier = Modifier
                            .shadow(
                                elevation = if (selectedRegion == region) 4.dp else 2.dp,
                                shape = MaterialTheme.shapes.large
                            )
                    ) {
                        Text(
                            text = region,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selectedRegion == region) {
                                MascotOnPrimary
                            } else {
                                MascotOnSurface
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            when {
                // 1) 생성 중
                isGenerating -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("퀘스트 생성 중입니다…")
                        }
                    }
                }

                // 2) 없음
                displayedQuests.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "아직 받은 의뢰가 없어요.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MascotOnSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "튜토리얼을 시작하면\n맞춤형 퀘스트가 도착합니다!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MascotOnSurface.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // 3) 리스트
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayedQuests) { quest ->
                            QuestItemCard(
                                quest = quest,
                                onClick = { id ->
                                    navController.navigate("quest_detail/$id")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
