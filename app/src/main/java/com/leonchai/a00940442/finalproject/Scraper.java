package com.leonchai.a00940442.finalproject;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Leon_ on 7/17/2017.
 */

public class Scraper {

    private static final int DESCRIPTION_LOCATION = 0;
    private static final int PREREQ_LOCATION = 1;
    private static final int CREDIT_LOCATION = 2;
    private static final String COURSE_URL = "https://www.bcit.ca/study/courses/";

    public static ArrayList<String> getCourseNamesList(){

        ArrayList<String> classNames = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.bcit.ca/study/programs/5500pdiplt#courses").get();
            Elements classes = doc.getElementsByClass("course_name clicktoshow_off");

            for (Element a : classes) {
                classNames.add(a.text());
            }

        } catch (IOException e) {
            Log.e("JSOUP", e.getMessage());
        }

        return classNames;
    }

    public static ArrayList<String> getCourseNumbersList(){
        ArrayList<String> classNumbers = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://www.bcit.ca/study/programs/5500pdiplt#courses").get();
            Elements numbers = doc.getElementsByClass("course_number");

            for(Element x : numbers){
                classNumbers.add(x.text());
            }

        } catch (IOException e) {
            Log.e("JSOUP", e.getMessage());
        }

        return classNumbers;
    }

    public static String getCourseDescription(String courseNumber){

        String number = courseNumber.replaceAll("\\s+", "");
        String description = "";

        try {
            Document doc = Jsoup.connect(COURSE_URL + number).get();
            Element details = doc.getElementById("details");
            Elements pTags = details.getElementsByTag("p");

            description = pTags.get(DESCRIPTION_LOCATION).text();

        } catch (IOException e){
            Log.e("JSOUP", e.getMessage());
        }

        return description;
    }

    public static String getCoursePrereq(Course course){
        String number = course.getCourseNumber().replaceAll("\\s+", "");
        String prereq = "";

        try {
            Document doc = Jsoup.connect(COURSE_URL + number).get();
            Element details = doc.getElementById("details");
            Elements pTags = details.getElementsByTag("p");

            Elements h2tags = details.getElementsByTag("h2");

            if(course.isHasPrereq()) {
                prereq = pTags.get(PREREQ_LOCATION).text();
            }

        } catch (IOException e){
            Log.e("JSOUP", e.getMessage());
        }

        return prereq;
    }

    public static String getCourseCredit(Course course){
        String number = course.getCourseNumber().replaceAll("\\s+", "");
        String credit = "";

        try {
            Document doc = Jsoup.connect(COURSE_URL + number).get();
            Element details = doc.getElementById("details");
            Elements pTags = details.getElementsByTag("p");


            //TODO: if a course doesn't have prereq, location of every other strings are different

            if(course.isHasPrereq()) {
                credit = pTags.get(CREDIT_LOCATION).text();
            } else {
                credit = pTags.get(1).text();
            }

        } catch (IOException e){
            Log.e("JSOUP", e.getMessage());
        }

        return credit;
    }

    public static ArrayList<String> getTerms(Course course){
        String number = course.getCourseNumber().replaceAll("\\s+", "");
        Set<String> termNames = new HashSet<>();
        ArrayList<String> terms = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(COURSE_URL + number).get();
            Elements term = doc.getElementsByClass("crse-term");

            for (Element e : term) {
                Elements q = e.getElementsByTag("h1");
                for (Element w : q) {
                    String a = w.text().trim().toLowerCase();
                    if (a.contains("spring") || a.contains("fall") || a.contains("winter") || a.contains("summer")) {
                        termNames.add(w.text());

                       //System.out.println(w.text());
                    }
                }
            }
        } catch (IOException e){
            Log.e("JSOUP", e.getMessage());
        }
        terms.addAll(termNames);
        return terms;
    }

    public static ArrayList<Class> getClasses(Course course){
        String number = course.getCourseNumber().replaceAll("\\s+", "");

        ArrayList<Class> classes = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(COURSE_URL + number).get();
            Elements term = doc.getElementsByClass("crse-term");

            for(int n = 0; n < course.getTerms().size(); n++){
                Elements termInfo = term.get(n).getElementsByTag("article");

                for(int i = 0; i < termInfo.size(); i++){
                    Element classInfo = termInfo.get(i);
                    Class courseClass = new Class();

                    courseClass.setCourseNumber(course.getCourseNumber());
                    courseClass.setTerm(course.getTerms().get(n));

                    Elements pTags = classInfo.getElementsByTag("p");

                    courseClass.setDates(pTags.get(0).text());
                    courseClass.setInstructor(pTags.get(1).text());
                    courseClass.setCost(pTags.get(3).text());

                    Elements table = classInfo.getElementsByTag("tbody");
                    Elements tds = table.get(0).getElementsByTag("td");

                    courseClass.setDays(tds.get(1).text());
                    courseClass.setTimes(tds.get(2).text());
                    courseClass.setCampus(tds.get(3).text());

                    Elements li = classInfo.getElementsByTag("li");

                    if(li.size() > 0 ) {
                        courseClass.setNotes(li.get(0).text());
                    }

                    classes.add(courseClass);

                }
            }
        } catch (IOException e){
            Log.e("JSOUP", e.getMessage());
        }

        return classes;
    }
}


