/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class DeviceControllers {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/device")
    public String showDeviceForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            model.addAttribute("device", deviceService.getDeviceById(id));
        } else {
            model.addAttribute("device", new Device());
        }
        model.addAttribute("types", categoryService.getCates());
        model.addAttribute("statuses", statusService.getStatus());
        model.addAttribute("bases", baseService.getBases());
        return "devices-edit";
    }
    
    @PostMapping("/device/add")
    public String add(@ModelAttribute(value = "device") Device p, BindingResult result,
            Model model) {
        Device template = this.deviceService.addOrUpdateDevice(p);
        if (template != null) {
            Maintenance m = new Maintenance();
            this.maintenanceService.addNewDevice(m, p);
            return "redirect:/";
        }
        return "devices-edit";
    }

    @GetMapping("/device-detail/{id}")
    public String showDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("device", this.deviceService.getDeviceById(id));

        model.addAttribute("maintenances", this.deviceService.getMaintenancesByDeviceId(id));
        model.addAttribute("issues", this.deviceService.getIssuesByDeviceId(id));
        model.addAttribute("repairs", this.deviceService.getRepairsByDeviceId(id));
        return "device-details";
    }

//=================================================================
    @GetMapping("/device/{deviceId}/maintenaces")
    public String getMaintenacesByDeviceId(@PathVariable("deviceId") int id, Model model) {
        model.addAttribute("device", this.deviceService.getDeviceById(id));

        model.addAttribute("maintenances", this.deviceService.getMaintenancesByDeviceId(id));
        return "device-details";
    }

    @GetMapping("/device/{deviceId}/issues")
    public String getIssuesByDeviceId(@PathVariable("deviceId") int id, Model model) {
        model.addAttribute("device", this.deviceService.getDeviceById(id));

        model.addAttribute("issues", this.deviceService.getIssuesByDeviceId(id));
        return "device-details";
    }

    @GetMapping("/device/{deviceId}/repairs")
    public String getRepairsByDeviceId(@PathVariable("deviceId") int id, Model model) {
        model.addAttribute("device", this.deviceService.getDeviceById(id));

        model.addAttribute("repairs", this.deviceService.getRepairsByDeviceId(id));
        return "device-details";
    }
}
