package com.example.notessecurity;

import com.google.firebase.Timestamp;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class Details {

    String title;
    String account;
    String password;
    Timestamp timestamp;

    public Details() {
    }

    public String getTitle()  {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}
