package com.example.smartattendance;

public class DAOinfo {
    private String NAME;
    private String ID;
    private String COMP;
    private String PW;

    public String getCOMP() {
        return COMP;
    }

    public void setCOMP(String COMP) {
        this.COMP = COMP;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }



    public DAOinfo(String id, String pw, String name, String comp){
        this.ID = id;
        this.PW = pw;
        this.NAME = name;
        this.COMP = comp;
    }
    public DAOinfo(){

    }
}
