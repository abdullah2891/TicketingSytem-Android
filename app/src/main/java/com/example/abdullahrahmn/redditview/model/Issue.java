package com.example.abdullahrahmn.redditview.model;

public class Issue {
    public Issue(String id ,String title){
        this.id = id;
        this.title = title;
    }

    public String id;
    public String title;
    public String description;
    public String status;
    public String projects ;
    public String date_created;
    public String date_modified;
}
