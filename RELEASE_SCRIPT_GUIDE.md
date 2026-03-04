# 一键发版脚本使用指南

## 🚀 快速开始

```bash
cd /Users/mannix/Project/android_demo
./release.sh
```

就这么简单！脚本会自动:
1. 读取当前版本
2. 计算新版本号
3. 更新 build.gradle.kts
4. 提交和标签
5. 推送到 GitHub
6. 触发 CI/CD 自动构建

---

## 📋 功能说明

### 自动版本计算

脚本从 `app/build.gradle.kts` 读取当前版本，然后自动增加小版本号:

```
当前版本: 1.0 (code: 1)
新版本:  1.1.0 (code: 2)
```

版本规则:
- **主版本号 (Major)**: 破坏性更新时增加
- **小版本号 (Minor)**: 脚本自动增加 (+1)
- **补丁版本 (Patch)**: 重置为 0
- **构建代码 (Code)**: 自动增加 (+1)

### 自动化步骤

1. **验证环境**
   ```bash
   ✓ Git 仓库检查
   ✓ build.gradle.kts 存在检查
   ✓ 版本号提取
   ```

2. **版本更新**
   ```bash
   ✓ 计算新版本号
   ✓ 显示变更预览
   ✓ 要求用户确认
   ```

3. **文件修改**
   ```bash
   ✓ 更新 versionCode
   ✓ 更新 versionName
   ✓ 验证更新成功
   ```

4. **Git 操作**
   ```bash
   ✓ git add app/build.gradle.kts
   ✓ git commit -m "Release version X.X.X"
   ✓ git tag -a vX.X.X -m "..."
   ```

5. **推送到 GitHub**
   ```bash
   ✓ git push origin main
   ✓ git push origin vX.X.X  ← 触发 CI/CD!
   ```

---

## 💻 使用示例

### 示例 1: 简单发布

```bash
$ ./release.sh

╔════════════════════════════════════════════════════════════════╗
║         Android Counter App - One-Click Release Script        ║
╚════════════════════════════════════════════════════════════════╝

📋 Reading current version from app/build.gradle.kts...
Current version: 1.0 (code: 1)
New version will be: 1.1.0 (code: 2)
Continue with release? (y/n) y

🔧 Updating version in app/build.gradle.kts...
✅ Version updated to 1.1.0

📝 Creating commit...
✅ Commit created

🏷️  Creating git tag v1.1.0...
✅ Tag created: v1.1.0

🚀 Pushing to GitHub...
✅ Main branch pushed
✅ Tag v1.1.0 pushed (GitHub Actions triggered!)

╔════════════════════════════════════════════════════════════════╗
✨ Release v1.1.0 Started Successfully! ✨
╚════════════════════════════════════════════════════════════════╝

📊 Release Information:
  Version: 1.1.0
  Build Code: 2
  Tag: v1.1.0

🔗 Monitor build progress:
  https://github.com/MannixHu/android_demo/actions

📥 Download APK when ready:
  https://github.com/MannixHu/android_demo/releases/tag/v1.1.0

⏱️  GitHub Actions will complete in 10-15 minutes
```

### 示例 2: 快速迭代

```bash
# 第一个版本
$ ./release.sh
→ v1.0 → v1.1.0

# 修复 bug
$ git add . && git commit -m "Fix bug in CounterViewModel"
$ git push origin main

# 发布修复版
$ ./release.sh
→ v1.1.0 → v1.2.0

# 添加新功能
$ git add . && git commit -m "Add analytics tracking"
$ git push origin main

# 发布新功能
$ ./release.sh
→ v1.2.0 → v1.3.0
```

---

## ⚙️ 脚本实现细节

### 版本提取

```bash
# 从 build.gradle.kts 提取版本
CURRENT_VERSION=$(grep -m 1 'versionName = ' app/build.gradle.kts | \
                  sed 's/.*versionName = "\([^"]*\)".*/\1/')

CURRENT_VERSION_CODE=$(grep -m 1 'versionCode = ' app/build.gradle.kts | \
                       sed 's/.*versionCode = \([0-9]*\).*/\1/')
```

### 版本计算

```bash
# 分解版本号
IFS='.' read -ra VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR=${VERSION_PARTS[0]}
MINOR=${VERSION_PARTS[1]:-0}

# 增加小版本号
NEW_MINOR=$((MINOR + 1))
NEW_VERSION="$MAJOR.$NEW_MINOR.0"
NEW_VERSION_CODE=$((CURRENT_VERSION_CODE + 1))
```

### 文件更新 (macOS/Linux)

```bash
# 更新 versionCode
sed -i '' "s/versionCode = [0-9]*/versionCode = $NEW_VERSION_CODE/" app/build.gradle.kts

# 更新 versionName
sed -i '' "s/versionName = \"[^\"]*\"/versionName = \"$NEW_VERSION\"/" app/build.gradle.kts
```

### Git 标签

```bash
git tag -a "v$NEW_VERSION" -m "Release version $NEW_VERSION

Version: $NEW_VERSION
Build code: $NEW_VERSION_CODE
Release date: $(date '+%Y-%m-%d')
Commit: $(git rev-parse --short HEAD)

https://github.com/MannixHu/android_demo/releases/tag/v$NEW_VERSION"
```

---

## 🔍 错误处理

脚本包含多层错误检查:

