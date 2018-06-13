package com.example.emad.newsfeedapp;

/**
 * Created by EMAD on 1/28/2018.
 */

public class News_object {
    String title;
    String name;
    String date;
    String link;
    String author;
    public News_object(String title, String name, String date, String author, String link) {
        this.title = title;
        this.name = name;
        this.date = date;
        this.link = link;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
        return author;
    }
}
