param(
    [string]$JavaHome = "",
    [string]$WorkDirName = ".admin-all-jobs-test",
    [string]$DataDir = ""
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$workRoot = Join-Path $projectRoot $WorkDirName
$classesDir = Join-Path $workRoot "classes"
$runnerDir = Join-Path $workRoot "runner"
$gsonJar = Join-Path $projectRoot "lib\gson-2.11.0.jar"

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
    (Join-Path $projectRoot "src\com\bupt\ta\dto\JobQuery.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\dto\PageResult.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\exception\BusinessException.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\model\Role.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\UserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\file\JsonFileRepositorySupport.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\file\PostingDataRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\repository\file\JsonUserRepository.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\JobService.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\service\impl\JobServiceImpl.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\DataPaths.java"),
    (Join-Path $projectRoot "src\com\bupt\ta\util\JsonUtils.java")
)

if (-not (Test-Path $gsonJar)) {
    throw "Gson jar not found: $gsonJar"
}

& $javac -encoding UTF-8 -cp $gsonJar -d $classesDir $sourceFiles
if ($LASTEXITCODE -ne 0) {
    throw "Compilation failed for admin jobs source files."
}

& $javac -encoding UTF-8 -cp "$classesDir;$gsonJar" -d $runnerDir (Join-Path $projectRoot "scripts\AdminAllJobsConsoleRunner.java")
if ($LASTEXITCODE -ne 0) {
    throw "Compilation failed for AdminAllJobsConsoleRunner."
}

$resolvedDataDir = (Resolve-Path $DataDir).Path
$env:TA_DATA_DIR = $resolvedDataDir

Write-Host "Using project data directory: $resolvedDataDir"
Write-Host ""

& $java -cp "$classesDir;$runnerDir;$gsonJar" AdminAllJobsConsoleRunner
if ($LASTEXITCODE -ne 0) {
    throw "AdminAllJobsConsoleRunner failed."
}

Write-Host ""
Write-Host "Validated jobs data under: $resolvedDataDir\postings\postings.json"


