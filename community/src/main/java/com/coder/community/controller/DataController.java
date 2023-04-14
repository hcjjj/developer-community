package com.coder.community.controller;

import com.coder.community.service.DataService;
import com.coder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 统计页面 即可以处理GET 也可以POST请求因为后面有POST请求转发过来，还是POST请求
    @RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage(Model model) throws ParseException {
        // 日期格式化
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df2 = new SimpleDateFormat("M/d");
        Calendar calendar = Calendar.getInstance();
        String startTime = "2022-04-10";
        Date start= new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
        calendar.setTime(start);
        Date end = new Date();
        List<String> uvDate = new ArrayList<>();
        List<Long> uvCount = new ArrayList<>();
        List<Long> dauCount = new ArrayList<>();
        while (calendar.getTime().before(end)) {
            uvDate.add(df2.format(calendar.getTime()));
            // UV
            String uvRedisKey = RedisKeyUtil.getUVKey(df.format(calendar.getTime()));
            uvCount.add(redisTemplate.opsForHyperLogLog().size(uvRedisKey));
            // ADU
            String dauRedisKey = RedisKeyUtil.getDAUKey(df.format(calendar.getTime()));
            List<byte[]> keyList = new ArrayList<>();
            keyList.add(dauRedisKey.getBytes());
            long result =  (long) redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    String redisKey = RedisKeyUtil.getDAUKey(df.format(start), df.format(end));
                    connection.bitOp(RedisStringCommands.BitOperation.OR,
                            redisKey.getBytes(), keyList.toArray(new byte[0][0]));
                    return connection.bitCount(redisKey.getBytes());
                }
            });
            dauCount.add(result);
            // 加1天
            calendar.add(Calendar.DATE, 1);
        }
        model.addAttribute("uvDate", uvDate);
        model.addAttribute("uvCount", uvCount);
        model.addAttribute("dauCount", dauCount);
        return "/site/admin/data";
    }

    // 统计网站UV  字符串转日期
    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);
        return "forward:/data";
    }

    // 统计活跃用户
    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                         @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        // 即 return /site/admin/data 转发到上面的 path = "/data"
        return "forward:/data";
    }

}
