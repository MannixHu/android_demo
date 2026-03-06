package com.example.androidemo.util

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.androidemo.data.remote.GithubAsset
import com.example.androidemo.data.remote.UpdateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class UpdateInfo(
    val versionName: String,
    val downloadUrl: String,
    val releaseNotes: String,
    val fileSize: Long,
    val hasUpdate: Boolean
)

class UpdateManager(
    private val context: Context,
    private val updateService: UpdateService
) {

    suspend fun checkForUpdates(currentVersion: String): UpdateInfo = withContext(Dispatchers.IO) {
        try {
            val latestRelease = updateService.getLatestRelease()
            val latestVersion = latestRelease.tag_name.removePrefix("v")
            val hasUpdate = isNewerVersion(latestVersion, currentVersion)

            val apkAsset = selectPreferredApkAsset(latestRelease.assets)
            val downloadUrl = apkAsset?.browser_download_url ?: ""
            val fileSize = apkAsset?.size ?: 0L

            UpdateInfo(
                versionName = latestVersion,
                downloadUrl = downloadUrl,
                releaseNotes = latestRelease.body,
                fileSize = fileSize,
                hasUpdate = hasUpdate
            )
        } catch (e: Exception) {
            // 如果检查失败，返回无更新状态
            UpdateInfo(
                versionName = currentVersion,
                downloadUrl = "",
                releaseNotes = "",
                fileSize = 0L,
                hasUpdate = false
            )
        }
    }

    private fun selectPreferredApkAsset(assets: List<GithubAsset>): GithubAsset? {
        val apkAssets = assets.filter { it.name.endsWith(".apk", ignoreCase = true) }

        return apkAssets.firstOrNull {
            !it.name.contains("debug", ignoreCase = true) &&
                    !it.name.contains("unsigned", ignoreCase = true)
        } ?: apkAssets.firstOrNull {
            it.name.contains("debug", ignoreCase = true)
        } ?: apkAssets.firstOrNull()
    }

    fun downloadApk(downloadUrl: String, onDownloadComplete: (String) -> Unit) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(downloadUrl)
        val request = DownloadManager.Request(uri).apply {
            setTitle("Counter App Update")
            setDescription("Downloading latest version...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "counter_app_latest.apk"
            )
        }

        val downloadId = downloadManager.enqueue(request)

        // 注册下载完成广播接收器
        val receiver = DownloadCompleteReceiver(downloadManager, downloadId) { filePath ->
            onDownloadComplete(filePath)
        }
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    fun installApk(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) return

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }

        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    }

    private fun isNewerVersion(newVersion: String, currentVersion: String): Boolean {
        return try {
            val newParts = newVersion.split(".")
            val currentParts = currentVersion.split(".")

            for (i in 0 until maxOf(newParts.size, currentParts.size)) {
                val newPart = newParts.getOrNull(i)?.toIntOrNull() ?: 0
                val currentPart = currentParts.getOrNull(i)?.toIntOrNull() ?: 0

                when {
                    newPart > currentPart -> return true
                    newPart < currentPart -> return false
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }
}

class DownloadCompleteReceiver(
    private val downloadManager: DownloadManager,
    private val downloadId: Long,
    private val onDownloadComplete: (String) -> Unit
) : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: android.content.Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L) ?: return
        if (id == downloadId) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                val localUri = cursor.getString(columnIndex)
                val filePath = Uri.parse(localUri).path ?: ""
                onDownloadComplete(filePath)
            }
            cursor.close()
            context?.unregisterReceiver(this)
        }
    }
}
