package com.game.util;

import java.util.Random;

public class IdGenerator {
    private static final Random random = new Random();
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成指定长度的随机数字 ID
     *
     * @param length ID 长度
     * @return 随机数字 ID
     */
    public static String generateId(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 生成 0-9 的随机数字
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的随机字母或数字 ID
     *
     * @param length ID 长度
     * @return 随机字母或数字 ID
     */
    public static String generateAlphaNumericId(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }
}