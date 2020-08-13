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

    /**
     * 127.0.0.1/api/studentsTwo?studentName=LiLei1 ---- get
     * @param studentName
     * @return
     */
    //单个属性查询student
    @GetMapping("/studentsTwo")
    List<Student> getStudentsByStudentName(@RequestParam String studentName){
        return studentService.getStudentsByStudentName(studentName);
    }

    /**
     * 127.0.0.1/api/studentsThree?studentName=LiLei2&cardId=2 ---- get
     * @param studentName
     * @return
     */
    //多个属性查询student
    @GetMapping("/studentsThree")
    List<Student> getStudentsByParams(
            @RequestParam String studentName,
            @RequestParam(required = false, defaultValue = "0") Integer cardId){
        return studentService.getStudentsByStudentName(studentName,cardId);
    }

    /**
     * 127.0.0.1/api/studentsFour?studentName=LiLei2&cardId=2 ---- get
     * @param studentName
     * @return
     */
    //多个属性查询student
    @GetMapping("/studentsFour")
    List<Student> getStudentsByParams2(
            @RequestParam String studentName,
            @RequestParam(required = false, defaultValue = "0") Integer cardId){
        return studentService.getStudentsByStudentName2(studentName,cardId);
    }

    /**
     * 127.0.0.1/api/studentsFive?studentName=LiLei2&cardId=2 ---- get
     * @param studentName
     * @return
     */
    //多个属性查询student
    @GetMapping("/studentsFive")
    List<Student> getStudentsByParams3(
            @RequestParam String studentName,
            @RequestParam(required = false, defaultValue = "0") Integer cardId){
        return studentService.getStudentsByStudentName3(studentName,cardId);
    }
}
