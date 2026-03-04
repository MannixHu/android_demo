# Release Workflow Visual Guide

## 🔄 Complete Release Process Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ANDROID COUNTER APP - RELEASE WORKFLOW                    │
└─────────────────────────────────────────────────────────────────────────────┘

STEP 1: DEVELOP
═══════════════════════════════════════════════════════════════════════════════
┌─────────────────────────────────────┐
│ Make code changes                   │
│ • Add features                      │
│ • Fix bugs                          │
│ • Improve UI/UX                     │
└────────────────┬────────────────────┘
                 │
                 ▼
        ┌────────────────┐
        │ Test locally   │
        │ ./gradlew test │
        └────────┬───────┘
                 │
                 ▼
        ┌──────────────────────┐
        │ Build debug APK      │
        │ ./gradlew runDebug   │
        └────────┬─────────────┘
                 │
                 ▼ (Tests pass? ✅)


STEP 2: COMMIT & TAG
═══════════════════════════════════════════════════════════════════════════════
┌──────────────────────────────────────────┐
│ git add .                                │
│ git commit -m "Release version 1.1"      │
│                                          │
│ Commit message conventions:              │
│ • "Release v1.1" (minor feature)        │
│ • "Fix critical bug" (patch)             │
│ • "Major refactor" (major)               │
└────────────────┬─────────────────────────┘
                 │
                 ▼
    ┌────────────────────────────┐
    │ git tag -a v1.1            │
    │ -m "Release v1.1"          │
    │                            │
    │ Tag naming: v{MAJOR}.{MINOR}.{PATCH}
    │ Examples: v1.0, v1.1, v2.0, v1.0-beta
    └────────────────┬───────────┘
                     │
                     ▼ (Locally ready ✅)


STEP 3: PUSH TO GITHUB
═══════════════════════════════════════════════════════════════════════════════
┌──────────────────────────────────────┐
│ git push origin main                 │
│ git push origin v1.1                 │
│                                      │
│ Or push all at once:                 │
│ git push origin main --tags          │
└────────────┬───────────────────────┘
             │
             ▼ (Pushed to GitHub ✅)


STEP 4: GITHUB ACTIONS AUTO-BUILD 🤖
═══════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────────────────────┐
│ GitHub detects tag push (v1.1)                                              │
│ ↓                                                                             │
│ Triggers: .github/workflows/build-release.yml                              │
│ ↓                                                                             │
│ ┌───────────────────────────────────────────────────────────────────────┐   │
│ │ 1. Setup Build Environment                                           │   │
│ │    ├─ Check out code                                                 │   │
│ │    ├─ Setup JDK 17                                                   │   │
│ │    └─ Cache Gradle (for speed)                                       │   │
│ │                                                                        │   │
│ │ 2. Build & Test                                                      │   │
│ │    ├─ ./gradlew assembleRelease  (APK optimized)                     │   │
│ │    ├─ ./gradlew assembleDebug    (Debug APK)                         │   │
│ │    └─ ./gradlew test             (Run all tests)                     │   │
│ │                                                                        │   │
│ │ 3. Generate Release Info                                             │   │
│ │    ├─ Extract version from tag (v1.1)                               │   │
│ │    ├─ Get build metadata (date, commit)                             │   │
│ │    └─ Generate release notes                                         │   │
│ │                                                                        │   │
│ │ 4. Create GitHub Release                                             │   │
│ │    ├─ Use GitHub API to create release                              │   │
│ │    ├─ Include version and release notes                             │   │
│ │    └─ Mark as non-draft                                              │   │
│ │                                                                        │   │
│ │ 5. Upload Artifacts                                                  │   │
│ │    ├─ app-release-v1.1.apk (production)                            │   │
│ │    ├─ app-debug-v1.1.apk (debug)                                    │   │
│ │    └─ Test reports (HTML)                                            │   │
│ │                                                                        │   │
│ │ ⏱️ Duration: 10-15 minutes                                            │   │
│ │ 📊 Status: Check Actions page                                        │   │
│ │ ✅ Success: Release published!                                       │   │
│ └───────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘


STEP 5: RELEASE PUBLISHED 🎉
═══════════════════════════════════════════════════════════════════════════════
┌─────────────────────────────────────────────────────────────────────────────┐
│ GitHub Release Created:                                                      │
│ https://github.com/MannixHu/android_demo/releases/tag/v1.1                 │
│                                                                              │
│ Contains:                                                                    │
│  ├─ Release notes                                                           │
│  ├─ app-release-v1.1.apk (5MB, optimized)                                 │
│  ├─ app-debug-v1.1.apk (8MB, debug)                                       │
│  ├─ Test reports link                                                       │
│  └─ Build metadata (date, commit hash)                                     │
│                                                                              │
│ Users can now:                                                              │
│  ├─ Download APK directly                                                   │
│  ├─ Get automatic update notification from app                              │
│  └─ Install latest version ✅                                              │
└─────────────────────────────────────────────────────────────────────────────┘


CONTINUOUS INTEGRATION (Every Push)
═══════════════════════════════════════════════════════════════════════════════

