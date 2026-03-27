param(
    [string]$JavaHome = "",
    [string]$WorkDirName = ".admin-overview-test",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$workRoot = Join-Path $projectRoot $WorkDirName
$classesDir = Join-Path $workRoot "classes"
$runnerDir = Join-Path $workRoot "runner"

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

$javac = Resolve-JavaTool -ToolName "javac" -JavaHomePath $JavaHome
$java = Resolve-JavaTool -ToolName "java" -JavaHomePath $JavaHome

if ([string]::IsNullOrWhiteSpace($DataDir)) {
    $DataDir = Join-Path $projectRoot "data"
}

if (Test-Path $workRoot) {
    Remove-Item -Recurse -Force $workRoot
}

New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $runnerDir | Out-Null
if (-not (Test-Path $DataDir)) {
    New-Item -ItemType Directory -Path $DataDir | Out-Null
}

$sourceFiles = @(
    (Join-Path $projectRoot "src\com\bupt\ta\model\Role.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\dto\PageResult.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\exception\BusinessException.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\AnalyticsService.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\UserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\file\JsonUserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\impl\AnalyticsServiceImpl.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\DataPaths.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\JsonUtils.java")
)

& $javac -encoding UTF-8 -d $classesDir $sourceFiles
& $javac -encoding UTF-8 -cp $classesDir -d $runnerDir (Join-Path $projectRoot "scripts\AdminOverviewConsoleRunner.java")

$resolvedDataDir = (Resolve-Path $DataDir).Path
$env:TA_DATA_DIR = $resolvedDataDir

Write-Host "Using data directory: $resolvedDataDir"
Write-Host ""

$jsonOutput = & $java -cp "$classesDir;$runnerDir" AdminOverviewConsoleRunner

try {
    $pretty = $jsonOutput | ConvertFrom-Json | ConvertTo-Json -Depth 8
    Write-Output $pretty
} catch {
    Write-Output $jsonOutput
}

