/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Status;
import com.nvd.repository.StatusRepository;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class StatusRepositoryImpl implements StatusRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @Override
    public List<Status> getStatus() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Status ORDER BY id ASC", Status.class);
        return q.getResultList();
    }

    @Override
    public Status getStatusById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Status.class, id);
    }

}
