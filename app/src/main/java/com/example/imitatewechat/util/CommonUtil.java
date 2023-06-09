package com.example.imitatewechat.util;

import com.alibaba.fastjson.JSONArray;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CommonUtil {
    /**
     * 获取用户header
     * 如: "张三"的header就是"Z"，用于用户默认分组
     *
     * @param userNickName 用户昵称
     * @return 用户header
     */
    public static String setUserHeader(String userNickName) {
        StringBuffer stringBuffer = new StringBuffer();
        // 将汉字拆分成一个个的char
        char[] chars = userNickName.toCharArray();
        // 遍历汉字的每一个char
        for (int i = 0; i < chars.length; i++) {

            try {
                HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

                // UPPERCASE：大写  (ZHONG)
                format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//输出大写

                // WITHOUT_TONE：无音标  (zhong)
                format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
                // 汉字的所有读音放在一个pinyins数组
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                if (pinyins == null) {
                    stringBuffer.append(chars[i]);
                } else {
                    stringBuffer.append(pinyins[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
        char firstChar = stringBuffer.toString().toUpperCase().charAt(0);
        // 不是A-Z字母
        if (firstChar > 90 || firstChar < 65) {
            return "#";
        } else { // 代表是A-Z
            return String.valueOf(firstChar);
        }
    }

    /**
     * 生成主键
     *
     * @return 主键
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * json字符串转泛型列表
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @param <T>   泛型
     * @return 泛型列表
     */
    public static <T> List<T> getListFromJson(String json, Class<T> clazz) {
        List<T> resultList;
        try {
            resultList = JSONArray.parseArray(json, clazz);
            if (null == resultList) {
                resultList = new ArrayList<>();
            }
        } catch (Exception e) {
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static String now() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
