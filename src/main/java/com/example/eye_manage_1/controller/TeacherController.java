package com.example.eye_manage_1.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eye_manage_1.pojo.Teacher;
import com.example.eye_manage_1.service.TeacherService;
import com.example.eye_manage_1.utils.MD5;
import com.example.eye_manage_1.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Teacher teacher
    ){
        Page<Teacher> page =new Page<>(pageNo,pageSize);
        IPage<Teacher> page1 = teacherService.getTeachers(teacher,page);
        return Result.ok(page1);
    }

    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        Integer id = teacher.getId();
        if (null == id || 0 ==id){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
