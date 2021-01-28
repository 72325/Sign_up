package com.example.sign_up;

public class Member {
    String Email;
    String User;
    String Number;
    String Name;
    public Member(){}

    public String getEmail() {
        return Email;
    }

    public String getNumber() {
        return Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
