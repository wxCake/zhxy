package com.example.eye_manage_1.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eye_manage_1.mapper.GradeMapper;
import com.example.eye_manage_1.pojo.Grade;
import com.example.eye_manage_1.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        queryWrapper.orderByDesc("id");
       Page page1 = baseMapper.selectPage(page,queryWrapper);

        return page1;

    }

    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);//null代表没有查询条件
    }
}
