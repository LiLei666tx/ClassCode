package com.sfac.javaSpringBoot.modules.test.service;

import com.github.pagehelper.PageInfo;
import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import com.sfac.javaSpringBoot.modules.test.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    Result<Student> insertStudent(Student student);

    //JPA（通过学生Id来查找学生）
    Student getStudentByStudentId(int studentId);

    Page<Student> getStudentsBySearchVo(SearchVo searchVo);

    List<Student> getStudents();
}
