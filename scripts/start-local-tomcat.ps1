param(
    [string]$TomcatPath = "",
    [string]$DataDir = ""
)

$projectRoot = Split-Path -Parent $PSScriptRoot
$defaultTomcatPath = Join-Path $projectRoot "..\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116"

if ([string]::IsNullOrWhiteSpace($TomcatPath)) {
    $TomcatPath = $defaultTomcatPath
}

$TomcatPath = [System.IO.Path]::GetFullPath($TomcatPath)
$startupScript = Join-Path $TomcatPath "bin\startup.bat"
$tomcatBin = Join-Path $TomcatPath "bin"

if (-not (Test-Path $startupScript)) {
    throw "Tomcat startup script not found: $startupScript"
}

if (-not (Test-Path $tomcatBin)) {
    throw "Tomcat bin directory not found: $tomcatBin"
}

$env:CATALINA_HOME = $TomcatPath
$env:CATALINA_BASE = $TomcatPath

if ([string]::IsNullOrWhiteSpace($DataDir)) {
    $DataDir = Join-Path $projectRoot "data"
}

if (-not (Test-Path $DataDir)) {
    throw "Data directory not found: $DataDir"
}

$env:TA_DATA_DIR = (Resolve-Path $DataDir).Path

Start-Process -FilePath $startupScript -WorkingDirectory $tomcatBin
Write-Host "Tomcat start command sent."
Write-Host "After Tomcat starts, open your deployed app URL in the browser."
