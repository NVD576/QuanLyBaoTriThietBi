/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Base;
import com.nvd.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author admin
 */
@Controller
public class BaseControllers {

    @Autowired
    private BaseService baseService;

    @GetMapping({"/bases", "/base"})
    public String listOrEditBase(@RequestParam(name = "id", required = false) Integer id, Model model) {
        List<Base> bases = baseService.getBases();
        model.addAttribute("bases", bases);

        Base baseForm;
        if (id != null) {
            baseForm = baseService.getBaseById(id);
        } else {
            baseForm = new Base();
        }
        model.addAttribute("baseForm", baseForm);

        return "facility";
    }
    
    @PostMapping("/base/add")
    public String add (@ModelAttribute(value = "base") Base p, BindingResult result,
            Model model) {
        this.baseService.addOrUpdateBase(p);
        return "facility";
    }

    @DeleteMapping("/base/delete")
    public String destroy (@RequestParam(name = "id") Integer id, Model model) {
        if (id != null)
            this.baseService.deleteBase(id);
        
        return "facility";
    }
}
