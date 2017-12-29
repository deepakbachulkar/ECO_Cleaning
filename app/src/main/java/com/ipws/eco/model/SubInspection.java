package com.ipws.eco.model;

/**
 * Created by ziffi on 12/25/17.
 */

public class SubInspection {
    String name, subName, properties;
    int id;
    boolean isCheck;

    public SubInspection(){ }

    public SubInspection(int id, String name, String properties, boolean isCheck)
    {
        this.id = id;
        this.name= name;
        this.subName =subName;
        this.properties = properties;
        this.isCheck = isCheck;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}

