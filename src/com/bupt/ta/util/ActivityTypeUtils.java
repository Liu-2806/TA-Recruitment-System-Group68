package com.bupt.ta.util;

import java.util.Locale;

public final class ActivityTypeUtils {
    public static final String LAB_SUPPORT = "lab-support";
    public static final String LAB_ASSESSMENT = "lab-assessment";
    public static final String PROJECT_ASSESSMENT = "project-assessment";
    public static final String INVIGILATION = "invigilation";
    public static final String OTHERS = "others";

    private ActivityTypeUtils() {
    }

    public static String normalize(String rawValue) {
        String normalized = sanitize(rawValue);
        if (normalized.isEmpty()) {
            return LAB_SUPPORT;
        }
        if (normalized.equals("lab") || normalized.equals("labsupport") || normalized.equals("lab-support")) {
            return LAB_SUPPORT;
        }
        if (normalized.equals("labassessment") || normalized.equals("lab-assessment")
            || normalized.equals("practicalassessment") || normalized.equals("practical-assessment")) {
            return LAB_ASSESSMENT;
        }
        if (normalized.equals("projectassessment") || normalized.equals("project-assessment")
            || normalized.equals("checkoff") || normalized.equals("check-off")
            || normalized.equals("projectcheckoff") || normalized.equals("project-checkoff")
            || normalized.equals("acceptance")) {
            return PROJECT_ASSESSMENT;
        }
        if (normalized.equals("exam") || normalized.equals("invigilation")
            || normalized.equals("invigilator") || normalized.equals("monitoring")
            || normalized.contains("监考")) {
            return INVIGILATION;
        }
        if (normalized.equals("other") || normalized.equals("others")) {
            return OTHERS;
        }
        return inferFromText(rawValue);
    }

    public static String inferFromText(String rawText) {
        String normalized = sanitize(rawText);
        if (normalized.isEmpty()) {
            return OTHERS;
        }
        if (normalized.contains("监考") || normalized.contains("exam") || normalized.contains("invig")) {
            return INVIGILATION;
        }
        if (normalized.contains("projectassessment") || normalized.contains("project-assessment")
            || normalized.contains("checkoff") || normalized.contains("check-off")
            || normalized.contains("milestone") || normalized.contains("course-design")
            || normalized.contains("intermediate-assessment") || normalized.contains("project")) {
            return PROJECT_ASSESSMENT;
        }
        if (normalized.contains("labassessment") || normalized.contains("lab-assessment")
            || normalized.contains("practicalassessment") || normalized.contains("practical-assessment")) {
            return LAB_ASSESSMENT;
        }
        if (normalized.contains("labsupport") || normalized.contains("lab-support") || normalized.contains("lab")) {
            return LAB_SUPPORT;
        }
        return OTHERS;
    }

    public static String label(String rawType) {
        return switch (normalize(rawType)) {
            case LAB_SUPPORT -> "Lab Support";
            case LAB_ASSESSMENT -> "Lab Assessment";
            case PROJECT_ASSESSMENT -> "Project Assessment";
            case INVIGILATION -> "Invigilation";
            default -> "Others";
        };
    }

    private static String sanitize(String rawValue) {
        if (rawValue == null) {
            return "";
        }
        return rawValue.trim()
            .toLowerCase(Locale.ROOT)
            .replace('_', '-')
            .replaceAll("\\s+", "-");
    }
}
