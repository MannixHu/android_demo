package com.example.androidemo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidemo.ui.components.UpdateDialog
import com.example.androidemo.viewmodel.CounterViewModel
import com.example.androidemo.viewmodel.UpdateViewModel

@Composable
fun CounterScreen(
    counterViewModel: CounterViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel()
) {
    val counter by counterViewModel.counterState.collectAsStateWithLifecycle()
    val updateState by updateViewModel.updateState.collectAsStateWithLifecycle()
    val triggerUpdateCheck by counterViewModel.triggerUpdateCheck.collectAsStateWithLifecycle()

    // 应用启动时自动检查更新
    LaunchedEffect(triggerUpdateCheck) {
        if (triggerUpdateCheck) {
            updateViewModel.checkForUpdates(force = false)
            counterViewModel.resetUpdateCheckTrigger()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 顶部工具栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { updateViewModel.checkForUpdates(force = true) }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "检查更新",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = "Counter",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = counter.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 60.sp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { counterViewModel.decrement() }) {
                Text("-1")
            }

            Button(
                onClick = { counterViewModel.reset() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Reset")
            }

            Button(onClick = { counterViewModel.increment() }) {
                Text("+1")
            }
        }
    }

    // 显示更新对话框
    UpdateDialog(
        updateState = updateState,
        onDownload = { downloadUrl ->
            updateViewModel.downloadUpdate(downloadUrl) { filePath ->
                // 下载完成后会自动显示安装对话框
            }
        },
        onInstall = { filePath ->
            updateViewModel.installUpdate(filePath)
        },
        onSkip = { version ->
            updateViewModel.skipVersion(version)
        },
        onDismiss = {
            updateViewModel.dismissUpdate()
        }
    )
}
