package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dao.StudentDao;
import com.example.studentmanagement.model.Student;
import com.fasterxml.jackson.databind.BeanProperty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    //SELECT * FROM student where name LIKE %T%;
    //name with %
    @Select("SELECT * FROM student where name LIKE #{name}")
    List<Student> getStudentsCotainStrInName(@Param("name") String name);

    // Select * from student where university_class_id IN
    //  (SELECT id FROM university_class where year=2021 AND number=1);
    @Select("Select * from student where university_class_id IN"+
            "(SELECT id FROM university_class where year=#{year} AND number=#{number})")
    List<Student> getStudentsInClass(@Param("year") int year, @Param("number") int number);
}
