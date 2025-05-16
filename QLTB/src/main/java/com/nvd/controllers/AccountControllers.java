/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Account;
import com.nvd.service.AccountService;
import com.nvd.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ADMIN
 */
@Controller
public class AccountControllers {

    @Autowired
    private AccountService accountService;
    @Autowired
    private BaseService baseService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/accounts")
    public String showAccountForm(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id != null) {
            model.addAttribute("account", accountService.getAccountById(id));
        } else {
            model.addAttribute("account", new Account());
        }
        model.addAttribute("accounts", accountService.getAccount());
        model.addAttribute("bases", baseService.getBases());
        return "accounts";
    }

    @PostMapping("/account-add")
    public String addOrUpdateAccount(@ModelAttribute("account") Account a) {
        this.accountService.addOrUpdateAccount(a);
        return "redirect:/accounts"; // quay về trang danh sách tài khoản
    }

}