```bash
✓ Git 仓库存在检查
  if [ ! -d ".git" ]; then exit 1; fi

✓ 构建文件存在检查
  if [ ! -f "app/build.gradle.kts" ]; then exit 1; fi

✓ 版本号提取验证
  if [ -z "$CURRENT_VERSION" ]; then exit 1; fi

✓ 标签重复检查
  if git rev-parse "v$NEW_VERSION" >/dev/null 2>&1; then exit 1; fi

✓ 版本更新验证
  if [ "$UPDATED_VERSION" != "$NEW_VERSION" ]; then exit 1; fi
```

---

## 🎨 彩色输出说明

脚本使用彩色输出提高可读性:

| 颜色 | 含义 | 示例 |
|------|------|------|
| 🔵 蓝色 | 信息步骤 | 📋 Reading current version... |
| 🟢 绿色 | 成功完成 | ✅ Version updated to 1.1.0 |
| 🟡 黄色 | 警告信息 | ℹ️  No changes detected |
| 🔴 红色 | 错误信息 | ❌ Error: Not a git repository |

---

## 📖 完整工作流程

### 日常开发

```
1. 开发新功能
   └─ vim app/src/main/kotlin/...
   └─ git add . && git commit -m "Add feature X"
   └─ git push origin main
      └─ GitHub Actions CI 自动运行

2. 功能完成，准备发布
   └─ ./release.sh
      └─ 脚本自动:
         ├─ 更新版本号
         ├─ 提交更改
         ├─ 创建标签
         └─ 推送到 GitHub

   └─ GitHub Actions Release 自动:
      ├─ 构建优化的 APK
      ├─ 运行全部测试
      ├─ 创建 GitHub Release
      └─ 上传 APK 文件

3. 验证发布成功
   └─ https://github.com/MannixHu/android_demo/releases
   └─ 下载并测试 APK

4. 用户获取更新
   └─ 应用检查新版本
   └─ 用户点击 "Update"
   └─ 自动下载和安装
```

### 多版本管理

```
v1.0.0 ──────────┬────────── v1.1.0 ────────── v1.2.0
       (initial)  │          (feature)        (fix)
                  └─────────────────────────────────→ main branch

每次发布:
  $ ./release.sh  (自动版本递增)
```

---

## 🔐 安全特性

### 用户确认

脚本在关键操作前要求用户确认:

```bash
echo "New version will be: $NEW_VERSION (code: $NEW_VERSION_CODE)"
read -p "Continue with release? (y/n) " -n 1 -r
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Release cancelled"
    exit 1
fi
```

### 原子操作

Git 操作以原子方式执行 (`set -e`):
- 任何步骤失败，脚本立即停止
- 不会留下不完整的状态

### 验证更新

脚本验证版本号确实已更新:

```bash
UPDATED_VERSION=$(grep -m 1 'versionName = ' app/build.gradle.kts | ...)
if [ "$UPDATED_VERSION" != "$NEW_VERSION" ]; then
    exit 1  # 验证失败
fi
```

---

## 📊 版本号策略

### 语义版本化 (Semantic Versioning)

格式: `MAJOR.MINOR.PATCH`

- **MAJOR**: 不兼容的 API 更新（手动更新）
- **MINOR**: 向后兼容的功能更新（脚本自动 +1）
- **PATCH**: 向后兼容的 bug 修复（脚本重置为 0）

示例:
```
v1.0.0  → v1.1.0  (新功能)
v1.1.0  → v1.2.0  (另一个新功能)
v1.2.0  → v2.0.0  (破坏性更新，需手动编辑)
```

---

## ✅ 检查清单

发布前:
- [ ] 功能开发完成
- [ ] 所有测试通过
- [ ] 代码已提交到 main
- [ ] 运行 `./release.sh`

发布后:
- [ ] 访问 GitHub Actions 确认构建成功
- [ ] 检查 Release 页面 APK 已上传
- [ ] 下载 APK 进行快速测试
- [ ] 确认应用更新通知生效

---

## 🆘 故障排除

### 脚本找不到

```bash
# 确保脚本可执行
chmod +x release.sh

# 确保在正确的目录
cd /Users/mannix/Project/android_demo

# 运行脚本
./release.sh
```

### 版本提取失败

```bash
# 检查 build.gradle.kts 格式
grep "versionName\|versionCode" app/build.gradle.kts

# 示例输出应该是:
# versionCode = 1
# versionName = "1.0"
```

### Git 操作失败

```bash
# 检查 git 状态
git status

# 如果有未提交的更改
git add . && git commit -m "Your message"
git push origin main

# 然后重新运行脚本
./release.sh
```

### 标签已存在

```bash
# 删除失败的标签
git tag -d v1.1.0
git push origin :refs/tags/v1.1.0

# 重新运行脚本
./release.sh
```

---

## 📚 相关文档

- [RELEASE.md](RELEASE.md) - 完整的发布指南
- [GIT_CI_CD_GUIDE.md](GIT_CI_CD_GUIDE.md) - Git 和 CI/CD 详解
- [RELEASE_WORKFLOW.md](RELEASE_WORKFLOW.md) - 工作流可视化

---

## 🎯 总结

使用 `release.sh` 脚本，发布一个新版本只需:

```bash
./release.sh
```

脚本会自动处理所有繁琐的操作，让你专注于开发！

---

*最后更新: 2026-03-05*
