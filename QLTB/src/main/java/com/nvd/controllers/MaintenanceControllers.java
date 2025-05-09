/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
import com.nvd.service.DeviceService;
import com.nvd.service.FrequencyService;
import com.nvd.service.MaintenanceService;
import com.nvd.service.MaintenanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author ADMIN
 */
@Controller
@ControllerAdvice
@PropertySource("classpath:configs.properties")
public class MaintenanceControllers {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;
    
    @Autowired
    private FrequencyService frequencyService;
    
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping("/maintenances")
    public String show(Model model) {
        // Truyền dữ liệu dropdown
        model.addAttribute("maintenances", this.maintenanceService.getMaintenances());
        return "maintenances";
    }

    
    
    @PostMapping("/maintenance/add")
    public String add(@ModelAttribute(value = "maintenance") Maintenance p, BindingResult result,
            Model model) {
        if (this.maintenanceService.addOrUpdateMaintenance(p) != null) {
            return "redirect:/maintenance";
        }
        return "maintenance-add";
    }

    @GetMapping("/maintenance")
    public String update(Model model){
        model.addAttribute("maintenance", new Maintenance());
        model.addAttribute("types", this.maintenanceTypeService.getMaintenanceTypes());
        model.addAttribute("frequencies", this.frequencyService.getFrequency());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        return "maintenance-add";
    }
}
