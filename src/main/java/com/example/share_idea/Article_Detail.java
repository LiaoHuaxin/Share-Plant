package com.example.share_idea;

import java.util.HashMap;

public class Article_Detail {
    String title_img,title,content,requirements,Introduce_Plant,notice_item;
    HashMap<String, Object> steps;

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getIntroduce_Plant() {
        return Introduce_Plant;
    }

    public void setIntroduce_Plant(String introduce_Plant) {
        Introduce_Plant = introduce_Plant;
    }

    public String getNotice_item() {
        return notice_item;
    }

    public void setNotice_item(String notice_item) {
        this.notice_item = notice_item;
    }

    public Article_Detail(String title_img, String title, String Introduce_Plant, String requirements, String content, String notice_item, HashMap<String, Object> steps) {
        this.title_img = title_img;
        this.title = title;
        this.Introduce_Plant = Introduce_Plant;
        this.requirements = requirements;
        this.content = content;
        this.notice_item = notice_item;
        this.steps = steps;
    }

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCounts() {
        return requirements;
    }

    public void setCounts(String counts) {
        this.requirements = counts;
    }

    public HashMap<String, Object> getSteps() {
        return steps;
    }

    public void setSteps(HashMap<String, Object> steps) {
        this.steps = steps;
    }
}
