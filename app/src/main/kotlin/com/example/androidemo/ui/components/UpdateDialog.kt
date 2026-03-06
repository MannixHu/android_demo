package com.example.androidemo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidemo.util.UpdateInfo
import com.example.androidemo.viewmodel.UpdateUIState

@Composable
fun UpdateDialog(
    updateState: UpdateUIState,
    onDownload: (String) -> Unit,
    onInstall: (String) -> Unit,
    onSkip: (String) -> Unit,
    onDismiss: () -> Unit
) {
    when (updateState) {
        is UpdateUIState.UpdateAvailable -> {
            UpdateAvailableDialog(
                updateInfo = updateState.updateInfo,
                onDownload = { onDownload(updateState.updateInfo.downloadUrl) },
                onSkip = { onSkip(updateState.updateInfo.versionName) },
                onDismiss = onDismiss
            )
        }

        is UpdateUIState.Checking -> {
            CheckingUpdateDialog(onDismiss = onDismiss)
        }

        is UpdateUIState.Downloading -> {
            DownloadingDialog(onDismiss = onDismiss)
        }

        is UpdateUIState.Downloaded -> {
            InstalledDialog(
                filePath = updateState.filePath,
                onInstall = { onInstall(updateState.filePath) },
                onDismiss = onDismiss
            )
        }

        is UpdateUIState.Error -> {
            ErrorDialog(
                message = updateState.message,
                onDismiss = onDismiss
            )
        }

        else -> {}
    }
}

@Composable
private fun UpdateAvailableDialog(
    updateInfo: UpdateInfo,
    onDownload: () -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("新版本可用")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "版本: ${updateInfo.versionName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (updateInfo.fileSize > 0) {
                    Text(
                        text = "大小: ${formatFileSize(updateInfo.fileSize)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (updateInfo.releaseNotes.isNotEmpty()) {
                    Text(
                        text = "更新内容:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = updateInfo.releaseNotes.take(200) +
                            if (updateInfo.releaseNotes.length > 200) "..." else "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDownload) {
                Text("下载更新")
            }
        },
        dismissButton = {
            TextButton(onClick = onSkip) {
                Text("跳过此版本")
            }
        }
    )
}

@Composable
private fun CheckingUpdateDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("检查更新中")
        },
        text = {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun DownloadingDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("下载中")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "正在下载新版本...",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("后台下载")
            }
        }
    )
}

@Composable
private fun InstalledDialog(
    filePath: String,
    onInstall: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("下载完成")
        },
        text = {
            Text("新版本已下载，点击安装进行更新。")
        },
        confirmButton = {
            Button(onClick = onInstall) {
                Text("立即安装")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("稍后")
            }
        }
    )
}

@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("错误")
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("确定")
            }
        }
    )
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes <= 0 -> "0B"
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> String.format("%.2f KB", bytes / 1024.0)
        bytes < 1024 * 1024 * 1024 -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
    }
}
