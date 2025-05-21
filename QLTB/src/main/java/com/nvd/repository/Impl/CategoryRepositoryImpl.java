/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Base;
import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.nvd.repository.CategoryRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Collections;
import org.hibernate.HibernateException;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Category> getCates() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Category ORDER BY id ASC", Category.class);
        return q.getResultList();
    }

    @Override
    public List<Device> getDevicesByCateId(int categoryId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Device> q = b.createQuery(Device.class);
            Root root = q.from(Device.class);
            q.select(root);
            q.where(b.equal(root.get("categoryId").as(Integer.class), categoryId));

            Query query = s.createQuery(q);
            return query.getResultList();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Category getCategotryById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Category.class, id);
    }

    @Override
    public Category addOrUpdateCategory(Category p) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new base: " + p);
                if (p.getName() == null || p.getName().isEmpty()) {
                    p.setName("Cơ sở TpHCM");
                }

                s.persist(p);
            } else {
                System.out.println("Updating base with ID: " + p.getId());
                s.merge(p);
            }
            s.refresh(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public void deleteCategory(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Category p = this.getCategotryById(id);
        if (p != null) {
            s.remove(p);
        } else {
            throw new IllegalArgumentException("Device không tồn tại với id = " + id);
        }
    }

}
