package com.mascot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.mascot.app.ui.MascotApp
import com.mascot.app.ui.theme.MascotTheme
import dagger.hilt.android.AndroidEntryPoint
/**
 * 메인 Activity
 * 
 * 역할:
 * - 앱 진입점
 * - Hilt 의존성 주입 초기화
 * - Compose UI 설정
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MascotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 앱 전체 네비게이션 및 화면 구성
                    MascotApp()
                }
            }
        }
    }
}
