import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.service.impl.AnalyticsServiceImpl;
import com.bupt.ta.util.JsonUtils;

/**
 * Admin dashboard overview 命令行测试入口。
 */
public class AdminOverviewConsoleRunner {
    public static void main(String[] args) {
        UserRepository userRepository = new JsonUserRepository();
        AnalyticsService analyticsService = new AnalyticsServiceImpl(userRepository);
        System.out.println(JsonUtils.toJson(analyticsService.getSystemOverview()));
    }
}

