$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$buildDir = Join-Path $root "build"
$classesDir = Join-Path $buildDir "classes"
$testClassesDir = Join-Path $buildDir "test-classes"
$pdfBoxJar = Join-Path $root "lib/pdfbox-app-3.0.2.jar"
$fontCacheDir = Join-Path $buildDir "pdfbox-font-cache"
$envFile = Join-Path $root ".env.local"

function Import-EnvFile {
    param (
        [string]$Path
    )

    if (-not (Test-Path $Path)) {
        return
    }

    Get-Content $Path | ForEach-Object {
        $line = $_.Trim()
        if (-not $line -or $line.StartsWith("#")) {
            return
        }
        $parts = $line.Split("=", 2)
        if ($parts.Length -ne 2) {
            return
        }
        $name = $parts[0].Trim()
        $value = $parts[1].Trim()
        if (
            ($value.StartsWith('"') -and $value.EndsWith('"')) -or
            ($value.StartsWith("'") -and $value.EndsWith("'"))
        ) {
            $value = $value.Substring(1, $value.Length - 2)
        }
        [System.Environment]::SetEnvironmentVariable($name, $value, "Process")
    }
}

Import-EnvFile -Path $envFile

New-Item -ItemType Directory -Force -Path $classesDir | Out-Null
New-Item -ItemType Directory -Force -Path $testClassesDir | Out-Null
New-Item -ItemType Directory -Force -Path $fontCacheDir | Out-Null

$mainSources = Get-ChildItem -Recurse -Filter *.java -Path (Join-Path $root "src/main/java") | Select-Object -ExpandProperty FullName
$testSources = Get-ChildItem -Recurse -Filter *.java -Path (Join-Path $root "src/test/java") | Select-Object -ExpandProperty FullName

javac -cp $pdfBoxJar -d $classesDir $mainSources
javac -cp "$pdfBoxJar;$classesDir" -d $testClassesDir $testSources

if (-not (Test-Path (Join-Path $root "demo-data/sample_resume.pdf"))) {
    java "-Dpdfbox.fontcache=$fontCacheDir" -cp "$pdfBoxJar;$classesDir;$testClassesDir" edu.qmul.ta.SampleResumePdfGenerator
}
java "-Dpdfbox.fontcache=$fontCacheDir" -cp "$pdfBoxJar;$classesDir" edu.qmul.ta.DemoApplication
