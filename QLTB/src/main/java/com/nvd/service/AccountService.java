/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Account;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
public interface AccountService extends UserDetailsService{
    List<Account> getAccount();
    Account getAccountByUsername(String username);
}
