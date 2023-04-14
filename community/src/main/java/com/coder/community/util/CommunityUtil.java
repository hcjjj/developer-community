package com.coder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    // 生成随机字符串，删除所有的 "-"
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456  容易被破解
    // hello + 3e4a8 -> abc123def456abc  + user表中的salt字段
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 将业务数据整合到一个JSON字符串，方便浏览器处理
     * @param code 状态码
     * @param msg 提示信息
     * @param map 业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    // 方法重载，方便调用
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }


    // editor.md 要求返回的 JSON 字符串格式
    public static String getEditorMdJSONString(int success, String message, String url) {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", message);
        json.put("url", url);
        return json.toJSONString();
    }

//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "hcj");
//        map.put("age", "23");
//        System.out.println(getJSONString(0, "ok", map));
//    }

}
