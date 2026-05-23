import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class AuthAndProfileTest implements TestSupport.AcceptanceTest {
    @Override
    public void run(TestSupport ctx) {
        User ta = ctx.authService.authenticate("syy", "1234567890syy", Role.TA);
        TestSupport.assertEquals(TestSupport.TA_ID, ta.getId(), "TA login should resolve the demo TA account.");
        TestSupport.assertEquals(Role.TA, ta.getRole(), "TA login should return TA role.");

        User mo = ctx.authService.authenticate("mo_demo", "Temp123!", Role.MO);
        TestSupport.assertEquals(TestSupport.MO_ID, mo.getId(), "MO login should resolve the demo MO account.");
        TestSupport.assertEquals(Role.MO, mo.getRole(), "MO login should return MO role.");

        User admin = ctx.authService.authenticate("admin", "Admin123!", Role.ADMIN);
        TestSupport.assertEquals(TestSupport.ADMIN_ID, admin.getId(), "Admin login should resolve the demo admin account.");
        TestSupport.assertEquals(Role.ADMIN, admin.getRole(), "Admin login should return ADMIN role.");

        TestSupport.assertThrows(
            () -> ctx.authService.authenticate("syy", "wrong-password", Role.TA),
            "Wrong TA password should be rejected."
        );

        Map<String, Object> profile = ctx.profileService.getTAProfile(TestSupport.TA_ID);
        TestSupport.assertNonBlank(profile.get("fullName"), "TA profile should expose fullName.");
        TestSupport.assertNonBlank(profile.get("studentId"), "TA profile should expose studentId.");
        TestSupport.assertNonBlank(profile.get("resumeFileName"), "TA profile should expose stored resume metadata.");

        Map<String, Object> update = new LinkedHashMap<>();
        update.put("phone", "+44 7700 900999");
        update.put("intro", "Acceptance testing confirms that TA profile edits are persisted.");
        update.put("skillTags", Arrays.asList("Java", "Git", "Testing", "Communication"));
        ctx.profileService.updateTAProfile(TestSupport.TA_ID, update);

        Map<String, Object> refreshed = ctx.profileService.getTAProfile(TestSupport.TA_ID);
        TestSupport.assertEquals("+44 7700 900999", refreshed.get("phone"), "TA phone update should persist.");
        TestSupport.assertEquals(update.get("intro"), refreshed.get("intro"), "TA intro update should persist.");
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) refreshed.get("skills");
        TestSupport.assertContains(skills, "Git", "TA skill selection should persist allowed tags.");
    }
}
