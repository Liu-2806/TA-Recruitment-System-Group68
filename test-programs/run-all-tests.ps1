param(
    [switch] $KeepData
)

$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$testRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = [System.IO.Path]::GetFullPath((Join-Path $testRoot ".."))
$dataSource = Join-Path $projectRoot "data"
$dataDir = Join-Path $projectRoot ".acceptance-test-data"
$classesDir = Join-Path $projectRoot "build\test-programs\classes"

function Assert-WorkspaceChild {
    param([string] $PathToCheck)
    $rootFull = [System.IO.Path]::GetFullPath($projectRoot).TrimEnd('\')
    $pathFull = [System.IO.Path]::GetFullPath($PathToCheck)
    if (-not $pathFull.StartsWith($rootFull, [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Refusing to operate outside project root: $pathFull"
    }
}

function Resolve-JavaTool {
    param([string] $ToolName)
    if ($env:JAVA_HOME) {
        $candidate = Join-Path $env:JAVA_HOME "bin\$ToolName.exe"
        if (Test-Path -LiteralPath $candidate) {
            return $candidate
        }
    }
    $command = Get-Command $ToolName -ErrorAction SilentlyContinue
    if ($command) {
        return $command.Source
    }
    throw "$ToolName was not found. Install JDK 21 or set JAVA_HOME."
}

Assert-WorkspaceChild $dataDir
Assert-WorkspaceChild $classesDir

if (-not (Test-Path -LiteralPath $dataSource)) {
    throw "Project data directory not found: $dataSource"
}

if (-not $KeepData) {
    if (Test-Path -LiteralPath $dataDir) {
        Remove-Item -LiteralPath $dataDir -Recurse -Force
    }
}
if (-not (Test-Path -LiteralPath $dataDir)) {
    New-Item -ItemType Directory -Path $dataDir | Out-Null
    Copy-Item -Path (Join-Path $dataSource "*") -Destination $dataDir -Recurse -Force
}

if (Test-Path -LiteralPath $classesDir) {
    Remove-Item -LiteralPath $classesDir -Recurse -Force
}
New-Item -ItemType Directory -Path $classesDir | Out-Null

$javac = Resolve-JavaTool "javac"
$java = Resolve-JavaTool "java"

$classpathItems = @(
    (Join-Path $projectRoot "lib\pdfbox-app-3.0.2.jar"),
    (Join-Path $projectRoot "lib\gson-2.11.0.jar"),
    (Join-Path $projectRoot "lib\javax.servlet-api-4.0.1.jar"),
    (Join-Path $projectRoot "web\WEB-INF\lib\taglibs-standard-spec-1.2.5.jar"),
    (Join-Path $projectRoot "web\WEB-INF\lib\taglibs-standard-impl-1.2.5.jar")
)
foreach ($item in $classpathItems) {
    if (-not (Test-Path -LiteralPath $item)) {
        throw "Required classpath item not found: $item"
    }
}
$classpath = $classpathItems -join ";"

$mainSources = @(Get-ChildItem -LiteralPath (Join-Path $projectRoot "src\com\bupt\ta") -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
$testSources = @(Get-ChildItem -LiteralPath $testRoot -Filter "*.java" | Select-Object -ExpandProperty FullName)
$sources = @($mainSources + $testSources)
if ($sources.Count -eq 0) {
    throw "No Java sources found to compile."
}

Write-Host "Compiling source and acceptance test programs..."
& $javac -encoding UTF-8 -cp $classpath -d $classesDir @sources
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE"
}

$previousApiUrl = $env:LLM_API_URL
$previousApiKey = $env:LLM_API_KEY
$previousModel = $env:LLM_MODEL
try {
    $env:LLM_API_URL = ""
    $env:LLM_API_KEY = ""
    $env:LLM_MODEL = ""
    Push-Location $testRoot
    try {
        Write-Host "Running acceptance test programs against isolated data..."
        & $java "-Dta.data.dir=$dataDir" -cp "$classesDir;$classpath" TestSuiteRunner
        if ($LASTEXITCODE -ne 0) {
            throw "Acceptance test programs failed with exit code $LASTEXITCODE"
        }
    } finally {
        Pop-Location
    }
} finally {
    $env:LLM_API_URL = $previousApiUrl
    $env:LLM_API_KEY = $previousApiKey
    $env:LLM_MODEL = $previousModel
}

Write-Host "All acceptance test programs passed."
