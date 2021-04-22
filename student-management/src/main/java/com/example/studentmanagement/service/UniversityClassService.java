package com.example.studentmanagement.service;

import com.example.studentmanagement.dao.UnversityClassDao;
import com.example.studentmanagement.exceptions.InvalidUniversityClassException;
import com.example.studentmanagement.model.UniversityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class UniversityClassService {
    private UnversityClassDao unversityClassDao;

    @Autowired
    public UniversityClassService(UnversityClassDao unversityClassDao) {
        this.unversityClassDao = unversityClassDao;
    }

    public List<UniversityClass> getAllClasses(){
        return (List<UniversityClass>) unversityClassDao.findAll();
    }

    public UniversityClass addClass(UniversityClass universityClass){
        int currentYear= Calendar.getInstance().get(Calendar.YEAR);

        if (universityClass.getYear()<currentYear){
            throw new InvalidUniversityClassException("Cannot add class in the past");
        }
        if (universityClass.getYear()>currentYear+1){
            throw new InvalidUniversityClassException("Cannot add class in the future");
        }

        return unversityClassDao.save(universityClass);
    }
}
