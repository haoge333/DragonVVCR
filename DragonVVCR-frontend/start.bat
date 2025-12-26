@echo off
chcp 65001 >nul
title DragonVVCR 前端项目启动器

echo.
echo ========================================
echo   DragonVVCR 前端项目启动器
echo ========================================
echo.

:: 检查 Node.js 是否安装
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Node.js，请先安装 Node.js
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)

:: 显示 Node.js 版本
echo [信息] 当前 Node.js 版本:
node --version

:: 检查 package.json 是否存在
if not exist "package.json" (
    echo [错误] 未找到 package.json 文件，请确保在项目根目录运行此脚本
    pause
    exit /b 1
)

:: 检查 node_modules 是否存在
if not exist "node_modules" (
    echo [信息] 正在安装项目依赖...
    npm install
    if %errorlevel% neq 0 (
        echo [错误] 依赖安装失败，请检查网络连接或 npm 配置
        pause
        exit /b 1
    )
)

echo.
echo [信息] 正在启动开发服务器...
echo [提示] 按 Ctrl+C 可停止服务器
echo.

:: 启动开发服务器
npm run dev

pause
