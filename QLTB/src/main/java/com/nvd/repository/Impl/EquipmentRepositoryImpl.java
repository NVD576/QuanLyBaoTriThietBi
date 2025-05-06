/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Equipment;
import com.nvd.repository.EquipmentRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
public class EquipmentRepositoryImpl implements EquipmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private Environment env;

    @Override
    public List<Equipment> getEquipments(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Equipment> q = b.createQuery(Equipment.class);
        Root<Equipment> root = q.from(Equipment.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String typeId = params.get("typeId");
            if (typeId != null && !typeId.isEmpty()) {
                // Nếu equipment_type là ManyToOne
                predicates.add(b.equal(root.get("typeId").get("id"), Integer.parseInt(typeId)));
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
    public int countEquipment() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("Select Count(*) From Equipment");
        return Integer.parseInt(q.getSingleResult().toString());
    }

    @Override
    public int countEquipmentByType(int typeId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(*) FROM Equipment e WHERE e.typeId.id = :typeId");
        q.setParameter("typeId", typeId);
        return Integer.parseInt(q.getSingleResult().toString());
    }

    @Override
    public Equipment getEquipmentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Equipment.class, id);

    }
}
