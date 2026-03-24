package com.bupt.ta.service;

import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;

/**
 * 认证服务接口。
 */
public interface AuthService {

    /**
     * 用户认证。
     *
     * @param username     用户名
     * @param rawPassword  明文密码（由实现层进行 BCrypt 校验）
     * @param expectedRole 期望登录角色
     * @return 登录成功后的用户对象
     */
    User authenticate(String username, String rawPassword, Role expectedRole);
}
