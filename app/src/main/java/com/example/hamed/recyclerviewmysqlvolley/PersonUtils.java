package com.example.hamed.recyclerviewmysqlvolley;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class PersonUtils  {

    private int id,code;
    private String username;
    private int mojodi;
    private int wam;
    private boolean checked;

    public boolean ischecked() {
        return checked;
    }

    public void setchecked(boolean checked) {
        this.checked = checked;
    }

    public int getwam() {
        return wam;
    }

    public void setwam(int wam) {
        this.wam = wam;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getmojodi() {
        return mojodi;
    }

    public void setmojodi(int mojodi) {
        this.mojodi = mojodi;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }


}