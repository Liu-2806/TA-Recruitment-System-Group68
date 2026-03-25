param(
    [string]$JavaHome = "E:\jdk-21.0.2",
    [string]$WorkDirName = ".codex-phase1-test"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$testRoot = Join-Path $projectRoot $WorkDirName
$classesDir = Join-Path $testRoot "classes"
$runnerDir = Join-Path $testRoot "runner"
$dataDir = Join-Path $testRoot "data"
$javac = Join-Path $JavaHome "bin\javac.exe"
$java = Join-Path $JavaHome "bin\java.exe"

if (-not (Test-Path $javac)) {
    throw "javac not found: $javac"
}

if (-not (Test-Path $java)) {
    throw "java not found: $java"
}

if (Test-Path $testRoot) {
    Remove-Item -Recurse -Force $testRoot
}

New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $runnerDir | Out-Null
New-Item -ItemType Directory -Path $dataDir | Out-Null

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

$runnerSource = @'
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.ServiceRegistry;
import java.util.LinkedHashMap;
import java.util.Map;

public class Phase1SmokeTest {
    public static void main(String[] args) {
        UserService userService = ServiceRegistry.userService();
        AuthService authService = ServiceRegistry.authService();

        Map<String, Object> taParams = new LinkedHashMap<String, Object>();
        taParams.put("fullName", "Phase1 Test TA");
        taParams.put("studentId", "PHASE1-TA-001");
        taParams.put("email", "phase1.ta@example.com");
        taParams.put("password", "Phase123");
        taParams.put("confirmPassword", "Phase123");
        taParams.put("majorProgram", "Software Engineering");
        taParams.put("academicYear", "Year 3");
        taParams.put("agreeTerms", "true");

        User ta = userService.registerTA(taParams);
        User taLogin = authService.authenticate("phase1.ta@example.com", "Phase123", Role.TA);

        Map<String, Object> moParams = new LinkedHashMap<String, Object>();
        moParams.put("fullName", "Phase1 Test MO");
        moParams.put("staffId", "PHASE1-MO-001");
        moParams.put("email", "phase1.mo@example.com");
        moParams.put("department", "Software Engineering");
        moParams.put("phone", "123456789");
        moParams.put("initialPassword", "Phase456");

        User mo = userService.createMO(moParams);
        User moLogin = authService.authenticate("phase1.mo@example.com", "Phase456", Role.MO);
        User adminLogin = authService.authenticate("admin", "Admin123!", Role.ADMIN);

        System.out.println("TA_REGISTERED=" + ta.getId());
        System.out.println("TA_LOGIN=" + taLogin.getRole());
        System.out.println("MO_CREATED=" + mo.getId());
        System.out.println("MO_LOGIN=" + moLogin.getRole());
        System.out.println("ADMIN_LOGIN=" + adminLogin.getRole());
        System.out.println("SMOKE_TEST=PASS");
    }
}
'@

$runnerFile = Join-Path $runnerDir "Phase1SmokeTest.java"
Set-Content -Path $runnerFile -Value $runnerSource -Encoding Ascii

& $javac -encoding UTF-8 -cp $classesDir -d $runnerDir $runnerFile

$env:TA_DATA_DIR = $dataDir
& $java -cp "$classesDir;$runnerDir" Phase1SmokeTest

Write-Host ""
Write-Host "Test data directory: $dataDir"
Write-Host "Users JSON files should now exist under that directory."
