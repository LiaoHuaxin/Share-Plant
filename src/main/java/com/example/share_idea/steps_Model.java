package com.example.share_idea;

public class steps_Model {
    String id, title, imag;

    public steps_Model(String id,String title, String img ){
        this.id = id;
        this.title = title;
        this.imag = img;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }
}
