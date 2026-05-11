package com.noetic.model;

public class User {
    private String email;
    private String password;
    private String displayName;
    private boolean admin;

    public User(String email, String password, String displayName, boolean admin) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.admin = admin;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDisplayName() { return displayName; }
    public boolean isAdmin() { return admin; }
}
