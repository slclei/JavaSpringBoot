package com.example.demo.service;

import com.example.demo.dao.StudentDao;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//tell spring that if someone need StudentService
//just new one of StudentService
@Service
public class StudentService {

    //use the right dao
    private StudentDao studentDao;

    //with autowired, this constructor will be created automatically
    //with class with @Repository that implements StudentDao
    //if more than one StudentDao are implemented,then @Qualifier(fakeStudentDao")
    //is required before StudentDao.
    @Autowired
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> getAllStudents(){
        return studentDao.selectAllStudent();
    }

    public int addStudent(Student student){
        return studentDao.insertStudent(student);
    }

    public int updateStudent(Student student){
        return studentDao.updateStudent(student);
    }

    public int deleteStudent(UUID id){
        return studentDao.deleteStudentById(id);
    }
}
