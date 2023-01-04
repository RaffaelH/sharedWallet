package de.hawlandshut.sharedwallet.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

    public static boolean isRequiredLength(int requiredLength, String string){
        return string.length() >= requiredLength;
    }

    public static boolean isEqualPw(String pw1, String pw2){
        return pw1.equals(pw2);
    }

    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    public static boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public static boolean isValidEmail(String string){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }



}
