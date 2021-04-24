package com.example.studentmanagement.service;

import com.example.studentmanagement.dao.StudentDao;
import com.example.studentmanagement.dao.UnversityClassDao;
import com.example.studentmanagement.exceptions.InvalidUniversityClassException;
import com.example.studentmanagement.exceptions.StudentEmptyNameException;
import com.example.studentmanagement.exceptions.StudentNonExistException;
import com.example.studentmanagement.mapper.StudentMapper;
import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.model.UniversityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StudentService {

    private StudentDao studentDao;
    private UnversityClassDao unversityClassDao;
    //for MyBatis
    private StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentDao studentDao,UnversityClassDao unversityClassDao,
                          StudentMapper studentMapper) {
        this.studentDao = studentDao;
        this.unversityClassDao=unversityClassDao;
        this.studentMapper=studentMapper;
    }

    public Student addStudent(Student student){
        if (student.getName().isEmpty()){
            throw new StudentEmptyNameException("Student name cannot be empty");
        }
        return studentDao.save(student);
    }

    public Student updateStudent(Student student){
        if (student.getId()==null || studentDao.existsById(student.getId())){
            throw new StudentNonExistException("Cannot find student ID");
        }

        return studentDao.save(student);
    }

    //install a class for this student
    public Student assignClass(Long studentId, Long classId){
        if (!studentDao.existsById(studentId)){
            throw new StudentNonExistException("Cannot find student ID: "+studentId);
        }
        if (!unversityClassDao.existsById(classId)){
            throw new InvalidUniversityClassException("Cannot find class ID: "+classId);
        }

        Student student=getStudentById(studentId).get();
        UniversityClass universityClass=unversityClassDao.findById(classId).get();

        student.setUniversityClass(universityClass);
        return studentDao.save(student);
    }

    public List<Student> getAllStudents(){
        return (List<Student>) studentDao.findAll();
    }

    public Optional<Student> getStudentById(Long id){
        return studentDao.findById(id);
    }

    public List<Student> getStudentsByName(String name){
        return studentDao.findByName(name);
    }


}
