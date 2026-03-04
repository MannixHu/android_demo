# Release & Update Guide

## 🚀 Making a Release

### Step 1: Update Version Code

Edit `app/build.gradle.kts`:
```gradle
defaultConfig {
    applicationId = "com.example.androidemo"
    minSdk = 28
    targetSdk = 35
    versionCode = 2          // Increment this
    versionName = "1.1"      // Update this
}
```

### Step 2: Update Changelog

Create release notes (optional but recommended):
```bash
echo "## Version 1.1 - 2026-03-05

### Features
- New feature X
- Improvement Y

### Bug Fixes
- Fixed issue A
- Fixed issue B" > CHANGELOG.md
```

### Step 3: Commit Changes

```bash
git add .
git commit -m "Release version 1.1

- Feature 1
- Feature 2
- Bug fix"
```

### Step 4: Create Git Tag

```bash
# Semantic versioning: v{MAJOR}.{MINOR}.{PATCH}
git tag -a v1.1 -m "Release version 1.1"
git tag -l  # List all tags
```

### Step 5: Push to GitHub

```bash
git push origin main
git push origin v1.1  # Push the specific tag
# Or push all tags:
git push origin --tags
```

### Automatic Actions 🤖

When you push a tag starting with `v`, GitHub Actions automatically:

1. **Builds the APK** (release and debug)
2. **Runs all tests**
3. **Creates a GitHub Release** with:
   - Release notes
   - Download links for APK files
   - Build metadata (date, commit, file sizes)

### View Release

Visit: https://github.com/MannixHu/android_demo/releases

---

## 📱 App Auto-Update Feature

### How It Works

The app periodically checks GitHub for the latest release:

1. App starts → Checks for updates in background
2. If newer version found → Shows notification to user
3. User clicks "Update" → Downloads APK from GitHub
4. Download completes → Prompts to install
5. User confirms → APK installs automatically

### Implementation Details

**Components:**
- `UpdateService.kt` - Retrofit API to fetch GitHub releases
- `UpdateManager.kt` - Handles download and installation
- `CheckUpdateUseCase.kt` - Business logic for update checks

**API Endpoint:**
```
GET https://api.github.com/repos/MannixHu/android_demo/releases/latest
```

**Response:**
```json
{
  "tag_name": "v1.1",
  "assets": [
    {
      "name": "app-release-v1.1.apk",
      "browser_download_url": "https://github.com/.../download/app-release-v1.1.apk",
      "size": 5242880
    }
  ],
  "body": "Release notes here..."
}
```

### Manual Update Check (in ViewModel)

```kotlin
// Example usage in CounterViewModel
fun checkForUpdates() {
    viewModelScope.launch {
        val currentVersion = "1.0"
        val updateInfo = checkUpdateUseCase(currentVersion)

        if (updateInfo.hasUpdate) {
            // Show update dialog to user
            _updateAvailable.value = updateInfo
        }
    }
}
```

### Permissions Required

Added to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

---

## 🔄 CI/CD Pipeline

### Two Workflows

#### 1. **Continuous Integration** (ci.yml)
Runs on every push to `main` or pull request:
- ✅ Compile code
- ✅ Run unit tests
- ✅ Lint checks
- ✅ Build debug APK
- 📊 Generate test reports

#### 2. **Release Build** (build-release.yml)
Runs ONLY when tag pushed (v*):
- 🔨 Build release APK (optimized)
- 🧪 Run full test suite
- 📦 Create GitHub Release
- 📥 Upload APK to release
- 📊 Generate reports

### View CI/CD Status

Go to: https://github.com/MannixHu/android_demo/actions

---

## 📋 Release Checklist

Before releasing:

- [ ] Update version code in `app/build.gradle.kts`
- [ ] Update version name in `app/build.gradle.kts`
- [ ] Add changelog entry if needed
- [ ] Run all tests locally: `./gradlew test`
- [ ] Build APK locally: `./gradlew assembleRelease`
- [ ] Commit all changes: `git commit -m "Release v1.1"`
- [ ] Create tag: `git tag -a v1.1 -m "Release v1.1"`
- [ ] Push to GitHub: `git push origin main && git push origin v1.1`
- [ ] Verify GitHub Actions build succeeded
- [ ] Check GitHub Releases page for uploaded APK
- [ ] Test APK on device/emulator

---

## 🔗 Quick Links

| Resource | URL |
|----------|-----|
| GitHub Repo | https://github.com/MannixHu/android_demo |
| Releases | https://github.com/MannixHu/android_demo/releases |
| Actions | https://github.com/MannixHu/android_demo/actions |
| Issues | https://github.com/MannixHu/android_demo/issues |

---

## 💡 Examples

### Example 1: Minor Version Update

```bash
# Update version
vim app/build.gradle.kts  # Change versionName to "1.1"

# Commit and tag
git add .
git commit -m "Bump version to 1.1"
git tag -a v1.1 -m "Version 1.1 release"
git push origin main
git push origin v1.1

# GitHub Actions automatically builds and releases! ✨
```

### Example 2: Patch Release

```bash
# For bug fixes
git add .
git commit -m "Fix counter persistence bug

- Fixed: Counter not loading from database on startup
- Improved: Database error handling"
git tag -a v1.0.1 -m "Patch release"
git push origin main --tags
```

### Example 3: Beta Release

```bash
# For beta/pre-release versions
git tag -a v1.1-beta -m "Beta 1"
git push origin v1.1-beta
# Mark as pre-release in GitHub Releases UI
```

---

## 🛠️ Troubleshooting

### GitHub Actions Build Failed

1. Check workflow file: `.github/workflows/build-release.yml`
2. View logs: https://github.com/MannixHu/android_demo/actions
3. Common issues:
   - JDK version mismatch → Fixed, using JDK 17
   - Gradle cache issues → `./gradlew clean build`
   - Missing dependencies → Check `app/build.gradle.kts`

### APK File Not Uploaded

1. Verify APK was built: Check Actions logs
2. Check release assets: https://github.com/MannixHu/android_demo/releases
3. Manually upload: Edit release → Upload files

### App Auto-Update Not Working

1. Check internet permission in manifest
2. Verify GitHub API rate limits (60 req/hour unauthenticated)
3. Check network connectivity
4. Add logging in `UpdateManager.kt` to debug

---

## 📖 Reference

- [Semantic Versioning](https://semver.org/)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github/about-releases)
- [Android APK Signing](https://developer.android.com/studio/publish/app-signing)
- [Retrofit Documentation](https://square.github.io/retrofit/)
