# Sets User JAVA_HOME to a JDK (with javac). User variables override Machine JAVA_HOME.
# Run in Windows PowerShell or pwsh (outside restricted sandboxes):  .\scripts\configure-java-home.ps1
param(
    [string]$JdkHome = "C:\Program Files\Java\jdk-17"
)

$ErrorActionPreference = "Stop"

$javac = Join-Path $JdkHome "bin\javac.exe"
if (-not (Test-Path $javac)) {
    throw "Not a valid JDK (missing javac): $javac"
}

[Environment]::SetEnvironmentVariable("JAVA_HOME", $JdkHome, "User")

$bin = Join-Path $JdkHome "bin"
$userPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ([string]::IsNullOrWhiteSpace($userPath)) {
    [Environment]::SetEnvironmentVariable("Path", $bin, "User")
}
else {
    $parts = $userPath -split ";" | Where-Object { $_ -and $_.Trim() }
    if ($parts -notcontains $bin) {
        [Environment]::SetEnvironmentVariable("Path", ($userPath.TrimEnd(";") + ";" + $bin), "User")
    }
}

Write-Host "JAVA_HOME (User) = $JdkHome"
Write-Host "PATH updated to include: $bin"
Write-Host "Close and reopen terminals (and Cursor) so new sessions pick this up."
