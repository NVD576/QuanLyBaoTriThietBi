/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Base;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.MaintenanceType;
import com.nvd.repository.MaintenanceRepository;
import com.nvd.repository.MaintenanceTypeRepository;
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
public class MaintenanceTypeRepositoryImpl implements MaintenanceTypeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MaintenanceType> getMaintenanceTypes() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM MaintenanceType ORDER BY id ASC", MaintenanceType.class);
        return q.getResultList();
    }

    @Override
    public MaintenanceType getMaintenanceTypeById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(MaintenanceType.class, id);
    }
}
