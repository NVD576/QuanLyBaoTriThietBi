/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Account;
import com.nvd.repository.AccountRepository;
import com.nvd.repository.CategoryRepository;
import com.nvd.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepo;
    
    @Override
    public List<Account> getAccount() {
        return this.accountRepo.getAccount();
    }
    
}
