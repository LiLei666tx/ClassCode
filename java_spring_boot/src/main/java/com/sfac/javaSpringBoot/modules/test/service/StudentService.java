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

    //根据单个属性studentName来模糊查询Student
    List<Student> getStudentsByStudentName(String studentName);

    //根据多个属性studentName和cardId来模糊查询Student(HQL)
    List<Student> getStudentsByStudentName(String studentName,int cardId);

    //根据多个属性studentName和cardId来模糊查询Student(JPQL)
    List<Student> getStudentsByStudentName2(String studentName,int cardId);

    //根据多个属性studentName和cardId来模糊查询Student(原生SQL)
    List<Student> getStudentsByStudentName3(String studentName,int cardId);
}
