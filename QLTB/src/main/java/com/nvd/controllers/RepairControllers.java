/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Repair;
import com.nvd.service.AccountService;
import com.nvd.service.DeviceService;
import com.nvd.service.RepairService;
import com.nvd.service.RepairTypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RepairControllers {

    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairTypeService repairTypeService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/repairs")
    public String Show(Model model) {
        model.addAttribute("repairs", this.repairService.getRepairs());
        return "repairs";
    }

    @PostMapping("/repair/add")
    public String add(@ModelAttribute(value = "repair") Repair p, BindingResult result,
            Model model) {
        if (this.repairService.addOrUpdateRepair(p) != null) {
            return "redirect:/repairs";
        }
        return "repair-add";
    }

    @GetMapping("/repair")
    public String insert(Model model) {
        model.addAttribute("repair", new Repair());
        model.addAttribute("repairTypes", this.repairTypeService.getRepairTypes());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        model.addAttribute("accounts", this.accountService.getAccount());
        return "repair-add";
    }

    @GetMapping("/repair/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("repair", this.repairService.getRepairById(id));
         model.addAttribute("repairTypes", this.repairTypeService.getRepairTypes());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        model.addAttribute("accounts", this.accountService.getAccount());
        return "repair-add";
    }
}
