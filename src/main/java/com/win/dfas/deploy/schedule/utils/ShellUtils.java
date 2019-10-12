package com.win.dfas.deploy.schedule.utils;

import cn.hutool.core.util.StrUtil;

import java.util.List;

public class ShellUtils {
    /**
     * 根据Runtime.exec返回的List<string>,取最后一行判断是否'OK'字符串，
     * 是则返回成功
     * @param resultList
     * @return
     */
    public static boolean isSuccess(List<String> resultList) {
        if(resultList == null || resultList.size() == 0) {
            return false;
        }

        String result = resultList.get(resultList.size()-1);
        if(result == null) {
            return false;
        }
        if(result.equalsIgnoreCase("OK")) {
            return true;
        }

        return false;
    }
}
