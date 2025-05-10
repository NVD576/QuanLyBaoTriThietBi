/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Frequency;
import com.nvd.repository.FrequencyRepository;
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
public class FrequencyRepositoryImpl implements FrequencyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Frequency> getFrequencys() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Frequency ORDER BY id ASC", Frequency.class);
        return q.getResultList();
    }

}
