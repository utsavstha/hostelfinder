package com.hostelfinder.app;

import com.hostelfinder.app.model.Hostel;

public class AppCache {
    private static Hostel hostel;

    public static Hostel getHostel() {
        return hostel;
    }

    public static void setHostel(Hostel hostel) {
        AppCache.hostel = hostel;
    }
}
