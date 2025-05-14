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
    public String showMaintenanceForm(Model model,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "deviceId", required = false) Integer deviceId) {

        Maintenance maintenance;
        if (id != null) { // Trường hợp chỉnh sửa
            maintenance = maintenanceService.getMaintenanceById(id);
            model.addAttribute("devices", deviceService.getDevices(null)); // Cho phép chọn lại device nếu cần
        } else { // Trường hợp thêm mới
            maintenance = new Maintenance();
            if (deviceId != null) {
                Device device = deviceService.getDeviceById(deviceId);
                maintenance.setDeviceId(device);
                model.addAttribute("device", device); // Nếu muốn hiển thị riêng thiết bị được chọn
            } else {
                model.addAttribute("devices", deviceService.getDevices(null)); // Cho phép chọn thiết bị
            }
        }

        model.addAttribute("maintenance", maintenance);
        model.addAttribute("types", maintenanceTypeService.getMaintenanceTypes());
        model.addAttribute("frequencies", frequencyService.getFrequency());

        return "maintenance-add";
    }

    @PostMapping("/maintenance/{id}/repair/add")
    public String confirmMaintenance(@PathVariable("id") int id,
            @RequestParam("cost") BigDecimal cost,
            @RequestParam("accountId") int accountId) {
        Maintenance maintenance = this.maintenanceService.getMaintenanceById(id);
        Repair repair = new Repair();
        repairService.addNewMaintenancyOrIssue(repair, cost, maintenance.getDeviceId(), this.repairTypeService.getTypeById(1), accountId);

        return "redirect:/maintenances";
    }

}
