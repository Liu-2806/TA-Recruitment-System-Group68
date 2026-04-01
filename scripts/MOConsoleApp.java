import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.ResumeService;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * MO 后端命令行手动测试入口。
 */
public class MOConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final ProfileService profileService = ServiceRegistry.profileService();
    private final JobService jobService = ServiceRegistry.jobService();
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final ResumeService resumeService = ServiceRegistry.resumeService();
    private final AnalyticsService analyticsService = ServiceRegistry.analyticsService();
    private final Path outputDir = Paths.get(System.getProperty("mo.console.output.dir", "output")).toAbsolutePath().normalize();

    private String currentMoId = "MO001";

    public static void main(String[] args) {
        new MOConsoleApp().run();
    }

    private void run() {
        try {
            Files.createDirectories(outputDir);
        } catch (Exception ex) {
            throw new IllegalStateException("初始化下载目录失败: " + outputDir, ex);
        }

        System.out.println("TA 招聘系统 - MO 控制台测试");
        System.out.println("当前数据目录: " + System.getProperty("ta.data.dir", "<默认数据目录>"));
        System.out.println("当前 MO ID: " + currentMoId);
        System.out.println("默认测试岗位/申请: POST001 / APP001");
        System.out.println("下载的简历将保存到: " + outputDir);
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("请选择操作编号");
            try {
                if ("1".equals(choice)) {
                    showProfile();
                } else if ("2".equals(choice)) {
                    updateProfile();
                } else if ("3".equals(choice)) {
                    showDashboard();
                } else if ("4".equals(choice)) {
                    createPosting();
                } else if ("5".equals(choice)) {
                    listMyPostings();
                } else if ("6".equals(choice)) {
                    listApplicants();
                } else if ("7".equals(choice)) {
                    showApplicationDetail();
                } else if ("8".equals(choice)) {
                    reviewApplication();
                } else if ("9".equals(choice)) {
                    downloadResume();
                } else if ("10".equals(choice)) {
                    switchMO();
                } else if ("11".equals(choice)) {
                    showNotes();
                } else if ("0".equals(choice)) {
                    running = false;
                } else {
                    System.out.println("无效选项，请重新输入。");
                }
            } catch (Exception ex) {
                System.out.println("操作失败: " + ex.getMessage());
            }

            System.out.println();
        }

        System.out.println("测试结束。");
    }

    private void printMenu() {
        System.out.println("当前 MO: " + currentMoId);
        System.out.println("1. 查看 MO 资料");
        System.out.println("2. 更新 MO 资料");
        System.out.println("3. 查看 MO 仪表盘概览");
        System.out.println("4. 新增岗位");
        System.out.println("5. 查看我的岗位");
        System.out.println("6. 查看岗位申请列表");
        System.out.println("7. 查看申请详情");
        System.out.println("8. 审核申请（录取 / 拒绝）");
        System.out.println("9. 下载申请人简历");
        System.out.println("10. 切换当前 MO");
        System.out.println("11. 查看测试说明");
        System.out.println("0. 退出");
    }

    private void showProfile() {
        System.out.println();
        System.out.println("[MO 资料]");
        Map<String, Object> profile = profileService.getMOProfile(currentMoId);
        printKeyValue("MO ID", profile.get("moId"));
        printKeyValue("姓名", profile.get("fullName"));
        printKeyValue("工号", profile.get("staffId"));
        printKeyValue("邮箱", profile.get("email"));
        printKeyValue("院系", profile.get("department"));
        printKeyValue("电话", profile.get("phone"));
        printKeyValue("简介", profile.get("description"));
        printKeyValue("更新时间", profile.get("updatedAt"));
    }

    private void updateProfile() {
        System.out.println();
        System.out.println("[更新 MO 资料]");
        Map<String, Object> profile = profileService.getMOProfile(currentMoId);
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("fullName", readLine("姓名", stringValue(profile.get("fullName"))));
        params.put("email", readLine("邮箱", stringValue(profile.get("email"))));
        params.put("department", readLine("院系", stringValue(profile.get("department"))));
        params.put("phone", readLine("电话", stringValue(profile.get("phone"))));
        params.put("description", readLine("简介", stringValue(profile.get("description"))));
        profileService.updateMOProfile(currentMoId, params);
        System.out.println("资料更新成功。");
        showProfile();
    }

    private void showDashboard() {
        System.out.println();
        System.out.println("[MO 仪表盘]");
        Map<String, Object> overview = analyticsService.getMODashboardOverview(currentMoId);
        @SuppressWarnings("unchecked")
        Map<String, Object> profileCard = (Map<String, Object>) overview.get("profileCard");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> alerts = (List<Map<String, Object>>) overview.get("alerts");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> activePostings = (List<Map<String, Object>>) overview.get("activePostings");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> recentActivity = (List<Map<String, Object>>) overview.get("recentActivity");

        printKeyValue("姓名", profileCard.get("fullName"));
        printKeyValue("院系", profileCard.get("department"));
        printKeyValue("待处理申请数", overview.get("awaitingReviewCount"));

        System.out.println("提醒:");
        if (alerts == null || alerts.isEmpty()) {
            System.out.println("- 无");
        } else {
            for (Map<String, Object> alert : alerts) {
                System.out.println("- " + stringValue(alert.get("courseName")) + "，截止日期=" + stringValue(alert.get("deadline")));
            }
        }

        System.out.println("当前开放岗位:");
        if (activePostings == null || activePostings.isEmpty()) {
            System.out.println("- 无");
        } else {
            for (Map<String, Object> posting : activePostings) {
                System.out.println("- " + stringValue(posting.get("postingId")) + " | " + stringValue(posting.get("courseName"))
                    + " | 状态=" + formatStatus(posting.get("status"))
                    + " | 申请数=" + formatValue(posting.get("applicationCount")));
            }
        }

        System.out.println("最近活动:");
        if (recentActivity == null || recentActivity.isEmpty()) {
            System.out.println("- 无");
        } else {
            for (Map<String, Object> activity : recentActivity) {
                System.out.println("- " + stringValue(activity.get("applicationId")) + " | " + stringValue(activity.get("taName"))
                    + " | 岗位=" + stringValue(activity.get("postingTitle"))
                    + " | 状态=" + formatStatus(activity.get("status"))
                    + " | 投递时间=" + stringValue(activity.get("appliedAt")));
            }
        }
    }

    private void createPosting() {
        System.out.println();
        System.out.println("[新增岗位]");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("courseCode", readLine("课程代码"));
        params.put("courseName", readLine("岗位名称"));
        params.put("vacancies", readLine("招聘人数"));
        params.put("deadline", readLine("截止日期（YYYY-MM-DD）"));
        params.put("description", readLine("岗位描述"));
        params.put("requiredSkills", readLine("所需技能（用逗号分隔）"));
        params.put("estimatedWorkloadHours", readLine("预估每周工时"));
        params.put("status", normalizePostingStatusInput(readLine("岗位状态", "开放")));

        Map<String, Object> posting = jobService.createJob(currentMoId, params);
        System.out.println("岗位创建成功。");
        printPosting(posting);
    }

    private void listMyPostings() {
        System.out.println();
        System.out.println("[查看我的岗位]");
        JobQuery query = new JobQuery();
        query.setKeyword(readLine("关键词（可选）"));
        query.setStatus(normalizePostingStatusInput(readLine("岗位状态（可选）")));
        query.setSortBy(normalizeJobSortInput(readLine("排序方式（截止时间升序 / 截止时间降序 / 申请人数升序 / 申请人数降序）", "截止时间降序")));
        query.setPage(parsePositiveInt(readLine("页码", "1"), 1));
        query.setSize(parsePositiveInt(readLine("每页条数", "20"), 20));

        PageResult<Map<String, Object>> page = jobService.listJobsByMO(currentMoId, query);
        System.out.println("总数=" + page.getTotal() + "，页码=" + page.getPage() + "，每页条数=" + page.getSize());
        if (page.getRecords().isEmpty()) {
            System.out.println("暂无岗位。");
            return;
        }
        for (Map<String, Object> posting : page.getRecords()) {
            printPosting(posting);
        }
    }

    private void listApplicants() {
        System.out.println();
        System.out.println("[查看岗位申请列表]");
        String postingId = readLine("岗位 ID", "POST001");
        ensurePostingBelongsToCurrentMO(postingId);

        ApplicationQuery query = new ApplicationQuery();
        query.setStatus(normalizeApplicationStatusInput(readLine("申请状态（可选）", "已提交")));
        query.setSortBy(normalizeApplicationSortInput(readLine("排序方式（匹配分升序 / 匹配分降序 / 时间升序 / 时间降序）", "匹配分降序")));
        query.setPage(parsePositiveInt(readLine("页码", "1"), 1));
        query.setSize(parsePositiveInt(readLine("每页条数", "20"), 20));

        PageResult<Map<String, Object>> page = applicationService.listApplicationsByJob(postingId, query);
        System.out.println("总数=" + page.getTotal() + "，页码=" + page.getPage() + "，每页条数=" + page.getSize());
        if (page.getRecords().isEmpty()) {
            System.out.println("暂无申请记录。");
            return;
        }
        for (Map<String, Object> application : page.getRecords()) {
            System.out.println("- " + stringValue(application.get("applicationId"))
                + " | 申请人=" + stringValue(application.get("taName"))
                + " | 状态=" + formatStatus(application.get("status"))
                + " | 匹配分=" + formatValue(application.get("skillMatchScore"))
                + " | 投递时间=" + stringValue(application.get("appliedAt")));
        }
    }

    private void showApplicationDetail() {
        System.out.println();
        System.out.println("[查看申请详情]");
        String applicationId = readLine("申请 ID", "APP001");
        Map<String, Object> detail = applicationService.getApplicationDetailForMO(applicationId, currentMoId);
        @SuppressWarnings("unchecked")
        Map<String, Object> job = (Map<String, Object>) detail.get("job");
        @SuppressWarnings("unchecked")
        Map<String, Object> taProfile = (Map<String, Object>) detail.get("taProfile");

        printKeyValue("申请 ID", detail.get("applicationId"));
        printKeyValue("状态", formatStatus(detail.get("status")));
        printKeyValue("反馈", detail.get("feedback"));
        printKeyValue("投递时间", detail.get("appliedAt"));
        printKeyValue("匹配分", detail.get("skillMatchScore"));
        printKeyValue("匹配解释", detail.get("skillMatchExplanation"));
        printKeyValue("匹配方法", detail.get("matchMethod"));
        printKeyValue("岗位 ID", job.get("postingId"));
        printKeyValue("岗位名称", job.get("courseName"));
        printKeyValue("TA ID", taProfile.get("taId"));
        printKeyValue("TA 姓名", taProfile.get("fullName"));
        printKeyValue("TA 邮箱", taProfile.get("email"));
        printKeyValue("TA 专业", taProfile.get("majorProgram"));
        printKeyValue("已匹配技能", detail.get("matchedSkills"));
        printKeyValue("缺失技能", detail.get("missingSkills"));
    }

    private void reviewApplication() {
        System.out.println();
        System.out.println("[审核申请]");
        String applicationId = readLine("申请 ID", "APP001");
        String decision = readLine("审核结果（录取 / 拒绝）");
        String feedback = readLine("反馈");
        ApplicationStatus status = normalizeDecisionInput(decision);
        if (status == ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("这里只允许录取或拒绝。");
        }
        applicationService.updateStatusByMO(applicationId, currentMoId, status, feedback);
        System.out.println("申请状态更新成功。");
        showApplicationDetailById(applicationId);
    }

    private void downloadResume() {
        System.out.println();
        System.out.println("[下载申请人简历]");
        String applicationId = readLine("申请 ID", "APP001");
        Map<String, Object> payload = resumeService.openResumeStreamForMO(applicationId, currentMoId);
        Path target = outputDir.resolve(applicationId + "_" + stringValue(payload.get("fileName")));
        try (InputStream inputStream = (InputStream) payload.get("stream")) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw new IllegalStateException("保存下载的简历失败。", ex);
        }
        System.out.println("简历已保存到: " + target);
    }

    private void switchMO() {
        System.out.println();
        System.out.println("[切换当前 MO]");
        String newMoId = readLine("MO ID", currentMoId);
        Map<String, Object> profile = profileService.getMOProfile(newMoId);
        currentMoId = stringValue(profile.get("moId"));
        System.out.println("已切换到 MO: " + currentMoId + "（" + stringValue(profile.get("fullName")) + "）");
    }

    private void showNotes() {
        System.out.println();
        System.out.println("[测试说明]");
        System.out.println("- 这个控制台程序只测试 MO 后端逻辑。");
        System.out.println("- 默认测试 MO 是 MO001。");
        System.out.println("- 默认测试岗位/申请是 POST001 和 APP001。");
        System.out.println("- 重新运行 PowerShell 脚本会重置隔离测试数据。");
        System.out.println("- 申请状态值仍保持仓库当前真实值：SUBMITTED、ACCEPTED、REJECTED。");
    }

    private void showApplicationDetailById(String applicationId) {
        Map<String, Object> detail = applicationService.getApplicationDetailForMO(applicationId, currentMoId);
        printKeyValue("申请 ID", detail.get("applicationId"));
        printKeyValue("状态", formatStatus(detail.get("status")));
        printKeyValue("反馈", detail.get("feedback"));
    }

    private void ensurePostingBelongsToCurrentMO(String postingId) {
        Map<String, Object> posting = jobService.getJobById(postingId);
        String ownerMoId = stringValue(posting.get("moId"));
        if (!currentMoId.equals(ownerMoId)) {
            throw new IllegalStateException("岗位 " + postingId + " 属于 " + ownerMoId + "，不属于当前 MO " + currentMoId + "。");
        }
    }

    private void printPosting(Map<String, Object> posting) {
        System.out.println("- " + stringValue(posting.get("postingId"))
            + " | " + stringValue(posting.get("courseCode"))
            + " | " + stringValue(posting.get("courseName"))
            + " | 状态=" + formatStatus(posting.get("status"))
            + " | 招聘人数=" + formatValue(posting.get("vacancies"))
            + " | 申请数=" + formatValue(posting.get("applicationCount"))
            + " | 截止日期=" + stringValue(posting.get("deadline")));
    }

    private void printKeyValue(String key, Object value) {
        System.out.println(key + ": " + formatValue(value));
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(rawValue.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private String readLine(String label) {
        System.out.print(label + ": ");
        if (!scanner.hasNextLine()) {
            System.out.println();
            return "0";
        }
        return normalizeInput(scanner.nextLine());
    }

    private String readLine(String label, String defaultValue) {
        String suffix = defaultValue == null || defaultValue.isBlank() ? "" : " [" + defaultValue + "]";
        System.out.print(label + suffix + ": ");
        if (!scanner.hasNextLine()) {
            System.out.println();
            return defaultValue;
        }
        String input = normalizeInput(scanner.nextLine());
        return input.isEmpty() ? defaultValue : input;
    }

    private String normalizeInput(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace("\uFEFF", "").trim();
    }

    private String normalizePostingStatusInput(String raw) {
        String normalized = normalizeInput(raw);
        if (normalized.isEmpty()) {
            return "";
        }
        String upper = normalized.toUpperCase(Locale.ROOT);
        if ("OPEN".equals(upper) || "开放".equals(normalized)) {
            return "OPEN";
        }
        if ("CLOSED".equals(upper) || "关闭".equals(normalized) || "已关闭".equals(normalized)) {
            return "CLOSED";
        }
        if ("DRAFT".equals(upper) || "草稿".equals(normalized)) {
            return "DRAFT";
        }
        return upper;
    }

    private String normalizeApplicationStatusInput(String raw) {
        String normalized = normalizeInput(raw);
        if (normalized.isEmpty()) {
            return "";
        }
        String upper = normalized.toUpperCase(Locale.ROOT);
        if ("SUBMITTED".equals(upper) || "已提交".equals(normalized)) {
            return "SUBMITTED";
        }
        if ("ACCEPTED".equals(upper) || "录取".equals(normalized) || "已录取".equals(normalized)) {
            return "ACCEPTED";
        }
        if ("REJECTED".equals(upper) || "拒绝".equals(normalized) || "已拒绝".equals(normalized)) {
            return "REJECTED";
        }
        return upper;
    }

    private String normalizeJobSortInput(String raw) {
        String normalized = normalizeInput(raw);
        if (normalized.isEmpty()) {
            return "deadlineDesc";
        }
        if ("截止时间升序".equals(normalized) || "deadlineAsc".equalsIgnoreCase(normalized)) {
            return "deadlineAsc";
        }
        if ("截止时间降序".equals(normalized) || "deadlineDesc".equalsIgnoreCase(normalized)) {
            return "deadlineDesc";
        }
        if ("申请人数升序".equals(normalized) || "applicationsAsc".equalsIgnoreCase(normalized)) {
            return "applicationsAsc";
        }
        if ("申请人数降序".equals(normalized) || "applicationsDesc".equalsIgnoreCase(normalized)) {
            return "applicationsDesc";
        }
        return normalized;
    }

    private String normalizeApplicationSortInput(String raw) {
        String normalized = normalizeInput(raw);
        if (normalized.isEmpty()) {
            return "scoreDesc";
        }
        if ("匹配分升序".equals(normalized) || "scoreAsc".equalsIgnoreCase(normalized)) {
            return "scoreAsc";
        }
        if ("匹配分降序".equals(normalized) || "scoreDesc".equalsIgnoreCase(normalized)) {
            return "scoreDesc";
        }
        if ("时间升序".equals(normalized) || "timeAsc".equalsIgnoreCase(normalized)) {
            return "timeAsc";
        }
        if ("时间降序".equals(normalized) || "timeDesc".equalsIgnoreCase(normalized)) {
            return "timeDesc";
        }
        return normalized;
    }

    private ApplicationStatus normalizeDecisionInput(String raw) {
        String normalized = normalizeApplicationStatusInput(raw);
        return ApplicationStatus.valueOf(normalized.toUpperCase(Locale.ROOT));
    }

    private String formatStatus(Object value) {
        String status = stringValue(value).trim().toUpperCase(Locale.ROOT);
        if ("OPEN".equals(status)) {
            return "OPEN（开放）";
        }
        if ("CLOSED".equals(status)) {
            return "CLOSED（关闭）";
        }
        if ("DRAFT".equals(status)) {
            return "DRAFT（草稿）";
        }
        if ("SUBMITTED".equals(status)) {
            return "SUBMITTED（已提交）";
        }
        if ("ACCEPTED".equals(status)) {
            return "ACCEPTED（已录取）";
        }
        if ("REJECTED".equals(status)) {
            return "REJECTED（已拒绝）";
        }
        return stringValue(value);
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue()).stripTrailingZeros().toPlainString();
        }
        return String.valueOf(value);
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }
}
