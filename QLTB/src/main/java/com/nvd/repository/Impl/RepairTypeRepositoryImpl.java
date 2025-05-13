/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.RepairType;
import com.nvd.repository.RepairTypeRepository;
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
public class RepairTypeRepositoryImpl implements RepairTypeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<RepairType> getRepairTypes() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM RepairType ORDER BY id ASC", RepairType.class);
        return q.getResultList();
    }

    @Override
    public RepairType getTypeById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(RepairType.class, id);
    }
}
