package com.example.studentmanagement.dao;

import com.example.studentmanagement.model.Student;
import org.springframework.data.repository.CrudRepository;

//CrudRepository includes add, insert, update, del... for database
//with input of Object, Primary Key
public interface StudentDao extends CrudRepository<Student,Long> {
}
