/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.service.BaseService;
import com.nvd.service.DeviceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class BaseControllers {

    @Autowired
    private BaseService baseService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/base")
    public String list(Model model) {
        model.addAttribute("bases", this.baseService.getBases());
        return "base-details";
    }

}
