/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Account;
import com.nvd.pojo.Base;
import com.nvd.pojo.Category;
import com.nvd.repository.AccountRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.HibernateException;
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

    @Override
    public Account addAccount(Account u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        return u;
    }

    @Override
    public Account getAccountById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Account.class, id);
    }

    @Override
    public Account addOrUpdateAccount(Account acc) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            if (acc.getId() == null) {
                System.out.println("Saving new account: " + acc);

                // Mặc định nếu có thể
                if (acc.getRole() == null || acc.getRole().isEmpty()) {
                    acc.setRole("ROLE_USER"); // Hoặc vai trò mặc định khác
                }

                // Gán base nếu chỉ có id (trong form submit về thường chỉ có baseId.id)
                if (acc.getBaseId() != null && acc.getBaseId().getId() != null) {
                    Base base = session.get(Base.class, acc.getBaseId().getId());
                    acc.setBaseId(base);
                }

                // Mã hóa mật khẩu nếu cần (nếu bạn dùng encode)
                // acc.setPassword(passwordEncoder.encode(acc.getPassword()));
                session.persist(acc);
            } else {
                System.out.println("Updating account with ID: " + acc.getId());

                if (acc.getBaseId() != null && acc.getBaseId().getId() != null) {
                    Base base = session.get(Base.class, acc.getBaseId().getId());
                    acc.setBaseId(base);
                }
                session.merge(acc);
            }
            session.refresh(acc);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return acc;
    }

}
