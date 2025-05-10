/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.service.AccountService;
import com.nvd.service.BaseService;
import com.nvd.service.CategoryService;
import com.nvd.service.DeviceService;
import com.nvd.service.MaintenanceService;
import com.nvd.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author ADMIN
 */
@Controller
public class DeviceControler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private BaseService baseService;
    
    @GetMapping("/devices")
    public String list(Model model) {
        model.addAttribute("device", new Device());
        model.addAttribute("types", this.categoryService.getCates());
        model.addAttribute("statuses", this.statusService.getStatus());
        model.addAttribute("bases", this.baseService.getBases());
        return "devices-edit";
    }

    @PostMapping("/device/add")
    public String add(@ModelAttribute(value = "device") Device p, BindingResult result,
                      Model model) {
        if(this.deviceService.addOrUpdateDevice(p) != null) {
            return "redirect:/";
        }
        return "devices-edit";
    }

    @GetMapping("/device/{id}")
    public String update (@PathVariable("id") int id, Model model) {
        model.addAttribute("device", this.deviceService.getDeviceById(id));
        // Truyền dữ liệu dropdown
        model.addAttribute("bases", this.baseService.getBases());
        model.addAttribute("types", this.categoryService.getCates());
        model.addAttribute("statuses", this.statusService.getStatus());
        return "devices-edit";
    }
    

}
