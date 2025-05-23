/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Base;
import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import com.nvd.pojo.Status;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nvd.repository.DeviceRepository;
import com.nvd.repository.StatusRepository;
import jakarta.persistence.criteria.Join;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class DeviceRepositoryImpl implements DeviceRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;
    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Device> getDevices(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Device> q = b.createQuery(Device.class);
        Root<Device> root = q.from(Device.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String typeId = params.get("cateId");
            if (typeId != null && !typeId.isEmpty()) {
                // Nếu device_type là ManyToOne
                predicates.add(b.equal(root.get("categoryId").get("id"), Integer.parseInt(typeId)));
            }

            // Lọc theo baseId (Base đối tượng)
            String baseId = params.get("baseId");
            if (baseId != null && !baseId.isEmpty()) {
                // Lọc theo baseId, Base là đối tượng, nên chúng ta cần join với đối tượng Base
                Join<Device, Base> baseJoin = root.join("baseId"); // baseId là tên thuộc tính trong entity Device
                predicates.add(b.equal(baseJoin.get("id"), Integer.parseInt(baseId)));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        q.orderBy(b.asc(root.get("id")));

        Query query = session.createQuery(q);

        if (params != null) {
            String p = params.get("page");
            if (p != null && !p.isEmpty()) {
                int page = Integer.parseInt(p);
                int pageSize = Integer.parseInt(this.env.getProperty("PAGE_SIZE"));
                query.setMaxResults(pageSize);
                query.setFirstResult((page - 1) * pageSize);
            }
        }

        return query.getResultList();
    }

    @Override
    public int countDevice() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("Select Count(*) From Device");
        return Integer.parseInt(q.getSingleResult().toString());
    }

    @Override
    public int countDeviceByType(int cateId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(*) FROM Device e WHERE e.categoryId.id = :cateId");
        q.setParameter("cateId", cateId);
        return Integer.parseInt(q.getSingleResult().toString());
    }
    
    @Override
    public int countDeviceByConditions(String kw, Integer baseId, Integer cateId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(*) FROM Device d WHERE 1=1";

        if (kw != null && !kw.trim().isEmpty()) {
            hql += " AND LOWER(d.name) LIKE :kw";
        }

        if (baseId != null) {
            hql += " AND d.baseId.id = :baseId";
        }

        if (cateId != null) {
            hql += " AND d.categoryId.id = :cateId";
        }

        Query q = s.createQuery(hql);

        if (kw != null && !kw.trim().isEmpty()) {
            q.setParameter("kw", "%" + kw.toLowerCase().trim() + "%");
        }

        if (baseId != null) {
            q.setParameter("baseId", baseId);
        }

        if (cateId != null) {
            q.setParameter("cateId", cateId);
        }

        return Integer.parseInt(q.getSingleResult().toString());
    }

    
    @Override
    public Device getDeviceById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Device.class, id);

    }

    @Override
    @Transactional
    public Device addOrUpdateDevice(Device p) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new device: " + p);
                if (p.getDate() == null) {
                    p.setDate(new Date()); // Set ngày hiện tại nếu chưa nhập
                }
                if (p.getStatusId() == null) {
                    List<Status> statuses = this.statusRepository.getStatus(); // Hoặc service tương đương
                    if (!statuses.isEmpty()) {
                        p.setStatusId(statuses.get(0)); // Lấy status đầu tiên làm mặc định
                    }
                }
                s.persist(p);
            } else {
                System.out.println("Updating device with ID: " + p.getId());
                s.merge(p);
            }
            s.refresh(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public void deleteDevice(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Device p = this.getDeviceById(id);
        if (p != null) {
            s.remove(p);
        } else {
            throw new IllegalArgumentException("Device không tồn tại với id = " + id);
        }
    }

    @Override
    public List<Maintenance> getMaintenancesByDeviceId(int deviceId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Maintenance> q = b.createQuery(Maintenance.class);
            Root root = q.from(Maintenance.class);
            q.select(root);
            q.where(b.equal(root.get("deviceId").as(Integer.class), deviceId));

            Query query = s.createQuery(q);
            return query.getResultList();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Issue> getIssuesByDeviceId(int deviceId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Issue> q = b.createQuery(Issue.class);
            Root root = q.from(Issue.class);
            q.select(root);
            q.where(b.equal(root.get("deviceId").as(Integer.class), deviceId));

            Query query = s.createQuery(q);
            return query.getResultList();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Repair> getRepairsByDeviceId(int deviceId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Repair> q = b.createQuery(Repair.class);
            Root root = q.from(Repair.class);
            q.select(root);
            q.where(b.equal(root.get("deviceId").as(Integer.class), deviceId));

            Query query = s.createQuery(q);
            return query.getResultList();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
