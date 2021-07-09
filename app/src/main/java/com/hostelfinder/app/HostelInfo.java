package com.hostelfinder.app;

public class HostelInfo {
    String name;
    int icon;

    public HostelInfo(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
