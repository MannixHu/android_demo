#!/bin/bash

#
# Android Counter App - One-Click Release Script
# Usage: ./release.sh
#
# This script automatically:
# 1. Gets current version from app/build.gradle.kts
# 2. Increments minor version
# 3. Commits changes
# 4. Creates git tag
# 5. Pushes to GitHub
# 6. Triggers GitHub Actions CI/CD
#

set -e  # Exit on error

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║         Android Counter App - One-Click Release Script        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get current directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo -e "${RED}❌ Error: Not a git repository${NC}"
    exit 1
fi

# Get current version from app/build.gradle.kts
echo -e "${BLUE}📋 Reading current version from app/build.gradle.kts...${NC}"

if [ ! -f "app/build.gradle.kts" ]; then
    echo -e "${RED}❌ Error: app/build.gradle.kts not found${NC}"
    exit 1
fi

# Extract current version
CURRENT_VERSION=$(grep -m 1 'versionName = ' app/build.gradle.kts | sed 's/.*versionName = "\([^"]*\)".*/\1/')
CURRENT_VERSION_CODE=$(grep -m 1 'versionCode = ' app/build.gradle.kts | sed 's/.*versionCode = \([0-9]*\).*/\1/')

if [ -z "$CURRENT_VERSION" ]; then
    echo -e "${RED}❌ Error: Could not extract current version${NC}"
    exit 1
fi

echo -e "${GREEN}Current version: $CURRENT_VERSION (code: $CURRENT_VERSION_CODE)${NC}"

# Parse version components
IFS='.' read -ra VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR=${VERSION_PARTS[0]}
MINOR=${VERSION_PARTS[1]:-0}
PATCH=${VERSION_PARTS[2]:-0}

# Increment minor version
NEW_MINOR=$((MINOR + 1))
NEW_VERSION="$MAJOR.$NEW_MINOR.0"
NEW_VERSION_CODE=$((CURRENT_VERSION_CODE + 1))

echo -e "${YELLOW}New version will be: $NEW_VERSION (code: $NEW_VERSION_CODE)${NC}"
echo ""

# Ask for confirmation
read -p "Continue with release? (y/n) " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${RED}Release cancelled${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}🔧 Updating version in app/build.gradle.kts...${NC}"

# Update versionCode
sed -i '' "s/versionCode = [0-9]*/versionCode = $NEW_VERSION_CODE/" app/build.gradle.kts

# Update versionName
sed -i '' "s/versionName = \"[^\"]*\"/versionName = \"$NEW_VERSION\"/" app/build.gradle.kts

echo -e "${GREEN}✅ Version updated to $NEW_VERSION${NC}"

# Verify changes
UPDATED_VERSION=$(grep -m 1 'versionName = ' app/build.gradle.kts | sed 's/.*versionName = "\([^"]*\)".*/\1/')
UPDATED_VERSION_CODE=$(grep -m 1 'versionCode = ' app/build.gradle.kts | sed 's/.*versionCode = \([0-9]*\).*/\1/')

if [ "$UPDATED_VERSION" != "$NEW_VERSION" ] || [ "$UPDATED_VERSION_CODE" != "$NEW_VERSION_CODE" ]; then
    echo -e "${RED}❌ Error: Version update verification failed${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}📝 Creating commit...${NC}"

# Check git status
if git diff --quiet app/build.gradle.kts; then
    echo -e "${YELLOW}ℹ️  No changes detected, skipping commit${NC}"
else
    git add app/build.gradle.kts
    git commit -m "Release version $NEW_VERSION

Version: $NEW_VERSION
Build code: $NEW_VERSION_CODE
Timestamp: $(date '+%Y-%m-%d %H:%M:%S')

Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>"
    echo -e "${GREEN}✅ Commit created${NC}"
fi

echo ""
echo -e "${BLUE}🏷️  Creating git tag v$NEW_VERSION...${NC}"

# Check if tag already exists
if git rev-parse "v$NEW_VERSION" >/dev/null 2>&1; then
    echo -e "${RED}❌ Error: Tag v$NEW_VERSION already exists${NC}"
    exit 1
fi

git tag -a "v$NEW_VERSION" -m "Release version $NEW_VERSION

Version: $NEW_VERSION
Build code: $NEW_VERSION_CODE
Release date: $(date '+%Y-%m-%d')
Commit: $(git rev-parse --short HEAD)

https://github.com/MannixHu/android_demo/releases/tag/v$NEW_VERSION"

echo -e "${GREEN}✅ Tag created: v$NEW_VERSION${NC}"

echo ""
echo -e "${BLUE}🚀 Pushing to GitHub...${NC}"

# Push commits
git push origin main
echo -e "${GREEN}✅ Main branch pushed${NC}"

# Push tag (this triggers GitHub Actions!)
git push origin "v$NEW_VERSION"
echo -e "${GREEN}✅ Tag v$NEW_VERSION pushed (GitHub Actions triggered!)${NC}"

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo -e "${GREEN}✨ Release v$NEW_VERSION Started Successfully! ✨${NC}"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""
echo -e "${BLUE}📊 Release Information:${NC}"
echo "  Version: $NEW_VERSION"
echo "  Build Code: $NEW_VERSION_CODE"
echo "  Tag: v$NEW_VERSION"
echo ""
echo -e "${BLUE}🔗 Monitor build progress:${NC}"
echo "  https://github.com/MannixHu/android_demo/actions"
echo ""
echo -e "${BLUE}📥 Download APK when ready:${NC}"
echo "  https://github.com/MannixHu/android_demo/releases/tag/v$NEW_VERSION"
echo ""
echo -e "${YELLOW}⏱️  GitHub Actions will complete in 10-15 minutes${NC}"
echo ""
