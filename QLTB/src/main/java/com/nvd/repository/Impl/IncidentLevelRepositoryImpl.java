/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.IncidentLevel;
import com.nvd.repository.IncidentLevelRepository;
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
public class IncidentLevelRepositoryImpl implements IncidentLevelRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<IncidentLevel> getIncidentLevels() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM IncidentLevel ORDER BY id ASC", IncidentLevel.class);
        return q.getResultList();
    }

}
