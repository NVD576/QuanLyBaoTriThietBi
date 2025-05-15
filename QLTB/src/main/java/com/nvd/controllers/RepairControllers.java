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
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showRepairForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Repair repair = (id != null) ? repairService.getRepairById(id) : new Repair();

        model.addAttribute("repair", repair);
        model.addAttribute("repairTypes", repairTypeService.getRepairTypes());
        model.addAttribute("devices", deviceService.getDevices(null));
        model.addAttribute("accounts", accountService.getAccount());

        return "repair-add";
    }

}
