package com.example.quanla.smartschool.sharePrefs;

/**
 * Created by QuanLA on 3/10/2017.
 */

public class LoginCredentials {
    private String username;
    private String password;
    private String idLogin;

    public LoginCredentials(String username, String password, String idLogin){
        this.username = username;
        this.password = password;
        this.idLogin = idLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIdLogin(){
        return idLogin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdLogin(String idLogin){
        this.idLogin = idLogin;
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", idLogin='" + idLogin + '\'' +
                '}';
    }
}
