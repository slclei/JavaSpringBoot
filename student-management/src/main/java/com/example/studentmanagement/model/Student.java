package com.example.studentmanagement.model;

//import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

//tell the database that this is an entity
//(name="") can be used to rename this entity
//Database setting is in resources/application.properties
@Entity
@Table(name="student")
public class Student {

    //set ID, to be generated auto
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //set columns
    @Column(nullable = false, name="name")
    private String name;

    @Version
    @JsonIgnore
    private Long version;

    @Override
    public int hashCode(){
        return Objects.hash(id,name,version);
    }

    @Override
    public boolean equals(Object o){
        if (this==o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student=(Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(name, student.name) &&
                Objects.equals(version, student.version);
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getVersion() {
        return version;
    }

    /*
    public UniversityClass getUniversityClass() {
        return universityClass;
    }

    public void setUniversityClass(UniversityClass universityClass) {
        this.universityClass = universityClass;
    }

    //set foreign key
    @ManyToOne
    @JoinColumn(name="University_Class_id")
    private UniversityClass universityClass;*/

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Student(){

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    //override toString

    @Override
    public String toString() {
        String str="";
        str +="Primary ID: "+getId();
        str +="Name: "+getName();
        return str;
    }
}
