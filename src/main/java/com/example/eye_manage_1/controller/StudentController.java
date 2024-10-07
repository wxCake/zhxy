package com.example.eye_manage_1.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eye_manage_1.pojo.Student;
import com.example.eye_manage_1.service.StudentService;
import com.example.eye_manage_1.utils.MD5;
import com.example.eye_manage_1.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentsByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Student student
    ){
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> page1 = studentService.getStudentsByOpr(page,student);
        return Result.ok(page1);

    }

    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        //判断，若是新增学生，密码则需要进行加密
        Integer id = student.getId();
        if (null == id || 0 ==id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @DeleteMapping("/delStudentById")
    public Result deleteStudent(@RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }



}
