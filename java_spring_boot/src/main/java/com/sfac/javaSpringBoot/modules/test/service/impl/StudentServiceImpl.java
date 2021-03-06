package com.sfac.javaSpringBoot.modules.test.service.impl;

import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import com.sfac.javaSpringBoot.modules.test.entity.Student;
import com.sfac.javaSpringBoot.modules.test.repository.StudentRepository;
import com.sfac.javaSpringBoot.modules.test.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public Result<Student> insertStudent(Student student) {
        student.setCreateDate(LocalDateTime.now());
        studentRepository.saveAndFlush(student);
        return new Result<Student>(Result.ResultStatus.SUCCESS.status,
                "InsertStudent Success!!", student);
    }

    //JPA（通过学生Id来查找学生）（演示查询）
    @Override
    public Student getStudentByStudentId(int studentId) {
        return studentRepository.findById(studentId).get();
    }

    //分页查询（演示jpa的分页查询）
    @Override
    public Page<Student> getStudentsBySearchVo(SearchVo searchVo) {
        //确定排序的方向性
        Sort.Direction direction = "desc".equalsIgnoreCase(searchVo.getSort()) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        //new一个Sort对象，然后把direction放进去；
        //然后判断orderBy的根据是否为空，
        // 若是，则根据学生Id来排序，反之，则用orderBy自带的来排序
        Sort sort = new Sort(direction, StringUtils.isBlank(searchVo.getOrderBy()) ?
                "studentId": searchVo.getOrderBy());

        //Pageable（jpa自带的分页对象）注意：他的起始页是从0开始的
        Pageable pageable = PageRequest.of(
                searchVo.getCurrentPage() -1,searchVo.getPageSize(),sort);

        Student student = new Student();
        //设置值就是下面的studentName获取到的关键模糊值
        student.setStudentName(searchVo.getKeyWord());
        //忽略掉StudentId
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("studentName",match -> match.contains())
                //忽略掉StudentId这个字段
                        .withIgnorePaths("studentId");
        //实例化example；matcher（example的匹配规则，对哪些字段进行匹配）
        Example<Student> example = Example.of(student, matcher);
        return studentRepository.findAll(example, pageable);
    }

    @Override
    public List<Student> getStudents() {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = new Sort(direction, "studentName");
        return studentRepository.findAll(sort);
    }

    //不用谢service层了，直接在service的实现类调用不同的方法就好了
    @Override
    public List<Student> getStudentsByStudentName(String studentName) {
//        return Optional.ofNullable(studentRepository
//                .findByStudentName(studentName))
//                .orElse(Collections.emptyList());
//        return Optional.ofNullable(studentRepository
//                .findByStudentNameLike(
//                        String.format("%s%s%s","%",studentName,"%")
//                ))
//                .orElse(Collections.emptyList());
        return Optional.ofNullable(studentRepository
                .findTop2ByStudentNameLike(
                        String.format("%s%s%s","%",studentName,"%")
                ))
                .orElse(Collections.emptyList());
    }

    //根据多个属性studentName和cardId来模糊查询Student(HQL)
    @Override
    public List<Student> getStudentsByStudentName(String studentName,int cardId) {

        if(cardId > 0){
            return studentRepository.getStudentsByParams(studentName,cardId);
        }else{
//          return Optional.ofNullable(studentRepository
//                .findByStudentName(studentName))
//                .orElse(Collections.emptyList());
//          return Optional.ofNullable(studentRepository
//                .findByStudentNameLike(
//                        String.format("%s%s%s","%",studentName,"%")
//                ))
//                .orElse(Collections.emptyList());
            return Optional.ofNullable(studentRepository
                    .findTop2ByStudentNameLike(
                            String.format("%s%s%s","%",studentName,"%")
                    ))
                    .orElse(Collections.emptyList());
        }
    }

    //根据多个属性studentName和cardId来模糊查询Student(JPQL)
    @Override
    public List<Student> getStudentsByStudentName2(String studentName, int cardId) {
        if(cardId > 0){
            return studentRepository.getStudentsByParams2(studentName,cardId);
        }else{
//          return Optional.ofNullable(studentRepository
//                .findByStudentName(studentName))
//                .orElse(Collections.emptyList());
//          return Optional.ofNullable(studentRepository
//                .findByStudentNameLike(
//                        String.format("%s%s%s","%",studentName,"%")
//                ))
//                .orElse(Collections.emptyList());
            return Optional.ofNullable(studentRepository
                    .findTop2ByStudentNameLike(
                            String.format("%s%s%s","%",studentName,"%")
                    ))
                    .orElse(Collections.emptyList());
        }
    }

    //根据多个属性studentName和cardId来模糊查询Student(原生SQL)
    @Override
    public List<Student> getStudentsByStudentName3(String studentName, int cardId) {
        if(cardId > 0){
            return studentRepository.getStudentsByParams3(studentName,cardId);
        }else{
//          return Optional.ofNullable(studentRepository
//                .findByStudentName(studentName))
//                .orElse(Collections.emptyList());
//          return Optional.ofNullable(studentRepository
//                .findByStudentNameLike(
//                        String.format("%s%s%s","%",studentName,"%")
//                ))
//                .orElse(Collections.emptyList());
            return Optional.ofNullable(studentRepository
                    .findTop2ByStudentNameLike(
                            String.format("%s%s%s","%",studentName,"%")
                    ))
                    .orElse(Collections.emptyList());
        }
    }
}
