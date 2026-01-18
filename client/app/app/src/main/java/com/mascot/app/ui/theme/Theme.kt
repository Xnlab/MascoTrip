package com.mascot.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 포켓캠프 스타일 라이트 컬러 스킴
 * 
 * 동물의숲 포켓캠프에서 영감을 받은 밝고 따뜻한 색감
 * 항상 라이트 모드 사용 (포켓캠프는 밝은 테마)
 */
private val MascotColorScheme = lightColorScheme(
    primary = MascotPrimary,
    onPrimary = MascotOnPrimary,
    primaryContainer = MascotPrimaryVariant,
    onPrimaryContainer = MascotOnPrimary,
    
    secondary = MascotSecondary,
    onSecondary = MascotOnPrimary,
    secondaryContainer = MascotSecondaryVariant,
    onSecondaryContainer = MascotOnPrimary,
    
    tertiary = MascotAccent,
    onTertiary = MascotOnPrimary,
    tertiaryContainer = MascotAccentLight,
    onTertiaryContainer = MascotOnPrimary,
    
    background = MascotBackground,
    onBackground = MascotOnBackground,
    
    surface = MascotSurface,
    onSurface = MascotOnSurface,
    surfaceVariant = MascotSurfaceVariant,
    onSurfaceVariant = MascotOnSurface,
    
    error = MascotError,
    onError = Color.White,
    errorContainer = Color(0xFFFFE5E5),
    onErrorContainer = MascotError,
    
    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFF0F0F0),
    
    scrim = Color(0x66000000),
    inverseSurface = MascotOnBackground,
    inverseOnSurface = MascotBackground,
    inversePrimary = MascotPrimaryVariant,
    surfaceTint = MascotPrimary
)

/**
 * Mascot 앱 테마
 * 
 * 포켓캠프 스타일의 밝고 따뜻한 UI 테마
 * 항상 라이트 모드 사용
 */
@Composable
fun MascotTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MascotColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}