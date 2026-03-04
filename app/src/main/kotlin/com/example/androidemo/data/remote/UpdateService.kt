package com.example.androidemo.data.remote

import retrofit2.http.GET

data class GithubRelease(
    val tag_name: String,
    val assets: List<GithubAsset>,
    val body: String
)

data class GithubAsset(
    val name: String,
    val browser_download_url: String,
    val size: Long
)

interface UpdateService {
    @GET("repos/MannixHu/android_demo/releases/latest")
    suspend fun getLatestRelease(): GithubRelease
}
