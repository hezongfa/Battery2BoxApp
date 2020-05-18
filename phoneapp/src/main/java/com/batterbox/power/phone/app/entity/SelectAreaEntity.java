package com.batterbox.power.phone.app.entity;

import com.chenyi.baselib.entity.BaseEntity;

public class SelectAreaEntity extends BaseEntity {
    public long id;
    public long pid;
    public String path;
    public String name;
    public int level=-1;
    public int getLevel(){
        if (level==-1&&path.contains(",")){
            String[] s=path.split(",");
            level=s.length-2;
        }
        return level;
    }
}
