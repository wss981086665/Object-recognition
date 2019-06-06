package com.wss.demo.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import com.wss.demo.auxiliary.Base64Util;
import com.wss.demo.auxiliary.FileUtil;
import com.wss.demo.auxiliary.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.sql.ResultSet;

@RestController
public class TestController {

    private String accessToken = "";

    @RequestMapping("/animal")
    @ResponseBody
    public String animal() {
        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
        accessToken = AuthService.getToken();

        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal";
        try {
            // 本地文件路径
            String filePath = "F:/Google下载/aaa.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam + "&top_num=" + 6;

            String result = HttpUtil.post(url, accessToken, param);

            JSONObject json = new JSONObject(result);
            JSONObject answer = (JSONObject) json.getJSONArray("result").get(0);

            return answer.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
