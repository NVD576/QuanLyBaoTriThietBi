/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Account;
import com.nvd.repository.AccountRepository;
import com.nvd.repository.CategoryRepository;
import com.nvd.service.AccountService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service("userDetailsService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public List<Account> getAccount() {
        return this.accountRepo.getAccount();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account u = this.getAccountByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return this.accountRepo.getAccountByUsername(username);
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.accountRepo.authenticate(username, password);
    }

}
