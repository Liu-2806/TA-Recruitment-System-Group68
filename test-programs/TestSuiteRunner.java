import java.util.ArrayList;
import java.util.List;

public final class TestSuiteRunner {
    private record TestEntry(String name, TestSupport.AcceptanceTest test) {
    }

    public static void main(String[] args) {
        TestSupport.ensureAcceptanceDataRoot();

        List<TestEntry> tests = new ArrayList<>();
        tests.add(new TestEntry("AuthAndProfileTest", new AuthAndProfileTest()));
        tests.add(new TestEntry("TAWorkflowTest", new TAWorkflowTest()));
        tests.add(new TestEntry("MOWorkflowTest", new MOWorkflowTest()));
        tests.add(new TestEntry("AdminWorkflowTest", new AdminWorkflowTest()));
        tests.add(new TestEntry("DataIntegrityTest", new DataIntegrityTest()));

        int passed = 0;
        List<String> failures = new ArrayList<>();
        System.out.println("TA Recruitment System acceptance test programs");
        System.out.println("Data root: " + TestSupport.dataRoot());
        System.out.println();

        for (TestEntry entry : tests) {
            try {
                entry.test().run(new TestSupport());
                passed++;
                System.out.println("[PASS] " + entry.name());
            } catch (Throwable ex) {
                failures.add(entry.name() + ": " + ex.getMessage());
                System.out.println("[FAIL] " + entry.name());
                ex.printStackTrace(System.out);
            }
        }

        System.out.println();
        System.out.println("Result: " + passed + "/" + tests.size() + " test programs passed.");
        if (!failures.isEmpty()) {
            System.out.println("Failures:");
            for (String failure : failures) {
                System.out.println("- " + failure);
            }
            System.exit(1);
        }
    }
}
