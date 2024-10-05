package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.ClassMapper;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("classServiceImpl")
@Transactional
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
}
