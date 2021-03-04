package com.mkierzkowski.vboard_back.service.storage;

public enum RelativePaths {
    PROFILE_PICS("./profilePics");

    private final String path;

    RelativePaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
