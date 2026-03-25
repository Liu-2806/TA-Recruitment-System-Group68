import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.ServiceRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 第一阶段后端命令行交互测试入口。
 */
public class Phase1ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = ServiceRegistry.userService();
    private final AuthService authService = ServiceRegistry.authService();
    private User currentUser;

    public static void main(String[] args) {
        new Phase1ConsoleApp().run();
    }

    private void run() {
        System.out.println("TA Recruitment System - Phase 1 Console Test");
        System.out.println("Default admin account: admin / Admin123!");
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Select an option");

            if ("1".equals(choice)) {
                registerTA();
            } else if ("2".equals(choice)) {
                login();
            } else if ("3".equals(choice)) {
                createMO();
            } else if ("4".equals(choice)) {
                logout();
            } else if ("5".equals(choice)) {
                showNotes();
            } else if ("0".equals(choice)) {
                running = false;
            } else {
                System.out.println("Unknown option.");
            }

            System.out.println();
        }

        System.out.println("Bye.");
    }

    private void printMenu() {
        if (currentUser == null) {
            System.out.println("Current user: <not logged in>");
        } else {
            System.out.println("Current user: " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
        }
        System.out.println("1. Register TA");
        System.out.println("2. Login");
        System.out.println("3. Create MO (Admin only)");
        System.out.println("4. Logout");
        System.out.println("5. Show current test notes");
        System.out.println("0. Exit");
    }

    private void registerTA() {
        System.out.println();
        System.out.println("[Register TA]");
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("fullName", readLine("Full name"));
        params.put("studentId", readLine("Student ID"));
        params.put("email", readLine("Email"));
        params.put("majorProgram", readLine("Major / Program"));
        params.put("academicYear", readLine("Academic year (optional)"));
        params.put("password", readLine("Password"));
        params.put("confirmPassword", readLine("Confirm password"));
        params.put("agreeTerms", "true");

        try {
            User user = userService.registerTA(params);
            System.out.println("TA registered successfully.");
            printUser(user);
        } catch (BusinessException ex) {
            System.out.println("Register failed: " + ex.getMessage());
        }
    }

    private void createMO() {
        System.out.println();
        System.out.println("[Create MO]");
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            System.out.println("Only a logged-in admin can create MO accounts.");
            return;
        }

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("fullName", readLine("Full name"));
        params.put("staffId", readLine("Staff ID"));
        params.put("email", readLine("Email"));
        params.put("department", readLine("Department"));
        params.put("phone", readLine("Phone (optional)"));
        params.put("description", readLine("Description (optional)"));
        params.put("initialPassword", readLine("Initial password"));

        try {
            User user = userService.createMO(params);
            System.out.println("MO created successfully.");
            printUser(user);
        } catch (BusinessException ex) {
            System.out.println("Create MO failed: " + ex.getMessage());
        }
    }

    private void login() {
        System.out.println();
        System.out.println("[Login]");
        String roleRaw = readLine("Role (TA / MO / ADMIN)");
        String username = readLine("Email / StudentID / StaffID / Username");
        String password = readLine("Password");

        try {
            Role role = Role.valueOf(roleRaw.trim().toUpperCase());
            User user = authService.authenticate(username, password, role);
            currentUser = user;
            System.out.println("Login success.");
            printUser(user);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid role. Please enter TA, MO or ADMIN.");
        } catch (BusinessException ex) {
            System.out.println("Login failed: " + ex.getMessage());
        }
    }

    private void logout() {
        System.out.println();
        System.out.println("[Logout]");
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        System.out.println("Logged out: " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
        currentUser = null;
    }

    private void showNotes() {
        System.out.println();
        System.out.println("[Notes]");
        System.out.println("- This console app only tests Phase 1 backend logic.");
        System.out.println("- TA and MO records are stored under data/users.");
        System.out.println("- MO cannot be created directly; you must log in as ADMIN first.");
        System.out.println("- Default admin account is seeded automatically on first run.");
        System.out.println("- Login supports email, student ID, staff ID or username when the role matches.");
    }

    private void printUser(User user) {
        System.out.println("User ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Display Name: " + user.getDisplayName());
        System.out.println("Role: " + user.getRole());
    }

    private String readLine(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }
}
