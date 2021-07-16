package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dao.StudentDao;
import com.example.studentmanagement.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    //SELECT * FROM student where name LIKE %T%;
    //name with %
    @Select("SELECT * FROM student where name LIKE @{name}")
    List<Student> getStudentsCotainStrInName(@Param("name") String name);
}
