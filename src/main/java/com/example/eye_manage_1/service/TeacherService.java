package com.example.eye_manage_1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);
}
