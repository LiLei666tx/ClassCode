package com.sfac.javaSpringBoot.modules.test.service;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.test.entity.Student;

public interface StudentService {

    Result<Student> insertStudent(Student student);
}
