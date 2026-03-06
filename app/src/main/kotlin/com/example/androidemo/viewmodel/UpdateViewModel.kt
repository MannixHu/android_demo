package com.example.androidemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidemo.util.AppVersionProvider
import com.example.androidemo.util.UpdateInfo
import com.example.androidemo.util.UpdateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UpdateUIState {
    object Idle : UpdateUIState()
    object Checking : UpdateUIState()
    data class UpdateAvailable(val updateInfo: UpdateInfo) : UpdateUIState()
    object NoUpdate : UpdateUIState()
    data class Error(val message: String) : UpdateUIState()
    object Downloading : UpdateUIState()
    data class Downloaded(val filePath: String) : UpdateUIState()
}

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val updateManager: UpdateManager,
    private val appVersionProvider: AppVersionProvider
) : ViewModel() {

    private val _updateState = MutableStateFlow<UpdateUIState>(UpdateUIState.Idle)
    val updateState: StateFlow<UpdateUIState> = _updateState.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0)
    val downloadProgress: StateFlow<Int> = _downloadProgress.asStateFlow()

    fun checkForUpdates(force: Boolean = false) {
        viewModelScope.launch {
            // 如果不是强制检查，检查是否需要检查
            if (!force && !updateManager.shouldCheckForUpdates()) {
                return@launch
            }

            _updateState.value = UpdateUIState.Checking

            try {
                val currentVersion = getCurrentVersion()
                val updateInfo = updateManager.checkForUpdates(currentVersion)

                // 记录检查时间
                updateManager.recordCheckTime()

                val skippedVersion = updateManager.getSkippedVersion()

                // 检查是否已跳过此版本
                if (updateInfo.hasUpdate && updateInfo.versionName != skippedVersion) {
                    _updateState.value = UpdateUIState.UpdateAvailable(updateInfo)
                } else if (updateInfo.hasUpdate && updateInfo.versionName == skippedVersion) {
                    _updateState.value = UpdateUIState.NoUpdate
                } else {
                    _updateState.value = UpdateUIState.NoUpdate
                }
            } catch (e: Exception) {
                _updateState.value = UpdateUIState.Error("检查更新失败: ${e.message}")
            }
        }
    }

    fun downloadUpdate(downloadUrl: String, onDownloadComplete: (String) -> Unit) {
        _updateState.value = UpdateUIState.Downloading
        try {
            updateManager.downloadApk(downloadUrl) { filePath ->
                _updateState.value = UpdateUIState.Downloaded(filePath)
                onDownloadComplete(filePath)
            }
        } catch (e: Exception) {
            _updateState.value = UpdateUIState.Error("下载失败: ${e.message}")
        }
    }

    fun installUpdate(filePath: String) {
        try {
            updateManager.installApk(filePath)
        } catch (e: Exception) {
            _updateState.value = UpdateUIState.Error("安装失败: ${e.message}")
        }
    }

    fun skipVersion(version: String) {
        updateManager.skipVersion(version)
        _updateState.value = UpdateUIState.Idle
    }

    fun dismissUpdate() {
        _updateState.value = UpdateUIState.Idle
    }

    private fun getCurrentVersion(): String {
        return appVersionProvider.getCurrentVersion()
    }
}
