package com.example.springboot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.config.interceptor.AuthAccess;
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

import com.example.springboot.service.ICategoryService;
import com.example.springboot.entity.Category;

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
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;
    @Resource
    IUserService userService;

    private final String now = DateUtil.now();

    // add or edit
    @PostMapping
    public Result save(@RequestBody Category category) {
        if (category.getId() == null) {
            //category.setTime(DateUtil.now());
            //category.setUser(TokenUtils.getCurrentUser().getNickname());
            //category.setUserid(TokenUtils.getCurrentUser().getId());
        }
        categoryService.saveOrUpdate(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        categoryService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        categoryService.removeByIds(ids);
        return Result.success();
    }

    @AuthAccess
    @GetMapping
    public Result findAll() {
        return Result.success(categoryService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(categoryService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        if (currentUser.getRole().equals("ROLE_USER")) {
//            queryWrapper.eq("userid", currentUser.getId());
//        }
        return Result.success(categoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * export
    */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // retrieve all data from the database
        List<Category> list = categoryService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // export objects within a list to Excel
        writer.write(list, true);

        // set browser response format
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Category table", "UTF-8");
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
        List<Category> list = reader.readAll(Category.class);

        categoryService.saveBatch(list);
        return Result.success();
    }

    private User getUser() {
        return TokenUtils.getCurrentUser();
    }

}

