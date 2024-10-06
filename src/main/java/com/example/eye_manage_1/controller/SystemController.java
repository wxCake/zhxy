package com.example.eye_manage_1.controller;

import com.example.eye_manage_1.pojo.Admin;
import com.example.eye_manage_1.pojo.LoginForm;
import com.example.eye_manage_1.pojo.Student;
import com.example.eye_manage_1.pojo.Teacher;
import com.example.eye_manage_1.service.AdminService;
import com.example.eye_manage_1.service.StudentService;
import com.example.eye_manage_1.service.TeacherService;
import com.example.eye_manage_1.utils.CreateVerifiCodeImage;
import com.example.eye_manage_1.utils.JwtHelper;
import com.example.eye_manage_1.utils.Result;
import com.example.eye_manage_1.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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
}
