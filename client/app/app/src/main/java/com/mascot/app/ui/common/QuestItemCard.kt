package com.mascot.app.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mascot.app.data.model.QuestItem
import com.mascot.app.ui.theme.*

/**
 * 포켓캠프 스타일 퀘스트 카드
 * 
 * 동물의숲 포켓캠프에서 영감을 받은 부드럽고 친근한 카드 디자인
 */
@Composable
fun QuestItemCard(
    quest: QuestItem,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = CardShadow
            )
            .clickable { onClick(quest.id) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = quest.title,
                style = MaterialTheme.typography.titleMedium,
                color = MascotOnSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = quest.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MascotOnSurface.copy(alpha = 0.7f)
            )
        }
    }
}
