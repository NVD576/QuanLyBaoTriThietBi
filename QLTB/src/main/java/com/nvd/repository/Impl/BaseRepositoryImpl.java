/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Base;
import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.repository.BaseRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Collections;
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
public class BaseRepositoryImpl implements BaseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Base> getBases() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Base ORDER BY id ASC", Base.class);
        List<Base> bases = q.getResultList();

        // Tùy chọn: load deviceSet cho mỗi base
        for (Base base : bases) {
            base.getDeviceSet().size(); // gọi để load lazy collection
            base.getAccountSet().size(); // gọi để load lazy collection
        }
        return q.getResultList();
    }

    @Override
    public Base getBaseById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Base.class, id);
    }

    @Override
    public List<Device> getDevicesByBaseId(int baseId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            CriteriaBuilder b = s.getCriteriaBuilder();
            CriteriaQuery<Device> q = b.createQuery(Device.class);
            Root root = q.from(Device.class);
            q.select(root);
            q.where(b.equal(root.get("baseId").as(Integer.class), baseId));

            Query query = s.createQuery(q);
            return query.getResultList();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

}
