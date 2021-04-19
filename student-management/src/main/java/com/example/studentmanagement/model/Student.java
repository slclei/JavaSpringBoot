package com.example.studentmanagement.model;

//import javax.annotation.processing.Generated;
import javax.persistence.*;

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
