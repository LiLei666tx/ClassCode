package com.sfac.javaSpringBoot.modules.test.controller;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import com.sfac.javaSpringBoot.modules.test.entity.Student;
import com.sfac.javaSpringBoot.modules.test.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 127.0.0.1/api/student/7 ---- get
     * @param studentId
     * @return
     */
    //现在时演示JPA的查询（还没到分页和排序什么的）
    @GetMapping("/student/{studentId}")
    public Student getStudentByStudentId(@PathVariable int studentId){
        return studentService.getStudentByStudentId(studentId);
    }

    /**
     * 127.0.0.1/api/students ---- post
     * {"currentPage":"1","pageSize":"5","keyWord":"Li","orderBy":"studentName","sort":"desc"}
     * @param searchVo
     * @return
     */
    @PostMapping(value = "/students", consumes = "application/json")
    public Page<Student> getStudentsBySearchVo(@RequestBody SearchVo searchVo) {
        return studentService.getStudentsBySearchVo(searchVo);
    }

    /**
     * 127.0.0.1/api/students ---- get
     */
    @GetMapping("/students")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }
}
