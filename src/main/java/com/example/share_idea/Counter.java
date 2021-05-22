package com.example.share_idea;

import android.app.Application;

public class Counter extends Application {
    public int mmcounter;
    public int complete_Add;
    public String AddAteps;
    public int mAddAteps_check;

    public int getmAddAteps_check() {
        return mAddAteps_check;
    }

    public void setmAddAteps_check(int mAddAteps_check) {
        this.mAddAteps_check = mAddAteps_check;
    }

    public String getAddAteps() {
        return AddAteps;
    }

    public void setAddAteps(String addAteps) {
        AddAteps = addAteps;
    }

    public int getComplete_Add() {
        return complete_Add;
    }

    public void setComplete_Add(int complete_Add) {
        this.complete_Add = complete_Add;
    }

    //全域變數
    public void counter(int counter){
        this.mmcounter = counter;
    }

    public void AddAteps(String AddAteps){this.AddAteps = AddAteps;}
    public void mAddAteps_check(int AddAteps){this.mAddAteps_check = mAddAteps_check;}
    public int getCounter() {
        return mmcounter;
    }

    public void setCounter(int counter) {
        this.mmcounter = counter;
    }

}
