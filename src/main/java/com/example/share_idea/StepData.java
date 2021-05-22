package com.example.share_idea;

public class StepData {
    private String mid;
    private String mimage;
    private String mcontent;
    public StepData(String id, String content, String image){
        this.mid = id;
        this.mcontent = content;
        this.mimage = image;
    }
    public String getImage() {
        return mimage;
    }

    public void setImage(String image) {
        this.mimage = image;
    }

    public String getContent() {
        return mcontent;
    }

    public void setContent(String continent) {
        this.mcontent = continent;
    }

    public String getId() {
        return mid;
    }

    public void setId(String continent) {
        this.mid = continent;
    }
}
