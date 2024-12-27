package com.example.akhirtolong;

import java.util.HashMap;
import java.util.Map;

public class HelperClass {
    String Nama, Email, Password, ConfPassword;

    public HelperClass(String Nama, String Email, String Password, String ConfPassword){
        this.Nama = Nama;
        this.Email = Email;
        this.Password = Password;
        this.ConfPassword = ConfPassword;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", getNama());
        result.put("email", getEmail());
        result.put("password", getPassword());
        result.put("confPassword", getConfPassword());
        return result;
    }


    public String getNama() {
        return Nama;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getConfPassword() {
        return ConfPassword;
    }
}

