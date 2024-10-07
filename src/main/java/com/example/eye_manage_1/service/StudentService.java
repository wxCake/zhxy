package com.example.eye_manage_1.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Student getStudentById(Long userId);

    IPage<Student> getStudentsByOpr(Page<Student> page, Student student);
}
