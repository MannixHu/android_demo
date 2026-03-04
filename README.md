# Android Counter App

一个完整的现代 Android 应用示例，展示最佳实践、自动化工作流和发布流程。

[![GitHub Release](https://img.shields.io/github/v/release/MannixHu/android_demo)](https://github.com/MannixHu/android_demo/releases)
[![Build Status](https://github.com/MannixHu/android_demo/workflows/Continuous%20Integration/badge.svg)](https://github.com/MannixHu/android_demo/actions)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 🎯 项目概述

Android Counter App 是一个完整的计数器应用，展示现代 Android 开发的技术栈和最佳实践:

- **Jetpack Compose** - 声明式 UI 框架
- **MVVM 架构** - 清洁的代码组织
- **Hilt 依赖注入** - 简洁的依赖管理
- **Room 数据库** - 本地数据持久化
- **GitHub Actions** - 自动化 CI/CD
- **自动更新** - 从 GitHub Releases 获取更新

## ✨ 核心功能

### 计数器应用
- 📱 显示当前计数值
- ➕ 增加 (+1)
- ➖ 减少 (-1)
- 🔄 重置为 0
- 💾 本地数据库持久化
- 🌙 支持浅色/深色主题

### 自动更新
- 🔍 自动检查新版本
- 📥 从 GitHub Releases 下载 APK
- ✅ 用户确认后自动安装
- 📊 版本号对比逻辑
- ⚡ 后台静默检查

### 自动化工作流
- 🤖 每次推送自动运行测试
- 📦 标签推送自动构建和发布 APK
- 📋 自动生成发布说明
- 📧 自动创建 GitHub Release
- 🎯 完全无需手动操作

## 🏗️ 项目架构

```
AndroidCounterApp
│
├── 🎨 UI Layer (Jetpack Compose)
│   ├── CounterScreen.kt - 主界面
│   └── theme/ - Material Design 3 主题
│
├── 📊 ViewModel Layer
│   └── CounterViewModel.kt - 状态管理
│
├── 🏢 Domain Layer
│   ├── repository/ - 仓储接口
│   └── usecase/ - 业务逻辑用例
│
├── 💾 Data Layer
│   ├── local/ - Room 数据库
│   ├── remote/ - GitHub API 客户端
│   └── datasource/ - 数据源
│
└── 🔧 DI Layer
    └── di/ - Hilt 依赖注入配置
```

### 数据流

```
UI Layer (Compose)
    ↓ 用户交互
ViewModel (StateFlow)
    ↓ 触发操作
Use Cases (业务逻辑)
    ↓ 调用接口
Repository (数据仓储)
    ↓ 操作数据
Data Sources (Room/API)
    ↓ 持久化
SQLite Database / GitHub API
```

## 🚀 快速开始

### 前提条件

- JDK 17+
- Android SDK 28+ (minSdk)
- Android Studio Giraffe+
- Git

### 本地运行

#### 方式 1: Android Studio

1. 克隆仓库
   ```bash
   git clone https://github.com/MannixHu/android_demo.git
   cd android_demo
   ```

2. 在 Android Studio 中打开项目
3. 点击 "Run" 或按 Shift + F10

#### 方式 2: 命令行

```bash
# 编译
./gradlew build

# 运行调试版本
./gradlew runDebug

# 运行测试
./gradlew test

# 构建 Release APK
./gradlew assembleRelease
```

### macOS 上运行

详见 [QUICKSTART.md](QUICKSTART.md) 完整指南

## 📱 应用演示

### 主界面

```
┌─────────────────────┐
│      Counter        │  ← 标题
│                     │
│         42          │  ← 当前计数
│                     │
│  [-1]  [Reset] [+1] │  ← 控制按钮
│                     │
└─────────────────────┘
```

### 功能

| 按钮 | 功能 | 颜色 |
|------|------|------|
| +1 | 增加计数 | 蓝色 (Primary) |
| -1 | 减少计数 | 灰色 (Secondary) |
| Reset | 重置为 0 | 红色 (Error) |

## 🔄 自动更新流程

```
App Startup
    ↓
CheckUpdateUseCase
    ↓
UpdateService (Retrofit)
    ↓
GitHub API: /repos/MannixHu/android_demo/releases/latest
    ↓ (Check Version)
Has Update?
    ├─ Yes → Show Notification
    │   └─ User clicks "Update"
    │       ├─ Download APK
    │       ├─ Wait for completion
    │       └─ Install APK
    └─ No → Continue normally
```

## 🛠️ 技术栈

### 构建工具
| 工具 | 版本 |
|------|------|
| AGP | 9.0.1 |
| Gradle | 9.1.0 |
| Kotlin | 2.0.0 |
| JDK | 17 |

### 框架库
| 库 | 版本 | 用途 |
|----|------|------|
| Compose | 1.7.0 | UI 框架 |
| Material 3 | 1.3.0 | 设计系统 |
| Hilt | 2.52 | DI 框架 |
| Room | 2.7.0 | 数据库 |
| Coroutines | 1.9.0 | 异步编程 |
| Retrofit | 2.11.0 | HTTP 客户端 |
| OkHttp | 4.12.0 | 网络库 |

### 测试
| 库 | 版本 |
|----|------|
| JUnit | 4.13.2 |
| MockK | 1.13.10 |
| Coroutines Test | 1.9.0 |

## 📦 一键发版

### 快速发布

只需一条命令发布新版本：

```bash
./release.sh
```

脚本会自动：
1. 读取当前版本
2. 计算新版本号 (e.g., 1.0 → 1.1.0)
3. 更新 build.gradle.kts
4. 提交和标签
5. 推送到 GitHub
6. **触发 GitHub Actions 自动构建**

### 发布过程

```
$ ./release.sh
Current version: 1.0 (code: 1)
New version will be: 1.1.0 (code: 2)
Continue with release? (y/n) y
✅ Version updated
✅ Commit created
✅ Tag created: v1.1.0
✅ Pushed to GitHub

⏱️ GitHub Actions 自动构建开始...
📦 10-15 分钟后 APK 已准备下载
```

详见 [RELEASE_SCRIPT_GUIDE.md](RELEASE_SCRIPT_GUIDE.md)

## 🔄 CI/CD 工作流

### 持续集成 (每次 push)

```yaml
触发事件: push to main
步骤:
  1. 检出代码
  2. 设置 JDK 17
  3. Lint 检查
  4. 构建 Debug APK
  5. 运行单元测试
  6. 生成测试报告
  7. 上传构建产物
```

### 自动发布 (标签 push v*)

```yaml
触发事件: push tag v*.*.*
步骤:
  1. 检出代码
  2. 构建 Release APK (优化)
  3. 构建 Debug APK
  4. 运行全部测试
  5. 创建 GitHub Release
  6. 上传 APK 文件到 Release
  7. 生成自动化发布说明
```

查看详情: [GIT_CI_CD_GUIDE.md](GIT_CI_CD_GUIDE.md)

## 📚 文档

项目包含完整的文档：

| 文档 | 说明 |
|------|------|
| [QUICKSTART.md](QUICKSTART.md) | 快速开始指南 |
| [RELEASE.md](RELEASE.md) | 发布和更新指南 |
| [RELEASE_SCRIPT_GUIDE.md](RELEASE_SCRIPT_GUIDE.md) | 一键发版脚本使用 |
| [RELEASE_WORKFLOW.md](RELEASE_WORKFLOW.md) | 发布流程可视化 |
| [GIT_CI_CD_GUIDE.md](GIT_CI_CD_GUIDE.md) | Git 和 CI/CD 详解 |
| [CI_CD_FIX_SUMMARY.md](CI_CD_FIX_SUMMARY.md) | CI/CD 问题修复说明 |

## 🔗 重要链接

| 资源 | 链接 |
|------|------|
| GitHub 仓库 | https://github.com/MannixHu/android_demo |
| Releases | https://github.com/MannixHu/android_demo/releases |
| GitHub Actions | https://github.com/MannixHu/android_demo/actions |
| Issues | https://github.com/MannixHu/android_demo/issues |

## 📊 项目统计

```
├─ 代码行数: ~1,500 行 Kotlin
├─ 测试覆盖: 5 个单元测试
├─ 文档: 1,500+ 行详细文档
├─ Git 提交: 7+ 个有意义的提交
├─ 分支: main (主分支)
└─ 标签: v1.0 (首个发布版本)
```

## 🎓 学习价值

通过本项目学习：

- ✅ **Jetpack Compose** - 现代声明式 UI 开发
- ✅ **MVVM 架构** - 清洁的代码组织
- ✅ **Hilt DI** - 依赖注入最佳实践
- ✅ **Room 数据库** - 本地数据持久化
- ✅ **Kotlin Coroutines** - 异步编程
- ✅ **REST API 集成** - Retrofit + OkHttp
- ✅ **GitHub Actions** - CI/CD 自动化
- ✅ **APK 发布** - 完整的发布流程
- ✅ **应用更新** - 自动更新机制
- ✅ **单元测试** - 测试驱动开发

## 📋 完整功能清单

### 核心应用
- [x] Jetpack Compose UI
- [x] Material Design 3 主题
- [x] 计数器显示
- [x] 增加/减少/重置功能
- [x] Room 数据库持久化
- [x] StateFlow 响应式状态

### 自动更新
- [x] GitHub API 集成
- [x] 版本检查逻辑
- [x] APK 自动下载
- [x] 用户确认安装
- [x] 错误处理和重试

### CI/CD 自动化
- [x] GitHub Actions 工作流
- [x] 自动编译和测试
- [x] 自动 APK 生成
- [x] 自动 Release 创建
- [x] Gradle Wrapper 配置

### 开发工具
- [x] 一键发版脚本
- [x] 自动版本计算
- [x] 完整的文档

### 测试
- [x] ViewModel 单元测试
- [x] UseCase 单元测试
- [x] Repository 测试设置

## 🎯 使用场景

### 1. 学习 Android 开发
- 完整的项目结构示例
- 现代技术栈展示
- 最佳实践参考

### 2. 生产环境模板
- 可直接用于实际项目
- 完整的工作流支持
- 自动化的发布流程

### 3. 团队参考
- 代码组织方式
- Git 工作流
- CI/CD 最佳实践

## 🚦 构建和部署

### 构建状态

最新构建: [![GitHub Actions](https://github.com/MannixHu/android_demo/workflows/Continuous%20Integration/badge.svg)](https://github.com/MannixHu/android_demo/actions)

### 最新发布

v1.0.0 - [View Release](https://github.com/MannixHu/android_demo/releases/tag/v1.0)

## 💡 最佳实践

### 代码组织
- 清洁架构 (UI/Domain/Data 分层)
- MVVM 模式 + StateFlow
- 依赖注入 (Hilt)
- 单元测试覆盖

### Git 工作流
- 语义化版本号
- 清晰的提交信息
- 标签管理

### CI/CD
- 自动化测试
- 自动化构建
- 自动化发布
- 零手动操作

## 📝 许可证

MIT License - 详见 [LICENSE](LICENSE)

## 🤝 贡献

欢迎贡献！请：

1. Fork 本仓库
2. 创建你的功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📧 联系方式

- GitHub: [@MannixHu](https://github.com/MannixHu)
- Issues: [项目 Issue](https://github.com/MannixHu/android_demo/issues)

## 🙏 致谢

感谢所有开源库和工具的贡献者：
- Jetpack 团队
- Kotlin 团队
- Android 社区

---

**项目状态**: ✅ Production Ready

最后更新: 2026-03-05
