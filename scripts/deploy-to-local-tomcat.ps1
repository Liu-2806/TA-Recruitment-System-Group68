param(
    [string]$TomcatPath = "",
    [string]$AppName = "TA-Recruitment-System-Group68"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$defaultTomcatPath = Join-Path $projectRoot "..\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116"

if ([string]::IsNullOrWhiteSpace($TomcatPath)) {
    $TomcatPath = $defaultTomcatPath
}

$TomcatPath = [System.IO.Path]::GetFullPath($TomcatPath)
$sourceWebapp = Join-Path $projectRoot "src\main\webapp"
$targetWebapps = Join-Path $TomcatPath "webapps"
$targetApp = Join-Path $targetWebapps $AppName

if (-not (Test-Path $TomcatPath)) {
    throw "Tomcat path not found: $TomcatPath"
}

if (-not (Test-Path $sourceWebapp)) {
    throw "Webapp path not found: $sourceWebapp"
}

if (-not (Test-Path $targetWebapps)) {
    throw "Tomcat webapps directory not found: $targetWebapps"
}

if (Test-Path $targetApp) {
    Remove-Item -Recurse -Force $targetApp
}

New-Item -ItemType Directory -Path $targetApp | Out-Null
Copy-Item -Path (Join-Path $sourceWebapp "*") -Destination $targetApp -Recurse -Force

Write-Host "Deployed webapp to: $targetApp"
Write-Host "Preview URL: http://localhost:8080/$AppName/"
