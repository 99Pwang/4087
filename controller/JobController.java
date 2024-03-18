package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.config.interceptor.AuthAccess;
import com.example.springboot.entity.*;
import com.example.springboot.service.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.utils.TokenUtils;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  Controller
 * </p>
 *
 * @author
 * @since
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private IJobService jobService;
    @Resource
    IUserService userService;
    @Resource
    IPaperService paperService;
    @Resource
    ICollectService collectService;
    @Resource
    IApplysService applysService;

    private final String now = DateUtil.now();

    @GetMapping("/all")
    public Result all() {
        List<String> collect = jobService.list().stream().map(Job::getName).distinct().collect(Collectors.toList());
        List<Map> list = new ArrayList<>();
        for (String s : collect) {
            Map<String, Object> map = new HashMap<>();
            map.put("value",s);
            map.put("label",s);
            list.add(map);
        }
        return Result.success(list);
    }


    // add or edit
    @PostMapping
    public Result save(@RequestBody Job job) {
        job.setTime(DateUtil.now());
        job.setPhone(TokenUtils.getCurrentUser().getPhone());
        job.setManager(TokenUtils.getCurrentUser().getNickname());
        job.setManagerid(TokenUtils.getCurrentUser().getId());
        jobService.saveOrUpdate(job);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        jobService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        jobService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(jobService.list());
    }



    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(jobService.getById(id));
    }

    @AuthAccess
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(defaultValue = "") String category,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        if (!"".equals(category)) {
            queryWrapper.like("category", category);
        }

        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getRole().equals("ROLE_MANAGER")) {
            queryWrapper.eq("managerid", currentUser.getId());
        }
        Page<Job> page = jobService.page(new Page<>(pageNum, pageSize), queryWrapper);
        if (currentUser != null) {
            for (Job record : page.getRecords()) {
                List<Collect> collectList = collectService.list(new QueryWrapper<Collect>().eq("jobid", record.getId()).eq("userid", currentUser.getId()));
                record.setHasCollect(collectList.size()> 0);
                List<Applys> applysList = applysService.list(new QueryWrapper<Applys>().eq("jobid", record.getId()).eq("userid", currentUser.getId())
                        .ne("status", "Audit rejected"));
                record.setHasApply(applysList.size()> 0);
            }
        }
        return Result.success(page);
    }

    /**
     * export
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        List<Job> list = jobService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Job table", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
     * excel import
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Job> list = reader.readAll(Job.class);

        jobService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

