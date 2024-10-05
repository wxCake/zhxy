package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.TeacherMapper;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Teacher;
import com.example.eye_manage_1.service.TeacherService;
import com.example.eye_manage_1.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Teacher teacher= baseMapper.selectOne(queryWrapper);
        return teacher;
    }
}
