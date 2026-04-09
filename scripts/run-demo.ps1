param(
    [string]$TomcatPath = "F:\softwareengineering\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116",
    [string]$AppName = "TA-Recruitment-System-Group68",
    [switch]$SkipStart
)

$ErrorActionPreference = "Stop"

$scriptRoot = $PSScriptRoot
$deployScript = Join-Path $scriptRoot "deploy-to-local-tomcat.ps1"
$startScript = Join-Path $scriptRoot "start-local-tomcat.ps1"

& $deployScript -TomcatPath $TomcatPath -AppName $AppName

if (-not $SkipStart) {
    & $startScript -TomcatPath $TomcatPath
}

Write-Host "Deployment helper finished."
