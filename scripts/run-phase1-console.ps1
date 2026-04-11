param(
    [string]$JavaHome = "E:\jdk-21.0.2",
    [string]$WorkDirName = ".phase1-console",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$workRoot = Join-Path $projectRoot $WorkDirName
$classesDir = Join-Path $workRoot "classes"
$runnerDir = Join-Path $workRoot "runner"
$javac = Join-Path $JavaHome "bin\javac.exe"
$java = Join-Path $JavaHome "bin\java.exe"

if (-not (Test-Path $javac)) {
    throw "javac not found: $javac"
}

if (-not (Test-Path $java)) {
    throw "java not found: $java"
}

if ([string]::IsNullOrWhiteSpace($DataDir)) {
    $DataDir = Join-Path $projectRoot "data"
}

if (-not (Test-Path $workRoot)) {
    New-Item -ItemType Directory -Path $workRoot | Out-Null
}

if (Test-Path $classesDir) {
    Remove-Item -Recurse -Force $classesDir
}

if (Test-Path $runnerDir) {
    Remove-Item -Recurse -Force $runnerDir
}

New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $runnerDir | Out-Null
if (-not (Test-Path $DataDir)) {
    New-Item -ItemType Directory -Path $DataDir | Out-Null
}

$sourceFiles = @(
    (Join-Path $projectRoot "src\com\bupt\ta\model\ApplicationStatus.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\model\Role.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\model\User.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\dto\PageResult.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\exception\BusinessException.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\exception\UnauthorizedException.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\AuthService.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\UserService.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\AnalyticsService.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\UserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\file\JsonUserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\impl\AuthServiceImpl.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\impl\UserServiceImpl.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\impl\AnalyticsServiceImpl.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\SessionKeys.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\DataPaths.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\JsonUtils.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\PasswordUtils.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\RoleUtils.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\ServiceRegistry.java")
)

& $javac -encoding UTF-8 -d $classesDir $sourceFiles
& $javac -encoding UTF-8 -cp $classesDir -d $runnerDir (Join-Path $projectRoot "scripts\Phase1ConsoleApp.java")

$env:TA_DATA_DIR = (Resolve-Path $DataDir).Path

Write-Host "Using data directory: $env:TA_DATA_DIR"
Write-Host ""

& $java -cp "$classesDir;$runnerDir" Phase1ConsoleApp
