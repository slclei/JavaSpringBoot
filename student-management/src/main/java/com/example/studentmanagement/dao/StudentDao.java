package com.example.studentmanagement.dao;

import com.example.studentmanagement.model.Student;
import com.fasterxml.jackson.databind.BeanProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

//CrudRepository includes add, insert, update, del... for database
//with input of Object, Primary Key
//Paging with extra options to set page size
public interface StudentDao extends PagingAndSortingRepository<Student,Long> {

    //define by self, to search entity with a field
    //named findBy"FieldName". No need to implement
    List<Student> findByName(String name);
}
