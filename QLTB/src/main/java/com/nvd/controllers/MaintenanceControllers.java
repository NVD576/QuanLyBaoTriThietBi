/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import com.nvd.service.AccountService;
import com.nvd.service.DeviceService;
import com.nvd.service.FrequencyService;
import com.nvd.service.MaintenanceService;
import com.nvd.service.MaintenanceTypeService;
import com.nvd.service.RepairService;
import com.nvd.service.RepairTypeService;
import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestParam;

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
    private RepairTypeService repairTypeService;
    @Autowired
    private FrequencyService frequencyService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RepairService repairService;
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/maintenances")
    public String show(Model model) {
        // Truyền dữ liệu dropdown
        model.addAttribute("maintenances", this.maintenanceService.getMaintenances());
        model.addAttribute("accounts", accountService.getAccount());
        return "maintenances";
    }

    @PostMapping("/maintenance/add")
    public String add(@ModelAttribute(value = "maintenance") Maintenance p, BindingResult result,
            Model model) {
        if (this.maintenanceService.addOrUpdateMaintenance(p) != null) {
            return "redirect:/maintenances";
        }
        return "maintenance-add";
    }

    @GetMapping("/maintenance")
    public String update(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        model.addAttribute("types", this.maintenanceTypeService.getMaintenanceTypes());
        model.addAttribute("frequencies", this.frequencyService.getFrequency());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        return "maintenance-add";
    }

    @GetMapping("/maintenance/Device/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        Maintenance m = new Maintenance();
        Device device = deviceService.getDeviceById(id);
        m.setDeviceId(device);  // Gán thiết bị cụ thể vào đối tượng maintenance

        model.addAttribute("maintenance", m);
        model.addAttribute("device", device);
        model.addAttribute("frequencies", frequencyService.getFrequency());
        model.addAttribute("types", maintenanceTypeService.getMaintenanceTypes());
        return "maintenance-add";
    }

    @GetMapping("/maintenance/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("maintenance", this.maintenanceService.getMaintenanceById(id));
        model.addAttribute("devices", this.deviceService.getDevices(null));
        model.addAttribute("frequencies", frequencyService.getFrequency());
        model.addAttribute("types", maintenanceTypeService.getMaintenanceTypes());
        return "maintenance-add";
    }

    @PostMapping("/maintenance/confirm/{id}")
    public String confirmMaintenance(@PathVariable("id") int id,
            @RequestParam("cost") BigDecimal cost,
            @RequestParam("accountId") int accountId) {
        Maintenance maintenance = this.maintenanceService.getMaintenanceById(id);
        Repair repair = new Repair();
        repairService.addNewMaintenancyOrIssue(repair, cost, maintenance.getDeviceId(), this.repairTypeService.getTypeById(1), accountId);

        return "redirect:/maintenances";
    }

}
