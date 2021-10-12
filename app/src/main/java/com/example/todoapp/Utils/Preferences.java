package com.example.todoapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    static final String KEY_LOGIN_STATUS = "login_status";
    static final String KEY_LOGGED_IN_USERID = "logged_in_userid";
    static final String KEY_LOGGED_IN_USERNAME = "logged_in_username";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedInUserId(Context context, int userId) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(KEY_LOGGED_IN_USERID, userId);
        editor.apply();
    }

    public static int getLoggedInUserId(Context context) {
        return getSharedPreference(context).getInt(KEY_LOGGED_IN_USERID,0);
    }

    public static void setLoggedInUser(Context context, String username) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_LOGGED_IN_USERNAME, username);
        editor.apply();
    }

    public static String getLoggedInUser(Context context) {
        return getSharedPreference(context).getString(KEY_LOGGED_IN_USERNAME,"");
    }

    public static void setLoginStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_LOGIN_STATUS,status);
        editor.apply();
    }

    public static boolean getLoginStatus(Context context) {
        return getSharedPreference(context).getBoolean(KEY_LOGIN_STATUS,false);
    }

    public static void clearLoggedInUser (Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_LOGGED_IN_USERID);
        editor.remove(KEY_LOGGED_IN_USERNAME);
        editor.remove(KEY_LOGIN_STATUS);
        editor.apply();
    }
}
