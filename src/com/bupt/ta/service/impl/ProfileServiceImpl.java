package com.bupt.ta.service.impl;

import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.SystemDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.service.ProfileService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProfileServiceImpl implements ProfileService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TADataRepository taDataRepository;
    private final SystemDataRepository systemDataRepository;
    private final UserRepository userRepository;

    public ProfileServiceImpl(
        TADataRepository taDataRepository,
        SystemDataRepository systemDataRepository,
        UserRepository userRepository
    ) {
        this.taDataRepository = taDataRepository;
        this.systemDataRepository = systemDataRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getTAProfile(String taUserId) {
        Map<String, Object> ta = taDataRepository.findByTaId(taUserId);
        if (ta == null) {
            throw new IllegalStateException("TA profile not found: " + taUserId);
        }
        return ta;
    }

    @Override
    public void updateTAProfile(String taUserId, Map<String, Object> params) {
        Map<String, Object> ta = getTAProfile(taUserId);
        if (hasValue(params.get("name"))) {
            ta.put("fullName", params.get("name"));
        }
        if (hasValue(params.get("email"))) {
            ta.put("email", params.get("email"));
        }
        if (hasValue(params.get("phone"))) {
            ta.put("phone", params.get("phone"));
        }
        if (hasValue(params.get("major"))) {
            ta.put("majorProgram", params.get("major"));
        }
        if (hasValue(params.get("grade"))) {
            ta.put("academicYear", params.get("grade"));
        }
        if (hasValue(params.get("intro"))) {
            ta.put("intro", params.get("intro"));
        }
        Object skillTags = params.get("skillTags");
        if (skillTags instanceof String[] tags) {
            ta.put("skills", List.of(tags));
        } else if (skillTags == null) {
            ta.put("skills", List.of());
        }
        taDataRepository.save(ta);
    }

    @Override
    public Map<String, Object> getMOProfile(String moUserId) {
        Map<String, Object> mo = userRepository.findById(Role.MO, moUserId);
        if (mo == null) {
            throw new IllegalStateException("MO profile not found: " + moUserId);
        }
        return sanitizeMOProfile(mo);
    }

    @Override
    public void updateMOProfile(String moUserId, Map<String, Object> params) {
        Map<String, Object> mo = userRepository.findById(Role.MO, moUserId);
        if (mo == null) {
            throw new IllegalStateException("MO profile not found: " + moUserId);
        }

        String fullName = firstNonBlank(params, "fullName", "name");
        if (fullName != null) {
            if (fullName.isBlank()) {
                throw new IllegalStateException("Full name cannot be empty.");
            }
            mo.put("fullName", fullName.trim());
            mo.put("displayName", fullName.trim());
        }

        if (params != null && params.get("email") != null) {
            String email = String.valueOf(params.get("email")).trim();
            if (email.isEmpty()) {
                throw new IllegalStateException("Email cannot be empty.");
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalStateException("Email format is invalid.");
            }
            mo.put("email", email.toLowerCase());
        }

        applyTextUpdate(mo, "department", params == null ? null : params.get("department"));
        applyTextUpdate(mo, "phone", params == null ? null : params.get("phone"));
        applyTextUpdate(mo, "description", params == null ? null : params.get("description"));
        mo.put("updatedAt", LocalDateTime.now().format(FORMATTER));
        userRepository.update(Role.MO, mo);
    }

    @Override
    public List<String> listAllSkillTags() {
        return systemDataRepository.listSkillTags();
    }

    private boolean hasValue(Object value) {
        return value != null && !String.valueOf(value).isBlank();
    }

    private void applyTextUpdate(Map<String, Object> target, String field, Object rawValue) {
        if (rawValue == null) {
            return;
        }
        target.put(field, String.valueOf(rawValue).trim());
    }

    private String firstNonBlank(Map<String, Object> values, String... keys) {
        if (values == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            if (!values.containsKey(key)) {
                continue;
            }
            Object value = values.get(key);
            if (value == null) {
                continue;
            }
            return String.valueOf(value).trim();
        }
        return null;
    }

    private Map<String, Object> sanitizeMOProfile(Map<String, Object> record) {
        Map<String, Object> profile = new LinkedHashMap<>(record);
        profile.remove("password");
        profile.remove("passwordSalt");
        profile.remove("passwordHash");
        return profile;
    }
}
