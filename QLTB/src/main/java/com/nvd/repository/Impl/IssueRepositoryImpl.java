/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Base;
import com.nvd.pojo.IncidentLevel;
import com.nvd.pojo.Issue;
import com.nvd.repository.IncidentLevelRepository;
import com.nvd.repository.IssueRepository;
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
public class IssueRepositoryImpl implements IssueRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private IncidentLevelRepository incidentLevelRepo;

    @Override
    public List<Issue> getIssues() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Issue ORDER BY id ASC", Issue.class);
        return q.getResultList();
    }

    @Override
    public Issue addOrUpdateIssue(Issue p) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new Issue: " + p);
                if (p.getDate() == null) {
                    p.setDate(new Date()); // Set ngày hiện tại nếu chưa nhập
                }
                if (p.getLevelId() == null) {
                    List<IncidentLevel> incidentLevels = this.incidentLevelRepo.getIncidentLevels(); // Hoặc service tương đương
                    if (!incidentLevels.isEmpty()) {
                        p.setLevelId(incidentLevels.get(0)); // Lấy incidentLevels đầu tiên làm mặc định
                    }
                }
                s.persist(p);
            } else {
                System.out.println("Updating incidentLevels with ID: " + p.getId());
                s.merge(p);
            }
            s.refresh(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public Issue getIssueById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Issue.class, id);
    }

    @Override
    public void deleteIssue(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Issue p = this.getIssueById(id);
        if (p != null) {
            s.remove(p);
        } else {
            throw new IllegalArgumentException("Device không tồn tại với id = " + id);
        }
    }
}
