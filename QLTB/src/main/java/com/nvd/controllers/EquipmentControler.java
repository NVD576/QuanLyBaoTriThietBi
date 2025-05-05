/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Account;
import com.nvd.pojo.Equipment;
import com.nvd.service.AccountService;
import com.nvd.service.CategoryService;
import com.nvd.service.EquipmentService;
import com.nvd.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author ADMIN
 */
@Controller
public class EquipmentControler {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/equipments")
    public String list(Model model) {
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("types", this.categoryService.getCates());
        model.addAttribute("status", this.statusService.getStatus());
        return "equipments";
    }

    @GetMapping("/equipment/{id}")
    public String getEquipmentDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("equipment", this.equipmentService.getEquipmentById(id));
        // Truyền dữ liệu dropdown
        model.addAttribute("types", this.categoryService.getCates());
        model.addAttribute("statuses", this.statusService.getStatus());
        model.addAttribute("accounts", this.accountService.getAccount());
        return "equipment-detail";
    }

}
