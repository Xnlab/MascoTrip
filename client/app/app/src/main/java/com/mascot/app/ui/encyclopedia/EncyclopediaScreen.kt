@file:OptIn(ExperimentalLayoutApi::class)

package com.mascot.app.ui.encyclopedia

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.mascot.app.data.encyclopediadata.entity.MascotEntity
import com.mascot.app.data.encyclopediadata.entity.ZoneEntity
import com.mascot.app.ui.theme.*

/**
 * 도감 화면
 * 
 * 기능:
 * - 지역별 마스코트 목록 표시
 * - 수집/미수집 상태 표시 (실루엣)
 * - 마스코트 상세 정보 다이얼로그
 */
@Composable
fun EncyclopediaScreen(
    viewModel: EncyclopediaViewModel = hiltViewModel()
) {
    // ViewModel에서 데이터 구독
    val zones by viewModel.zones.collectAsState(initial = emptyList())
    val mascotList by viewModel.mascots.collectAsState(initial = emptyList())

    // zoneId 기준으로 mascot 매핑
    val mascots = remember(mascotList) {
        viewModel.getMascotsByZone(mascotList)
    }

    var selectedMascot by remember { mutableStateOf<MascotEntity?>(null) }

    // 화면 가로폭 계산
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = 16.dp * 2
    val spacing = 16.dp
    val availableWidth = screenWidth - horizontalPadding - (spacing * 2)
    val cardWidth = availableWidth / 3

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MascotBackground)  // 포켓캠프 스타일 배경
            .padding(16.dp)
    ) {

        item {
            Text(
                "지역 마스코트 도감",
                style = MaterialTheme.typography.headlineLarge,
                color = MascotOnBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        zones.groupBy { it.region }.forEach { (regionName, regionZones) ->

            item {
                RegionHeader(title = regionName)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                FlowRow(
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    regionZones.forEach { zone ->
                        mascots[zone.id]?.let { mascot ->
                            MascotCard(zone, mascot, cardWidth) {
                                selectedMascot = mascot
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }

    selectedMascot?.let { mascot ->
        MascotDetailDialog(mascot) { selectedMascot = null }
    }
}

/**
 * 포켓캠프 스타일 지역 헤더
 */
@Composable
fun RegionHeader(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MascotPrimary.copy(alpha = 0.2f)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MascotOnBackground,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun MascotCard(
    zone: ZoneEntity,
    mascot: MascotEntity,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val imageId = remember {
        context.resources.getIdentifier(
            mascot.imageUrl,
            "drawable",
            context.packageName
        )
    }

    val silhouetteId = remember {
        context.resources.getIdentifier(
            "${mascot.imageUrl}_silhouette",
            "drawable",
            context.packageName
        )
    }

    val finalImageId =
        if (mascot.isCollected && imageId != 0) imageId
        else if (!mascot.isCollected && silhouetteId != 0) silhouetteId
        else 0

    Column(
        modifier = Modifier
            .width(cardWidth)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 포켓캠프 스타일 카드 배경
        Surface(
            modifier = Modifier.size(100.dp),
            shape = MaterialTheme.shapes.medium,
            color = CardBackground,
            shadowElevation = 4.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (finalImageId != 0) {
                    Image(
                        painter = painterResource(id = finalImageId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.75f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            mascot.name,
            style = MaterialTheme.typography.titleSmall,
            color = MascotOnBackground
        )
        Text(
            mascot.region,
            style = MaterialTheme.typography.bodySmall,
            color = MascotOnSurface.copy(alpha = 0.6f)
        )
    }
}

/**
 * 포켓캠프 스타일 마스코트 상세 다이얼로그
 */
@Composable
fun MascotDetailDialog(
    mascot: MascotEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = CardBackground,
            modifier = Modifier.padding(20.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = mascot.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MascotOnBackground
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "지역: ${mascot.region}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MascotOnSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = mascot.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MascotOnSurface
                )
                Spacer(Modifier.height(24.dp))
                
                // 포켓캠프 스타일 버튼
                Surface(
                    onClick = onDismiss,
                    shape = MaterialTheme.shapes.medium,
                    color = MascotPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "닫기",
                        style = MaterialTheme.typography.labelLarge,
                        color = MascotOnPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
