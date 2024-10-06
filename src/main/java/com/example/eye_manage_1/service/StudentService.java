package com.example.eye_manage_1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Student getStudentById(Long userId);
}
