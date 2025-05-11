/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.pojo.Frequency;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.MaintenanceType;
import com.nvd.pojo.Status;
import com.nvd.repository.MaintenanceRepository;
import com.nvd.repository.MaintenanceTypeRepository;
import com.nvd.service.FrequencyService;
import com.nvd.service.MaintenanceTypeService;
import jakarta.persistence.Query;
import java.util.Calendar;
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
    @Autowired
    private MaintenanceTypeService maintenanceTypeService;
    @Autowired
    private FrequencyService frequencyService;

    @Override
    public List<Maintenance> getMaintenances() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Maintenance ORDER BY id ASC", Maintenance.class);
        return q.getResultList();
    }

    @Override
    public Maintenance getMaintenanceById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Maintenance.class, id);
    }

    @Override
    @Transactional
    public Maintenance addOrUpdateMaintenance(Maintenance p) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new Maintenance: " + p);

                if (p.getTypeId() == null) {
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

    @Override
    public List<Maintenance> getByDeviceId(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery(
                "FROM Maintenance m WHERE m.deviceId.id = :deviceId ORDER BY m.id ASC",
                Maintenance.class
        );
        q.setParameter("deviceId", id);
        return q.getResultList();
    }

    @Override
    @Transactional
    public Maintenance addNewDevice(Maintenance p, Device d) {
        Session s = this.factory.getObject().getCurrentSession();

        try {
            // Gán deviceId
            p.setDeviceId(d);

            // Tính ngày bảo trì là 30 ngày sau ngày mua/thiết lập thiết bị
            if (d.getDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d.getDate());
                cal.add(Calendar.DATE, 30); // cộng 30 ngày
                p.setDate(cal.getTime());
            }

            // Gán frequencyId mặc định
            List<Frequency> frequencies = frequencyService.getFrequency();
            if (!frequencies.isEmpty()) {
                p.setFrequencyId(frequencies.get(0));
            }

            // Gán typeId mặc định
            List<MaintenanceType> types = maintenanceTypeService.getMaintenanceTypes();
            if (!types.isEmpty()) {
                p.setTypeId(types.get(0));
            }

            // Lưu bản ghi bảo trì mới
            s.persist(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        s.refresh(p);
        return p;
    }
}
