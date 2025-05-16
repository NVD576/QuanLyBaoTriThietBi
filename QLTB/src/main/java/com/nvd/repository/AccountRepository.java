/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Account;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface AccountRepository {
    List<Account> getAccount();
    Account getAccountById(int id);
    Account getAccountByUsername(String username);
    boolean authenticate(String username, String password);
    Account addAccount(Account u);
    Account addOrUpdateAccount(Account acc);
}
