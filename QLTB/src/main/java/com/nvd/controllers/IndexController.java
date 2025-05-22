/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.service.BaseService;
import com.nvd.service.DeviceService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.nvd.service.CategoryService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author ADMIN
 */
@Controller
@ControllerAdvice
@PropertySource("classpath:configs.properties")
public class IndexController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Environment env;
    @Autowired
    private BaseService baseService;

    @ModelAttribute
    public void commonAttr(Model model) {
        model.addAttribute("categories", this.categoryService.getCates());
        model.addAttribute("bases", this.baseService.getBases());
    }

    @RequestMapping("/")
    public String index(Model model,
            @RequestParam Map<String, String> params,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        // Trích xuất các điều kiện lọc (nếu có)
        String keyword = params.get("kw") != null ? params.get("kw").trim() : "";
        Integer baseId = null;
        Integer cateId = null;

        try {
            if (params.get("baseId") != null) {
                baseId = Integer.parseInt(params.get("baseId"));
            }
        } catch (NumberFormatException ex) {
            baseId = null;
        }

        try {
            if (params.get("cateId") != null) {
                cateId = Integer.parseInt(params.get("cateId"));
            }
        } catch (NumberFormatException ex) {
            cateId = null;
        }

        // Gửi các biến lọc về view để giữ giá trị trong form và link phân trang
        model.addAttribute("kw", keyword);
        model.addAttribute("baseId", baseId);
        model.addAttribute("cateId", cateId);

        // Gửi danh sách thiết bị theo điều kiện
        model.addAttribute("devices", this.deviceService.getDevices(params));

        // Lấy page size từ cấu hình
        int pageSize = Integer.parseInt(this.env.getProperty("PAGE_SIZE"));

        // Gọi hàm đếm tổng số device theo điều kiện (hàm bạn đã combine)
        int count = this.deviceService.countDeviceByConditions(keyword, baseId, cateId);

        // Tính tổng số trang
        int totalPages = count > 0 ? (int) Math.ceil(count * 1.0 / pageSize) : 1;

        // Gửi dữ liệu phân trang về view
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "index";
    }

}
