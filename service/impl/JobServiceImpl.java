package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.Job;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.JobMapper;
import com.example.springboot.service.ICollectService;
import com.example.springboot.service.IJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Resource
    ICollectService collectService;
    @Resource
    IUserService userService;


}
