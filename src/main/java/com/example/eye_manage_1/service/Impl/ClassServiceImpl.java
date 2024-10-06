package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.ClassMapper;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.pojo.Grade;
import com.example.eye_manage_1.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("classServiceImpl")
@Transactional
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Override
    public IPage<Grade> getClassByOpr(Page<Class> page, Class clazz) {
        QueryWrapper queryWrapper = new QueryWrapper();
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }
        String name = clazz.getName();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }

       Page page1= baseMapper.selectPage(page,queryWrapper);
        return page1;
    }
}
