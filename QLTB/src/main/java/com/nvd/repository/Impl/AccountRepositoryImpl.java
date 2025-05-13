/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Account;
import com.nvd.pojo.Category;
import com.nvd.repository.AccountRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public List<Account> getAccount() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Account ORDER BY id ASC", Account.class);
        return q.getResultList();
    }

    @Override
    public Account getAccountByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Account.findByUsername", Account.class);
        q.setParameter("username", username);

        return (Account) q.getSingleResult();
    }

    @Override
    public boolean authenticate(String username, String password) {
        Account u = this.getAccountByUsername(username);

        return this.passwordEncoder.matches(password, u.getPassword());
    }

}
