package com.example.eye_manage_1.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.service.AdminService;
import com.example.eye_manage_1.utils.MD5;
import com.example.eye_manage_1.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
           @PathVariable("pageNo") Integer pageNo,
           @PathVariable("pageSize") Integer pageSize,
            Admin admin
    ){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin> page1 = adminService.getAllAdmin(admin,page);
        return Result.ok(page1);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        Integer id = admin.getId();
        if (null == id || 0 ==id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();

    }
}
