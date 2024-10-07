package com.example.eye_manage_1.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.pojo.Grade;

import java.util.List;

public interface ClassService extends IService<Class> {
    IPage<Grade> getClassByOpr(Page<Class> page, Class clazz);

    List<Class> getClasses();
}
