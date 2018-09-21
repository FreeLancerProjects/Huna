package com.semicolon.huna.SingleTones;

import com.semicolon.huna.Models.UserModel;

public class UserSingletone {
    private static UserSingletone instance=null;
    private UserModel userModel;
    private UserSingletone() {
    }

    public static synchronized UserSingletone getInstance()
    {
        if (instance==null)
        {
            instance = new UserSingletone();
        }
        return instance;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }
    public UserModel getUserModel()
    {
        return userModel;
    }
}
