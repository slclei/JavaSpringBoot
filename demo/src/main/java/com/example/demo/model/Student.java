package com.example.demo.model;

import java.util.UUID;

public class Student {

    //As this is a demo, use UUID to stand for the key in real database
    private UUID id;

    //private can not be used for xuliehua; therefore, it can not be recognized by controller
    //public can be used for recognized by controller
    //with getter, it can also be recognized by controller
    private String name;

    //from Code->generate->constructor
    public Student(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    //from Code->generate->getter
    public UUID getId() {
        return id;
    }

    //JsonIgnore can be used to ignore this getter to be passed to url for display
    public String getName() {
        return name;
    }
}
