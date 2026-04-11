param(
    [string]$TomcatPath = "",
    [string]$AppName = "TA-Recruitment-System-Group68"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
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
$sourceWebapp = Join-Path $projectRoot "src\main\webapp"
$targetWebapps = Join-Path $TomcatPath "webapps"
$targetApp = Join-Path $targetWebapps $AppName

$compileClasspath = @(
    (Join-Path $projectRoot "lib\pdfbox-app-3.0.2.jar"),
    (Join-Path $projectRoot "lib\gson-2.11.0.jar"),
    (Join-Path $projectRoot "lib\javax.servlet-api-4.0.1.jar")
) -join ";"

if (-not (Test-Path $TomcatPath)) {
    throw "Tomcat path not found: $TomcatPath"
}

if (-not (Test-Path $sourceWebRoot)) {
    throw "Web root not found: $sourceWebRoot"
}

if (-not (Test-Path $sourceJavaRoot)) {
    throw "Java source root not found: $sourceJavaRoot"
}

if (-not (Test-Path $targetWebapps)) {
    throw "Tomcat webapps directory not found: $targetWebapps"
}

foreach ($jarPath in $compileClasspath.Split(";")) {
    if (-not (Test-Path $jarPath)) {
        throw "Required compile dependency not found: $jarPath"
    }
}

if (Test-Path $stagingApp) {
    Remove-Item -LiteralPath $stagingApp -Recurse -Force
}
New-Item -ItemType Directory -Path $stagingClasses -Force | Out-Null

Copy-Item -Path (Join-Path $sourceWebRoot "*") -Destination $stagingApp -Recurse -Force

$sources = Get-ChildItem -Path $sourceJavaRoot -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
if (-not $sources) {
    throw "No Java sources found under: $sourceJavaRoot"
}

javac -encoding UTF-8 -cp $compileClasspath -d $stagingClasses $sources
if ($LASTEXITCODE -ne 0) {
    throw "Java compilation failed while preparing Tomcat deployment."
}

if (Test-Path $targetApp) {
    Remove-Item -LiteralPath $targetApp -Recurse -Force
}
Copy-Item -Path $stagingApp -Destination $targetApp -Recurse -Force

Write-Host "Deployed webapp to: $targetApp"
Write-Host "Application URL: http://localhost:8080/$AppName/"
Write-Host "TA demo login: http://localhost:8080/$AppName/dev/login-as?role=TA"
Write-Host "MO demo login: http://localhost:8080/$AppName/dev/login-as?role=MO"
