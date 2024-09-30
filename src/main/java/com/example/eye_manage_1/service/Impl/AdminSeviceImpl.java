package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.AdminMapper;
import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.service.AdminService;
import org.springframework.stereotype.Service;

@Service("adminSeviceImpl")
public class AdminSeviceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
