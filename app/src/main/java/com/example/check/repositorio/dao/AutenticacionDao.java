package com.example.check.repositorio.dao;

public class AutenticacionDao {
    public Boolean esValido(String user, String mail, String pass, String passcon) {
        if (user == null || user.equals("")) {
            return false;
        } else if (mail == null || mail.equals("")) {
            return false;
        } else if (pass == null || pass.equals("")) {
            return false;
        } else if (passcon == null || passcon.equals("")) {
            return false;
        } else return pass.equals(passcon);
    }
}
