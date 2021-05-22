package com.example.share_idea;

public class ItemModel {
    private String name;
    private String description;
    private String Img;

    public ItemModel(String name, String description, String img) {
        this.name = name;
        this.description = description;
        Img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
