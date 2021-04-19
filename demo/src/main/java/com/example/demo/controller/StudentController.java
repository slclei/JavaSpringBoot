package com.example.demo.controller;

import com.example.demo.dao.StudentDao;
import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//default url: localhost:8080
//set port # in /resources/application.properties
@RestController
//set url: localhost:8080/api/student
@RequestMapping("api/student")
public class StudentController {

    /*GetMapping is default request from url in RequestMapping
    @GetMapping
    public String helloWorld(){
        return "hello world";
    }*/

    //create student service
    private StudentService studentService;

    //autowired to create studentService with Service

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //Try random student id and test name
    //Get will not modify file in server
    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    //Post can modify file in server
    //same url with different methods
    @PostMapping
    public String addStudent(@RequestBody Student student){
        studentService.addStudent(student);
        return "Added student";
    }

    @PutMapping
    public String updateStudent(@RequestBody Student student){
        studentService.updateStudent(student);
        return "updated student";
    }

    @GetMapping
    //set url: localhost:8080/api/student/h1
    @RequestMapping("/h1")
    public String helloWorld1(){
        return "hello world1";
    }

    //Path is used to set a specific path. Note {}
    @DeleteMapping(path="{id1}")
    //localhost:8080/api/student/"id1"
    public String deleteStudent(@PathVariable("id1") UUID id){
        studentService.deleteStudent(id);
        return "Deleted studet";
    }
}
