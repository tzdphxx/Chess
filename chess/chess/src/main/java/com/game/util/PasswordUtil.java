package com.game.util;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private PasswordUtil() {}

    public static String encodedPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String encodedPassword) {
        return BCrypt.checkpw(password, encodedPassword);
    }

}
