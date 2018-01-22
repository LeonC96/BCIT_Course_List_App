package com.leonchai.a00940442.finalproject;

import java.util.ArrayList;

/**
 * Created by Leon_ on 7/17/2017.
 */

public class Course {

    private String courseNumber;
    private String courseName;
    private String credit;
    private String description;
    private String prerequisite;
    private boolean hasPrereq = true;
    private ArrayList<Class> classes;
    private ArrayList<String> terms;

    private int mData;


    public Course(){
        this.courseNumber = "";
        this.courseName = "";
        this.credit = "";
        this.description = "";
        this.prerequisite = "";
        this.classes = new ArrayList<>();
        this.terms = new ArrayList<>();
    }

    public Course(String number, String courseName){
        this.courseNumber = number;
        this.courseName = courseName;
        this.credit = "";
        this.description = "";
        this.prerequisite = "";
        this.classes = new ArrayList<>();
        this.terms = new ArrayList<>();

    }


    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {

        if(prerequisite.isEmpty()){
            setHasPrereq(false);
        } else {
            setHasPrereq(true);
        }

        this.prerequisite = prerequisite;
    }

    public boolean isHasPrereq() {
        return hasPrereq;
    }

    public void setHasPrereq(boolean hasPrereq) {
        this.hasPrereq = hasPrereq;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = terms;
    }
}
