package com.example.eye_manage_1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Student;
import com.example.eye_manage_1.pojo.Teacher;
import com.example.eye_manage_1.service.AdminService;
import com.example.eye_manage_1.service.StudentService;
import com.example.eye_manage_1.service.TeacherService;
import com.example.eye_manage_1.utils.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verificode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本存到session
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verificode);
        //将验证码图片响应到浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //登录
    @PostMapping("login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        String sessionverifiCode =(String) session.getAttribute("verifiCode");
        String verifiCode = loginForm.getVerifiCode();
        if("".equals(sessionverifiCode) || null == sessionverifiCode){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if(!sessionverifiCode.equalsIgnoreCase(verifiCode)){
            return Result.fail().message("验证码错误");
        }
        //从session中移除现有验证码
        session.removeAttribute("verifiCode");
        //分用户类型
        Map<String ,Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin){
                        String token = JwtHelper.createToken(admin.getId().longValue(),1);
                        map.put("token", token);
                    }else{
                        throw new RuntimeException("用户名或者密码有错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e){
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student){
                        String token = JwtHelper.createToken(student.getId().longValue(),2);
                        map.put("token", token);
                    }else{
                        throw new RuntimeException("用户名或者密码有错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e){
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher){
                        String token = JwtHelper.createToken(teacher.getId().longValue(),3);
                        map.put("token", token);
                    }else{
                        throw new RuntimeException("用户名或者密码有错误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e){
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

        }

        return Result.fail().message("查无此人");

    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        //首先判断token有没有过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出用户id和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String,Object> map =new LinkedHashMap();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;

            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
         return Result.ok(map);
    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("需要上传的文件") @RequestPart("multipartFile") MultipartFile multipartFile
    ) throws IOException {
        //重命名图片
        String uuid = UUID.randomUUID().toString().replace("-","");
        String orginName = multipartFile.getOriginalFilename();
        int i = orginName.lastIndexOf(".");
        String newFileName = uuid+orginName.substring(i);

        //保存图片
        String realpath = "E:/eye_manage_1/target/classes/public/upload/"+newFileName;
        multipartFile.transferTo(new File(realpath));

        //响应图片路径
        String path = "upload/".concat(newFileName);
        return Result.ok(path);

    }


    //修改密码
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            //token过期
            return Result.fail().message("token失效");
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",userId.intValue());
                queryWrapper.eq("password", MD5.encrypt(oldPwd));
                Admin admin = adminService.getOne(queryWrapper);
                if (admin !=null){
                    admin.setPassword(MD5.encrypt(newPwd));
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有错误");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password", MD5.encrypt(oldPwd));
                Student student = studentService.getOne(queryWrapper1);
                if (student !=null){
                    student.setPassword(MD5.encrypt(newPwd));
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有错误");
                }
                break;

            case 3:
                QueryWrapper<Teacher> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password", MD5.encrypt(oldPwd));
                Teacher teacher = teacherService.getOne(queryWrapper2);
                if (teacher !=null){
                    teacher.setPassword(MD5.encrypt(newPwd));
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有错误");
                }
                break;
        }
         return Result.ok();
    }
}
