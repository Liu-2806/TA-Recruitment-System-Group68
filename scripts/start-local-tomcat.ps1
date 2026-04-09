param(
    [string]$TomcatPath = "F:\softwareengineering\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116"
)

$startupScript = Join-Path $TomcatPath "bin\startup.bat"

if (-not (Test-Path $startupScript)) {
    throw "Tomcat startup script not found: $startupScript"
}

Start-Process -FilePath $startupScript
Write-Host "Tomcat start command sent."
Write-Host "After Tomcat starts, open your deployed app URL in the browser."
