package com.sfac.javaSpringBoot.modules.test.controller;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.test.entity.Student;
import com.sfac.javaSpringBoot.modules.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 127.0.0.1/api/student ----post
     * {"studentName":"LiLei","studentCard":{"cardId":"1"}}
     * @param student
     * @return
     */
    @PostMapping(value = "student", consumes = "application/json")
    public Result<Student> insertStudent(@RequestBody Student student){
        return studentService.insertStudent(student);
    }
}
