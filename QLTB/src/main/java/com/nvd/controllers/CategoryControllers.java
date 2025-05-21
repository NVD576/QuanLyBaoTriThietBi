/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Base;
import com.nvd.pojo.Category;
import com.nvd.service.CategoryService;
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
 * @author ADMIN
 */
@Controller
public class CategoryControllers {

    @Autowired
    private CategoryService cateSer;

    @GetMapping("/categories")
    public String listOrEdit(@RequestParam(name = "id", required = false) Integer id, Model model) {
        List<Category> categories = cateSer.getCates();
        model.addAttribute("categories", categories);
        Category cateForm;
        if (id != null) {
            cateForm = cateSer.getCategotryById(id);
        } else {
            cateForm = new Category();
        }
        model.addAttribute("categoriesForm", cateForm);

        return "categories";
    }
    
    @PostMapping("/category/add")
    public String add (@ModelAttribute(value = "cate") Category p, BindingResult result,
            Model model) {
        this.cateSer.addOrUpdateCategory(p);
        return "redirect:/categories";
    }

    @DeleteMapping("/cate/delete")
    public String destroy (@RequestParam(name = "id") Integer id, Model model) {
        if (id != null)
            this.cateSer.deleteCategory(id);
        
        return "categories";
    }

}
