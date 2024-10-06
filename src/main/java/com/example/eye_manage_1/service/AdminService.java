package com.example.eye_manage_1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.pojo.LoginForm;


public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);
}
