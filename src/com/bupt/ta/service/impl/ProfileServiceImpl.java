package com.bupt.ta.service.impl;

import com.bupt.ta.repository.file.SystemDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.service.ProfileService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProfileServiceImpl implements ProfileService {
    private final TADataRepository taDataRepository;
    private final SystemDataRepository systemDataRepository;

    public ProfileServiceImpl(TADataRepository taDataRepository, SystemDataRepository systemDataRepository) {
        this.taDataRepository = taDataRepository;
        this.systemDataRepository = systemDataRepository;
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
        }
        taDataRepository.save(ta);
    }

    @Override
    public Map<String, Object> getMOProfile(String moUserId) {
        Map<String, Object> profile = new LinkedHashMap<>();
        profile.put("moId", moUserId);
        profile.put("fullName", "MO User");
        profile.put("email", "mo@example.com");
        profile.put("department", "Software Engineering");
        profile.put("phone", "");
        profile.put("description", "");
        return profile;
    }

    @Override
    public void updateMOProfile(String moUserId, Map<String, Object> params) {
        // Outside the owned module scope.
    }

    @Override
    public List<String> listAllSkillTags() {
        return systemDataRepository.listSkillTags();
    }

    private boolean hasValue(Object value) {
        return value != null && !String.valueOf(value).isBlank();
    }
}
