/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Account;
import com.nvd.service.AccountService;
import com.nvd.utils.JwtUtils;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiAccountControllers {

    @Autowired
    private AccountService userDetailsService;

    @PostMapping(path = "/accounts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> create(@RequestParam Map<String, String> params, @RequestParam(value = "avatar") MultipartFile avatar) {
        return new ResponseEntity<>(this.userDetailsService.addAccount(params, avatar), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account u) {

        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    public ResponseEntity<Account> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userDetailsService.getAccountByUsername(principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/account/edit")
    public ResponseEntity<Account> edit(
            @RequestParam(value = "id") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        Account p = this.userDetailsService.getAccountById(id);
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (name != null && !name.trim().isEmpty()) {
            p.setName(name.trim());
        }
        if (email != null && !email.trim().isEmpty()) {
            p.setEmail(email.trim());
        }
        if (avatar != null && !avatar.isEmpty()) {
            p.setFile(avatar);
        }

        Account updated = this.userDetailsService.addOrUpdateAccount(p);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
