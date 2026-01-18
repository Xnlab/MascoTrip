package com.mascot.app.ui.quest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mascot.app.ui.theme.*

/**
 * 완료된 퀘스트 목록 화면
 * 
 * 기능:
 * - 완료한 퀘스트 목록 표시
 * - 빈 상태 처리
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedQuestScreen(
    navController: NavController,
    viewModel: QuestViewModel = hiltViewModel()
) {

    val completedQuests by viewModel.completedQuests.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("완료된 퀘스트") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MascotBackground)  // 포켓캠프 스타일 배경
        ) {
            if (completedQuests.isEmpty()) {
                // 완료한 퀘스트가 없을 때
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 완료한 퀘스트가 없어요.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MascotOnSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                // 완료된 퀘스트 목록 (포켓캠프 스타일)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(completedQuests) { quest ->
                        // 포켓캠프 스타일 완료 카드
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            color = MascotSuccess.copy(alpha = 0.15f),
                            shadowElevation = 3.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "✅ ",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = quest.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MascotOnBackground
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 ${quest.location}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MascotOnSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
