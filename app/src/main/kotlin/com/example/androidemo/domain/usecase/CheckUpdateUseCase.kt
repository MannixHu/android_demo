package com.example.androidemo.domain.usecase

import com.example.androidemo.util.UpdateInfo
import com.example.androidemo.util.UpdateManager

class CheckUpdateUseCase(private val updateManager: UpdateManager) {
    suspend operator fun invoke(currentVersion: String): UpdateInfo {
        return updateManager.checkForUpdates(currentVersion)
    }
}
