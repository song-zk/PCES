/*
 * Copyright (c) 2019
 * FileName: StudentService.java
 * @Author: 孙浩杰
 * @LastModified:2019-10-30 21:43:49
 */

package com.ouc.pces.service;

import com.ouc.pces.entity.Student;
import com.ouc.pces.mapper.StudentMapper;
import com.ouc.pces.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    StudentMapper studentMapper;

    /**
     * 学生登录服务
     * @param studentId
     * @param password
     * @return Response
     */
    public Response login(String studentId, String password) {
        Student student = studentMapper.selectByStudentId(studentId);
        if (student == null) //用户id不存在
            return new Response(Response.NotFound, "用户不存在");
        else if (!student.getPassword().equals(password)) //密码错误
            return new Response(Response.Forbidden, "密码错误");
        else if (student.getActivate() != 1) //用户处于未激活/冻结
            return new Response(Response.Forbidden, "户处于未激活/冻结");
        else
            return new Response(Response.OK, "登录成功", student);
    }

    /**
     * 学生注册服务
     * @param student
     * @return Response
     */
    public Response register(Student student){
        //确保student不为null
        if(student == null)
            return new Response(Response.NotFound,"用户信息为空");
        //确保id不重复
        else if(studentMapper.checkStudentIdExist())
            return new Response(Response.Forbidden, "该学号已注册");
        //确保nickname不重复
        else if(studentMapper.checkNickNameExist())
            return new Response(Response.Forbidden, "改昵称已被使用");
        //todo:校验注册数据合法性
        else {
            //todo:studentMapper insert方法
            if(studentMapper.insert(student) == 1)
                return new Response(Response.OK, "注册成功");
            else
                return new Response(Response.FAILED, "注册失败！（可能字段填写不完整或服务器错误）");
        }
    }
}
