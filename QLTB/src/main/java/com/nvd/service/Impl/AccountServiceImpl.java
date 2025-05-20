/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvd.pojo.Account;
import com.nvd.repository.AccountRepository;
import com.nvd.repository.CategoryRepository;
import com.nvd.service.AccountService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private Cloudinary cloudinary;

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

    @Override
    public Account addAccount(Map<String, String> params, MultipartFile avatar) {
        Account u = new Account();
        u.setName(params.get("name"));
        u.setEmail(params.get("email"));
        u.setUsername(params.get("username"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setRole("ROLE_USER");

        if (!avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(DeviceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.accountRepo.addAccount(u);
    }

    @Override
    public Account getAccountById(int id) {
        return this.accountRepo.getAccountById(id);
    }

    @Override
    public Account addOrUpdateAccount(Account acc) {
        Account currentAcc = null;

        if (acc.getId() != null) {
            currentAcc = this.getAccountById(acc.getId());
        }
        if (acc.getId() == null) {
            // Trường hợp thêm mới: mã hóa password bắt buộc
            acc.setPassword(this.passwordEncoder.encode(acc.getPassword()));
        } else {
            // Trường hợp update: kiểm tra nếu password khác null và khác rỗng, encode lại
            if (acc.getPassword() != null && !acc.getPassword().isEmpty()) {
                // Có thể kiểm tra nếu khác mật khẩu hiện tại mới encode (tùy logic)
                acc.setPassword(this.passwordEncoder.encode(acc.getPassword()));
            } else {
                // Giữ nguyên password cũ
                acc.setPassword(currentAcc.getPassword());
            }
        }

        if (acc.getFile() != null && !acc.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(acc.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                acc.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            acc.setAvatar(currentAcc.getAvatar());
        }
        return this.accountRepo.addOrUpdateAccount(acc);
    }

    @Override
    public void deleteAccount(int id) {
        this.accountRepo.deleteAccount(id);
    }
}
