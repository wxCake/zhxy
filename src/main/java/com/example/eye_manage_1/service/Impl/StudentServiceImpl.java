package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.StudentMapper;
import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Student;
import com.example.eye_manage_1.service.StudentService;
import com.example.eye_manage_1.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Student student= baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public IPage<Student> getStudentsByOpr(Page<Student> page, Student student) {
        QueryWrapper queryWrapper = new QueryWrapper();

        String studentName = student.getName();
        if(!StringUtils.isEmpty(studentName)){
            queryWrapper.like("name",studentName);
        }
        String className = student.getClazzName();
        if(!StringUtils.isEmpty(className)){
            queryWrapper.like("clazz_name",className);
        }
        Page<Student> page1 = baseMapper.selectPage(page,queryWrapper);
        return page1;
    }
}
