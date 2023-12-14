package com.users.findo.dataClasses;

public class Misc {
    boolean maintainance_user;
    String desc;

    public Misc(){};

    public Misc(boolean maintainance_user, String desc) {
        this.maintainance_user = maintainance_user;
        this.desc = desc;
    }

    public boolean isMaintainance_user() {
        return maintainance_user;
    }

    public void setMaintainance_user(boolean maintainance_user) {
        this.maintainance_user = maintainance_user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
