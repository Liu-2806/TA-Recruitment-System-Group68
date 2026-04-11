param(
    [string]$TomcatPath = "",
    [string]$AppName = "TA-Recruitment-System-Group68",
    [string]$JavaHome = "",
    [switch]$SkipStart
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
# Official zip is sometimes extracted with an extra nested folder; try both layouts.
$tomcatCandidateA = Join-Path $projectRoot "..\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116"
$tomcatCandidateB = Join-Path $projectRoot "..\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116-windows-x64\apache-tomcat-9.0.116"

if ([string]::IsNullOrWhiteSpace($TomcatPath)) {
    $TomcatPath = $tomcatCandidateA
    $tryA = Join-Path ([System.IO.Path]::GetFullPath($TomcatPath)) "bin\startup.bat"
    if (-not (Test-Path $tryA)) {
        $TomcatPath = $tomcatCandidateB
    }
}

$TomcatPath = [System.IO.Path]::GetFullPath($TomcatPath)
$classesDir = Join-Path $projectRoot "web\WEB-INF\classes"
$sourceRoot = Join-Path $projectRoot "src\com\bupt\ta"
$dataDir = Join-Path $projectRoot "data"
$targetApp = Join-Path $TomcatPath ("webapps\" + $AppName)
$tomcatBin = Join-Path $TomcatPath "bin"
$startupScript = Join-Path $tomcatBin "startup.bat"
$shutdownScript = Join-Path $tomcatBin "shutdown.bat"

function Resolve-JavaTool {
    param(
        [string]$ToolName,
        [string]$JavaHomePath
    )

    if (-not [string]::IsNullOrWhiteSpace($JavaHomePath)) {
        $candidate = Join-Path $JavaHomePath ("bin\{0}.exe" -f $ToolName)
        if (Test-Path $candidate) {
            return $candidate
        }
        throw "$ToolName not found under JavaHome: $candidate"
    }

    if (-not [string]::IsNullOrWhiteSpace($env:JAVA_HOME)) {
        $candidate = Join-Path $env:JAVA_HOME ("bin\{0}.exe" -f $ToolName)
        if (Test-Path $candidate) {
            return $candidate
        }
    }

    $command = Get-Command $ToolName -ErrorAction SilentlyContinue
    if ($command) {
        return $command.Source
    }

    throw "$ToolName not found. Pass -JavaHome or configure JAVA_HOME / PATH."
}

if (-not (Test-Path $TomcatPath)) {
    throw "Tomcat path not found: $TomcatPath"
}

if (-not (Test-Path $startupScript)) {
    throw "Tomcat startup script not found: $startupScript"
}

if (-not (Test-Path $shutdownScript)) {
    throw "Tomcat shutdown script not found: $shutdownScript"
}

if (-not (Test-Path $sourceRoot)) {
    throw "Source directory not found: $sourceRoot"
}

$javac = Resolve-JavaTool -ToolName "javac" -JavaHomePath $JavaHome
$classpath = @(
    (Join-Path $projectRoot "lib\javax.servlet-api-4.0.1.jar"),
    (Join-Path $projectRoot "lib\gson-2.11.0.jar"),
    (Join-Path $projectRoot "lib\pdfbox-app-3.0.2.jar")
) -join ";"

$sourceFiles = Get-ChildItem -Path $sourceRoot -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if (-not $sourceFiles) {
    throw "No Java source files found under: $sourceRoot"
}

if (Test-Path $classesDir) {
    Remove-Item -Recurse -Force $classesDir
}
New-Item -ItemType Directory -Path $classesDir | Out-Null

& $javac -encoding UTF-8 -cp $classpath -d $classesDir $sourceFiles
if ($LASTEXITCODE -ne 0) {
    throw "Compilation failed."
}

$env:TA_DATA_DIR = $dataDir
$env:CATALINA_HOME = $TomcatPath
$env:CATALINA_BASE = $TomcatPath

try {
    & $shutdownScript | Out-Null
    Start-Sleep -Seconds 4
} catch {
    # Ignore shutdown failures when Tomcat is not already running.
}

Write-Host "Compiled classes to: $classesDir"

if (Test-Path $targetApp) {
    Remove-Item -Recurse -Force $targetApp
}
Copy-Item -Path (Join-Path $projectRoot "web") -Destination $targetApp -Recurse -Force

Write-Host "Deployed app to: $targetApp"
Write-Host "Using data dir: $dataDir"

if (-not $SkipStart) {
    Start-Process -FilePath $startupScript -WorkingDirectory $tomcatBin
    Write-Host "Tomcat start command sent."
}

Write-Host "Open: http://localhost:8080/$AppName/"
