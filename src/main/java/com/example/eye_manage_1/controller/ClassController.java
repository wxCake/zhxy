package com.example.eye_manage_1.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.eye_manage_1.pojo.Class;
import com.example.eye_manage_1.pojo.Grade;
import com.example.eye_manage_1.service.ClassService;
import com.example.eye_manage_1.utils.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping("/getClazzs")
    public Result getClasses(){
        List<Class> classes = classService.getClasses();
        return Result.ok(classes);
    }

    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClassByOpr(
         @ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,
         @ApiParam("分页查询页大小") @PathVariable("pageSize")  Integer pageSize,
         @ApiParam("查询条件") Class clazz
    ){
        Page<Class> page = new Page<>(pageNo,pageSize);
        IPage<Grade> page1 = classService.getClassByOpr(page,clazz);
       return Result.ok(page1);
    }

    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClass(@RequestBody Class clazz){
        classService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @DeleteMapping("/deleteClazz")
    public Result deleteClass(@RequestBody List<Integer> ids){
        classService.removeByIds(ids);
        return Result.ok();

    }

}
