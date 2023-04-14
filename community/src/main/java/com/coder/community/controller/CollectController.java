package com.coder.community.controller;

import com.coder.community.entity.User;
import com.coder.community.service.CollectService;
import com.coder.community.util.CommunityUtil;
import com.coder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CollectController {

    @Autowired
    private CollectService collectService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/collect", method = RequestMethod.POST)
    @ResponseBody
    public String collect(int postId) {
        User user = hostHolder.getUser();
        // 收藏操作
        collectService.collect(user.getId(), postId);
        // 获取文章被收藏的数量
        long collectCount = collectService.findArticleCollectCount(postId);
        // 状态
        int collectStatus = collectService.findArticleCollectStatus(user.getId(), postId);
        // 返回结果封装一下传递给页面
        Map<String, Object> map = new HashMap<>();
        map.put("collectCount", collectCount);
        map.put("collectStatus", collectStatus);

        return CommunityUtil.getJSONString(0, null, map);
    }

}
