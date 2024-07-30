package com.example.ritik_1;

public class ContactDetail {

    private String dataName;
    private String dataNumber;
    private String dataRelation;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataNumber() {
        return dataNumber;
    }

    public String getDataRelation() {
        return dataRelation;
    }

    public String getDataImage() {
        return dataImage;
    }

    public ContactDetail(String dataName, String dataNumber, String dataRelation, String dataImage) {
        this.dataName = dataName;
        this.dataNumber = dataNumber;
        this.dataRelation = dataRelation;
        this.dataImage = dataImage;
    }
    public ContactDetail(){

    }
}