┌─────────────────────────────────────────────────────────────┐
│ Code pushed to main branch                                   │
│ ↓                                                             │
│ Triggers: .github/workflows/ci.yml                          │
│ ↓                                                             │
│ ┌──────────────────────────────────────────────────────┐    │
│ │ 1. Build Debug APK                                   │    │
│ │ 2. Run Unit Tests                                    │    │
│ │ 3. Lint Checks                                       │    │
│ │ 4. Generate Reports                                  │    │
│ │                                                       │    │
│ │ ✅ Build Success                                     │    │
│ │ 📊 Report uploaded                                   │    │
│ │ 💬 Auto-comment on PR: "Build Successful"           │    │
│ │                                                       │    │
│ │ ⏱️ Duration: 5-10 minutes                            │    │
│ └──────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 Quick Reference Commands

### Version Bump
```bash
# Edit version in app/build.gradle.kts
vim app/build.gradle.kts

# Update both:
# versionCode = 2          (integer, always increment)
# versionName = "1.1"      (string, semantic versioning)
```

### Commit & Tag
```bash
# Commit all changes
git add .
git commit -m "Release version 1.1

- Feature 1
- Feature 2
- Bug fix 1"

# Create annotated tag
git tag -a v1.1 -m "Version 1.1 release"

# Verify tag
git tag -l
git show v1.1
```

### Push to GitHub (Triggers Build!)
```bash
# Push commits
git push origin main

# Push specific tag
git push origin v1.1

# Or push all at once
git push origin main --tags

# Verify
git remote -v
```

### Monitor Build
```bash
# View GitHub Actions
# https://github.com/MannixHu/android_demo/actions

# View specific release
# https://github.com/MannixHu/android_demo/releases/tag/v1.1

# Download APK
# Click release → Download app-release-v1.1.apk
```

---

## 🎯 Semantic Versioning Guide

```
Version Format: v{MAJOR}.{MINOR}.{PATCH}

Examples:
  v1.0      - First release (MAJOR.MINOR)
  v1.1      - New features (MAJOR.MINOR.PATCH)
  v1.0.1    - Bug fix (MAJOR.MINOR.PATCH)
  v2.0      - Breaking changes (MAJOR.MINOR.PATCH)
  v1.0-beta - Pre-release version
  v1.0-rc1  - Release candidate

Rules:
  • MAJOR: Incompatible API changes, major features
  • MINOR: New features, backwards compatible
  • PATCH: Bug fixes, minor improvements
  • Always increment versionCode (integer)
  • Update versionName (string) to match tag
```

---

## 🔧 Troubleshooting

### Tag Already Exists
```bash
# Delete local tag
git tag -d v1.1

# Delete remote tag
git push origin :refs/tags/v1.1

# Create new tag
git tag -a v1.1 -m "Version 1.1"
git push origin v1.1
```

### Wrong Tag Message
```bash
# Fix tag annotation
git tag -a v1.1 -f -m "New message"
git push origin v1.1 -f
```

### Build Failed
1. Check: https://github.com/MannixHu/android_demo/actions
2. Click the failed workflow
3. View logs for error details
4. Common issues:
   - Gradle build error → Fix locally, re-push
   - Test failure → Fix test, re-tag
   - Permission issue → Check secrets/tokens

### APK Not Uploaded
1. Verify build succeeded in Actions logs
2. Check if workflow completed
3. Check release page: https://github.com/MannixHu/android_demo/releases
4. Manually upload if needed:
   - Go to release page
   - Click "Edit"
   - Drag & drop APK file
   - Save

---

## 📊 Example Release Timeline

### Development (Days 1-5)
```
Mon: Feature implementation
Tue: Code review & fixes
Wed: Testing & bug fixes
Thu: Final polish
Fri: Prepare release
     ↓
     Update version to 1.1
     Run local tests
     Create commit & tag
     Push to GitHub
```

### Release Day (Automated)
```
09:00 - Push tag → GitHub receives v1.1
09:05 - GitHub Actions triggered
09:10 - Build starts (JDK setup, cache)
09:12 - APK compilation
09:15 - Tests execution
09:18 - Release creation
09:20 - APK upload
09:25 - Release published ✅

Users get notification: "Update available v1.1"
Users click "Download"
Automatic install happens ✨
```

---

## ✅ Release Checklist

Before creating a tag:
- [ ] Code reviewed and approved
- [ ] All tests passing: `./gradlew test`
- [ ] Build locally: `./gradlew assembleRelease`
- [ ] Version updated in `app/build.gradle.kts`
- [ ] Git status clean: `git status`
- [ ] Changes committed: `git add . && git commit`
- [ ] Tag created: `git tag -a v1.1 -m "Release v1.1"`
- [ ] Ready to push: `git push origin main --tags`

After pushing:
- [ ] GitHub Actions started: https://github.com/MannixHu/android_demo/actions
- [ ] Build completed (10-15 min)
- [ ] Release created: https://github.com/MannixHu/android_demo/releases
- [ ] APK files uploaded
- [ ] Release notes generated
- [ ] Test reports available
- [ ] Verify APK by downloading & testing

---

## 🎓 Learning More

- [Semantic Versioning](https://semver.org/)
- [Git Tagging](https://git-scm.com/book/en/v2/Git-Basics-Tagging)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)
- [GitHub Actions](https://docs.github.com/en/actions)
- [Android APK Build](https://developer.android.com/build)

---

*This workflow ensures consistent, automated releases with minimal manual effort.*
*Every tag push automatically builds and publishes a release! 🚀*
