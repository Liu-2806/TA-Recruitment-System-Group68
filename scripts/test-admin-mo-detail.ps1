param(
    [string]$JavaHome = "",
    [string]$WorkDirName = ".admin-mo-detail-test",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$workRoot = Join-Path $projectRoot $WorkDirName
$defaultDataDir = Join-Path $workRoot "data"
$classesDir = Join-Path $workRoot "classes"
$runnerDir = Join-Path $workRoot "runner"
$servletApiJar = Join-Path $projectRoot "lib\javax.servlet-api-4.0.1.jar"

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

if (-not (Test-Path $servletApiJar)) {
    throw "Servlet API jar not found: $servletApiJar"
}

$javac = Resolve-JavaTool -ToolName "javac" -JavaHomePath $JavaHome
$java = Resolve-JavaTool -ToolName "java" -JavaHomePath $JavaHome

$useIsolatedDataDir = [string]::IsNullOrWhiteSpace($DataDir)
if ($useIsolatedDataDir) {
    $DataDir = $defaultDataDir
}

if (Test-Path $workRoot) {
    Remove-Item -Recurse -Force $workRoot
}

New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $runnerDir | Out-Null
if ($useIsolatedDataDir) {
    Copy-Item -Path (Join-Path $projectRoot "data") -Destination $DataDir -Recurse -Force
} elseif (-not (Test-Path $DataDir)) {
    New-Item -ItemType Directory -Path $DataDir | Out-Null
}

$sourceFiles = @(
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
    (Join-Path $projectRoot "src\com\bupt\ta\util\ServiceRegistry.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\controller\common\BaseServlet.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\controller\admin\AdminMODetailServlet.java")
)

& $javac -encoding UTF-8 -cp $servletApiJar -d $classesDir $sourceFiles
& $javac -encoding UTF-8 -cp "$classesDir;$servletApiJar" -d $runnerDir (Join-Path $projectRoot "scripts\AdminMODetailConsoleRunner.java")

$resolvedDataDir = (Resolve-Path $DataDir).Path
$env:TA_DATA_DIR = $resolvedDataDir

if ($useIsolatedDataDir) {
    Write-Host "Using isolated test data directory: $resolvedDataDir"
} else {
    Write-Host "Using custom data directory: $resolvedDataDir"
}
Write-Host ""

& $java -cp "$classesDir;$runnerDir;$servletApiJar" AdminMODetailConsoleRunner

Write-Host ""
Write-Host "Validated MO detail data under: $resolvedDataDir\users\mo.json"
Write-Host "Validated posting count data under: $resolvedDataDir\postings\postings.json"



