package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Applys;
import com.example.springboot.entity.Job;
import com.example.springboot.entity.User;
import com.example.springboot.service.IApplysService;
import com.example.springboot.service.IJobService;
import com.example.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IApplysService applysService;

    @GetMapping("/data")
    public Result data() {
        List<String> collect = jobService.list().stream().map(Job::getName).distinct().collect(Collectors.toList());

        List<Map<String, Object>> list = new ArrayList<>();
        for (String s : collect) {
            Map<String, Object> map = new HashMap<>();
            map.put("name",s);
            map.put("value",applysService.count(new LambdaQueryWrapper<Applys>().eq(Applys::getName,s)));
            list.add(map);
        }
        return Result.success(list);
    }

}
