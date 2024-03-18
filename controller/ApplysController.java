package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Paper;
import com.example.springboot.service.IPaperService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import org.springframework.web.multipart.MultipartFile;
import com.example.springboot.entity.User;
import com.example.springboot.utils.TokenUtils;
import com.example.springboot.service.IUserService;

import com.example.springboot.service.IApplysService;
import com.example.springboot.entity.Applys;

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
@RequestMapping("/applys")
public class ApplysController {

    @Resource
    private IApplysService applysService;
    @Resource
    IPaperService paperService;
    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    // add or edit
    @PostMapping
    public Result save(@RequestBody Applys applys) {
        if (applys.getId() == null) {
            applys.setTime(DateUtil.now());
            applys.setUserid(TokenUtils.getCurrentUser().getId());

            Double total = 0.0;
            Paper paper = paperService.getById(applys.getPaperid());
            if (paper.getEducation().equals("Bachelor's degree")){
                total += 1;
            } else if (paper.getEducation().equals("Master's degree")){
                total += 3;
            } else {
                total += 5;
            }

            if (paper.getYears().equals("Less than one year")){
                total += 1;
            } else if (paper.getYears().equals("1-3 years")){
                total += 3;
            } else if (paper.getYears().equals("3-5 years")){
                total += 5;
            } else {
                total += 7;
            }
            applys.setScores(total);
        }
        applysService.saveOrUpdate(applys);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        applysService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        applysService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(applysService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(applysService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Applys> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("scores");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRole().equals("ROLE_USER")) {
            queryWrapper.eq("userid", currentUser.getId());
        }
        if (currentUser.getRole().equals("ROLE_MANAGER")) {
            queryWrapper.eq("managerid", currentUser.getId());
        }
        Page<Applys> page = applysService.page(new Page<>(pageNum, pageSize), queryWrapper);
        for (Applys record : page.getRecords()) {
            Paper paper = paperService.getById(record.getPaperid());
            if (paper != null && StrUtil.isNotBlank(paper.getFile())) {
                record.setFile(paper.getFile());
            }
            record.setPaper(paper);
        }
        return Result.success(page);
    }

    /**
     * export
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // retrieve all data from the database
        List<Applys> list = applysService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // export objects within a list to Excel
        writer.write(list, true);

        // set browser response format
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Applys report", "UTF-8");
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
        List<Applys> list = reader.readAll(Applys.class);

        applysService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

