/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.IncidentLevel;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Repair;
import com.nvd.repository.RepairRepository;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
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
public class RepairRepositoryImpl implements RepairRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Repair> getRepairs() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Repair ORDER BY id ASC", Repair.class);
        return q.getResultList();
    }

    @Override
    public Repair getRepairById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Repair.class, id);
    }

    @Override
    public Repair addOrUpdateRepair(Repair p) {
       Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new Repair: " + p);
                if (p.getDate() == null) {
                    p.setDate(new Date()); // Set ngày hiện tại nếu chưa nhập
                }

                s.persist(p);
            } else {
                System.out.println("Updating Repair with ID: " + p.getId());
                s.merge(p);
            }
            s.refresh(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return p;}

}
