/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.service.EquipmentService;
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
    private EquipmentService equipmentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Environment env;

    @ModelAttribute
    public void commonAttr(Model model){
        model.addAttribute("categories", this.categoryService.getCates());
    }
    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params, @RequestParam(value = "page", defaultValue = "1") int page) {

        model.addAttribute("equipments", this.equipmentService.getEquipments(params));
        int pageSize = Integer.parseInt(this.env.getProperty("PAGE_SIZE"));
        int count;

        // Đếm số sản phẩm tùy theo typeId
        if (params.containsKey("typeId")) {
            try {
                int typeId = Integer.parseInt(params.get("typeId"));
                count = this.equipmentService.countEquipmentByType(typeId);
            } catch (NumberFormatException ex) {
                count = this.equipmentService.countEquipment(); // fallback nếu typeId không hợp lệ
            }
        } else {
            count = this.equipmentService.countEquipment();
        }
        int totalPages = count > 0 ? (int) Math.ceil(count * 1.0 / pageSize) : 1;
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "index";
    }
}
