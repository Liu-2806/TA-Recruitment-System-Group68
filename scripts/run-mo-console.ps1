param(
    [string]$JavaHome = "",
    [string]$WorkDirName = ".mo-console",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$workRoot = Join-Path $projectRoot $WorkDirName
$classesDir = Join-Path $workRoot "classes"
$runnerDir = Join-Path $workRoot "runner"
$downloadsDir = Join-Path $workRoot "downloads"
$defaultDataDir = Join-Path $workRoot "data"
$sourceDataDir = Join-Path $projectRoot "data"

if ([string]::IsNullOrWhiteSpace($JavaHome)) {
    if (-not [string]::IsNullOrWhiteSpace($env:JAVA_HOME)) {
        $JavaHome = $env:JAVA_HOME
    } else {
        $JavaHome = "E:\jdk-21.0.2"
    }
}

$javac = Join-Path $JavaHome "bin\javac.exe"
$java = Join-Path $JavaHome "bin\java.exe"

if (-not (Test-Path $javac)) {
    throw "未找到 javac: $javac"
}

if (-not (Test-Path $java)) {
    throw "未找到 java: $java"
}

if (-not (Test-Path $sourceDataDir)) {
    throw "未找到源数据目录: $sourceDataDir"
}

if ([string]::IsNullOrWhiteSpace($DataDir)) {
    $DataDir = $defaultDataDir

    if (Test-Path $workRoot) {
        Remove-Item -Recurse -Force $workRoot
    }

    New-Item -ItemType Directory -Path $workRoot | Out-Null
    New-Item -ItemType Directory -Path $DataDir | Out-Null
    Copy-Item -Path (Join-Path $sourceDataDir "*") -Destination $DataDir -Recurse -Force
} else {
    if (-not (Test-Path $workRoot)) {
        New-Item -ItemType Directory -Path $workRoot | Out-Null
    }
    if (-not (Test-Path $DataDir)) {
        New-Item -ItemType Directory -Path $DataDir | Out-Null
    }
}

if (Test-Path $classesDir) {
    Remove-Item -Recurse -Force $classesDir
}

if (Test-Path $runnerDir) {
    Remove-Item -Recurse -Force $runnerDir
}

if (Test-Path $downloadsDir) {
    Remove-Item -Recurse -Force $downloadsDir
}

New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $runnerDir | Out-Null
New-Item -ItemType Directory -Path $downloadsDir | Out-Null

$libClasspath = @(
    (Join-Path $projectRoot "lib\pdfbox-app-3.0.2.jar"),
    (Join-Path $projectRoot "lib\gson-2.11.0.jar"),
    (Join-Path $projectRoot "lib\javax.servlet-api-4.0.1.jar")
) -join ";"

$sourceFiles = Get-ChildItem -Recurse -Filter *.java (Join-Path $projectRoot "src\com\bupt\ta") | ForEach-Object { $_.FullName }
& $javac -encoding UTF-8 -cp $libClasspath -d $classesDir $sourceFiles
& $javac -encoding UTF-8 -cp "$classesDir;$libClasspath" -d $runnerDir (Join-Path $projectRoot "scripts\MOConsoleApp.java")

$resolvedDataDir = (Resolve-Path $DataDir).Path
$resolvedDownloadsDir = (Resolve-Path $downloadsDir).Path

Write-Host "使用的数据目录: $resolvedDataDir"
Write-Host "下载输出目录: $resolvedDownloadsDir"
Write-Host ""

& $java "-Dta.data.dir=$resolvedDataDir" "-Dmo.console.output.dir=$resolvedDownloadsDir" -cp "$classesDir;$runnerDir;$libClasspath" MOConsoleApp
