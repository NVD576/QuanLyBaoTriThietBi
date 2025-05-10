/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.MaintenanceType;
import com.nvd.pojo.Status;
import com.nvd.repository.MaintenanceRepository;
import com.nvd.repository.MaintenanceTypeRepository;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class MaintenanceRepositoryImpl implements MaintenanceRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    @Override
    public List<Maintenance> getMaintenances() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Maintenance ORDER BY id ASC", Maintenance.class);
        return q.getResultList();
    }

    @Override
    public Maintenance getMaintenanceById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    @Transactional
    public Maintenance addOrUpdateMaintenance(Maintenance p) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new Maintenance: " + p);
                if (p.getDate() == null) {
                    p.setDate(new Date()); // Set ngày hiện tại nếu chưa nhập
                }
                if (p.getTypeId()== null) {
                    List<MaintenanceType> types = this.maintenanceTypeRepository.getMaintenanceTypes(); // Hoặc service tương đương
                    if (!types.isEmpty()) {
                        p.setTypeId(types.get(0)); // Lấy status đầu tiên làm mặc định
                    }
                }
                s.persist(p);
            } else {
                System.out.println("Updating Maintenance with ID: " + p.getId());
                s.merge(p);
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        s.refresh(p);
        return p;
    }



}
