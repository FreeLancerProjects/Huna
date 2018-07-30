package com.semicolon.criuse.SingleTones;

import com.semicolon.criuse.Models.UserModel;

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
