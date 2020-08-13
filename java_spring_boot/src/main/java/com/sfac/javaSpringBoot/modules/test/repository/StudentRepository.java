package com.sfac.javaSpringBoot.modules.test.repository;

import com.sfac.javaSpringBoot.modules.test.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    //精确查找
    List<Student> findByStudentName(String studentName);

    //模糊查询
    List<Student> findByStudentNameLike(String studentName);

    //模糊查询，只取前面的两条数据出来
    List<Student> findTop2ByStudentNameLike(String studentName);

    //自定义 HQL 查询（根据学生的姓名和schoolId来查找学生信息）
    @Query(value = "select s from Student s where s.studentName = ?1 and s.studentCard.cardId = ?2")
    List<Student> getStudentsByParams(String studentName, int cardId);

    //自定义 JPQL 查询（根据学生的姓名和schoolId来查找学生信息）
    @Query(value = "select s from Student s where s.studentName = :studentName " +
            "and s.studentCard.cardId = :cardId")
    List<Student> getStudentsByParams2(@Param("studentName") String studentName,
                                      @Param("cardId") int cardId);

    //自定义 原生SQL传参 查询（根据学生的姓名和schoolId来查找学生信息）
    @Query(nativeQuery = true,
            value = "select * from h_student where student_name = :studentName " +
            "and card_id = :cardId")
    List<Student> getStudentsByParams3(@Param("studentName") String studentName,
                                       @Param("cardId") int cardId);
}
