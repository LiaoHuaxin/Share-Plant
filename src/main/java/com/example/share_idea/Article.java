package com.example.share_idea;

public class Article {
    public String mimage_view;
    public String mtxtdata;
    public String mcounts;
    public String mtxtcontent;
    public Article(String mimage_view, String mtxtdata, String mcounts, String txtcontent) {
        this.mimage_view = mimage_view;
        this.mtxtdata = mtxtdata;
        this.mcounts = mcounts;
        this.mtxtcontent = txtcontent;
    }

    public String getMimage_view() {
        return (mimage_view);
    }

    public void setMimage_view(String mimage_view) {
        this.mimage_view = mimage_view;
    }

    public String getMtxtdata() {
        return mtxtdata;
    }

    public void setMtxtdata(String mtxtdata) {
        this.mtxtdata = mtxtdata;
    }

    public String getMcounts() {
        return mcounts;
    }

    public void setMcounts(String mcounts) {
        this.mcounts = mcounts;
    }

    public String getMtxtcontent() {
        return mtxtcontent;
    }

    public void setMtxtcontent(String mtxtcontent) {
        this.mtxtcontent = mtxtcontent;
    }
}
