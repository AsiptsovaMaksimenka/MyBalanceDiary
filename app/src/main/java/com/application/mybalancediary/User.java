package com.application.mybalancediary;

public class User {
    public String name;
    public String gender;
    public int age;
    public String height;
    public float weight;
    public int caloriegoal;


    public User() {

    }

    public User(String name, String gender, int age, String height, float weight, int caloriegoal) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.caloriegoal = caloriegoal;
    }
}
