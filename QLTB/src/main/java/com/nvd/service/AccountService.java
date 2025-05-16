/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Account;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
public interface AccountService extends UserDetailsService{
    List<Account> getAccount();
    Account getAccountById(int id);
    Account getAccountByUsername(String username);
    boolean authenticate(String username, String password);
    Account addAccount(Map<String, String> params, MultipartFile avatar);
    Account addOrUpdateAccount(Account acc);
}
