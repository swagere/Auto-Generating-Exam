package com.group.auto_generating_exam.util;

import java.util.Date;

public class timeUtils {
    /**
     * 获取精确到秒的时间戳
     *
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.parseInt(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }
}
