package com.ipws.eco.model;

import java.util.List;

/**
 * Created by ziffi on 12/25/17.
 */

public class Inspection {
    String name, subName, properties;
    int id;
    boolean isCheck;
    List<SubInspection> listSubInspection;

    public Inspection(){ }

    public Inspection(int id, String name, String subName, String properties, boolean isCheck, List<SubInspection>  listSubInspection)
    {
        this.id = id;
        this.name= name;
        this.subName =subName;
        this.properties = properties;
        this.isCheck = isCheck;
        this.listSubInspection = listSubInspection;
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

    public List<SubInspection> getListSubInspection() {
        return listSubInspection;
    }

    public void setListSubInspection(List<SubInspection> listSubInspection) {
        this.listSubInspection = listSubInspection;
    }
}

