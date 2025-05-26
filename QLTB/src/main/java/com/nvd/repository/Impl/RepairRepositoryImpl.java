/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.repository.Impl;

import com.nvd.pojo.Account;
import com.nvd.pojo.Device;
import com.nvd.pojo.IncidentLevel;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import com.nvd.repository.RepairRepository;
import com.nvd.service.AccountService;
import com.nvd.service.RepairTypeService;
import jakarta.persistence.Query;
import java.math.BigDecimal;
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
    @Autowired
    private RepairTypeService repairTypeService;
    @Autowired
    private AccountService accountService;

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
        return p;
    }

    @Override
    public Repair addNewMaintenancyOrIssue(Repair p, BigDecimal cost, Device deviceID, RepairType repairTypeId, int accountId) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            if (p.getId() == null) {
                System.out.println("Saving new Repair: " + p);
                if (p.getDate() == null) {
                    p.setDate(new Date()); // Set ngày hiện tại nếu chưa nhập
                }
                p.setCost(cost);
                p.setDeviceId(deviceID);
                p.setTypeId(repairTypeId);
                p.setAccountId(accountService.getAccountById(accountId));
                s.persist(p);
            }
            s.refresh(p);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    @Override
    public void deleteRepair(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Repair p = this.getRepairById(id);
        if (p != null) {
            s.remove(p);
        } else {
            throw new IllegalArgumentException("Device không tồn tại với id = " + id);
        }
    }

    @Override
    public BigDecimal getTotalRepairCost(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COALESCE(SUM(r.cost), 0) FROM Repair r WHERE 1=1";

        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        Query q = s.createQuery(hql);

        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        return (BigDecimal) q.getSingleResult();
    }

    @Override
    public long countRepairs(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(r) FROM Repair r WHERE 1=1";

        // Thêm điều kiện tương tự getTotalRepairCost
        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        Query q = s.createQuery(hql);

        // Set parameters tương tự
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        return (long) q.getSingleResult();
    }

    @Override
    public Device getMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r.deviceId, COUNT(r) as repairCount FROM Repair r WHERE 1=1";

        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        hql += " GROUP BY r.deviceId ORDER BY repairCount DESC";

        Query q = s.createQuery(hql);
        q.setMaxResults(1);

        // Set parameters
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        List<Object[]> results = q.getResultList();

        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            return (Device) result[0];
        }

        return null; // Không tìm thấy thiết bị nào
    }

    @Override
    public RepairType getMostCommonRepairType(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r.typeId, COUNT(r) as typeCount FROM Repair r WHERE 1=1";

        // Thêm điều kiện
        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        hql += " GROUP BY r.typeId ORDER BY typeCount DESC";

        Query q = s.createQuery(hql);
        q.setMaxResults(1);

        // Set parameters
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        List<Object[]> results = q.getResultList();

        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            return (RepairType) result[0];
        }

        return null; // Không có loại sửa chữa phổ biến nào
    }

    @Override
    public List<Object[]> getRepairCostStatsByTimeRange(Date startDate, Date endDate, Integer categoryId, Integer statusId, String timeRange) {
        Session s = this.factory.getObject().getCurrentSession();
        String dateFormat;

        switch (timeRange.toLowerCase()) {
            case "day":
                dateFormat = "%Y-%m-%d";
                break;
            case "week":
                dateFormat = "%x-W%v";  // Tuần ISO trong MySQL
                break;
            case "month":
                dateFormat = "%Y-%m";
                break;
            case "year":
                dateFormat = "%Y";
                break;
            default:
                dateFormat = "%Y-%m";
        }

        String sql = "SELECT DATE_FORMAT(r.date, :dateFormat) AS timePeriod, COALESCE(SUM(r.cost), 0) "
                + "FROM repair r WHERE 1=1";

        if (startDate != null) {
            sql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            sql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            sql += " AND r.device_id IN (SELECT d.id FROM device d WHERE d.category_id = :categoryId)";
        }
        if (statusId != null) {
            sql += " AND r.device_id IN (SELECT d.id FROM device d WHERE d.status_id = :statusId)";
        }

        sql += " GROUP BY timePeriod ORDER BY timePeriod";

        Query q = s.createNativeQuery(sql);

        q.setParameter("dateFormat", dateFormat);
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        return q.getResultList();
    }

    @Override
    public List<Object[]> getRepairTypeStats(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r.typeId, COUNT(r), COALESCE(SUM(r.cost), 0) "
                + "FROM Repair r WHERE 1=1";

        // Thêm điều kiện
        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        hql += " GROUP BY r.typeId";

        Query q = s.createQuery(hql);

        // Set parameters
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null) {
            q.setParameter("statusId", statusId);
        }

        return q.getResultList();
    }

    @Override
    public List<Object[]> getDeviceRepairStats(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT r.deviceId, r.deviceId.categoryId, r.deviceId.statusId, "
                + "COUNT(r), COALESCE(SUM(r.cost), 0), MAX(r.date) "
                + "FROM Repair r WHERE 1=1";

        // Thêm điều kiện
        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }
        if (categoryId != null && categoryId != 0) {
            hql += " AND r.deviceId.categoryId.id = :categoryId";
        }
        if (statusId != null && statusId != 0) {
            hql += " AND r.deviceId.statusId.id = :statusId";
        }

        hql += " GROUP BY r.deviceId, r.deviceId.categoryId, r.deviceId.statusId "
                + "ORDER BY COUNT(r) DESC";

        Query q = s.createQuery(hql);

        // Set parameters
        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }
        if (categoryId != null && categoryId != 0) {
            q.setParameter("categoryId", categoryId);
        }
        if (statusId != null && statusId != 0) {
            q.setParameter("statusId", statusId);
        }

        return q.getResultList();
    }

    @Override
    public List<Repair> getRepairsByDevice(int deviceId, Date startDate, Date endDate) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM Repair r WHERE r.deviceId.id = :deviceId";

        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }

        hql += " ORDER BY r.date DESC";

        Query q = s.createQuery(hql);
        q.setParameter("deviceId", deviceId);

        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }

        return q.getResultList();
    }

    @Override
    public long countRepairsByDevice(int deviceId, Date startDate, Date endDate) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COUNT(r) FROM Repair r WHERE r.deviceId.id = :deviceId";

        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }

        Query q = s.createQuery(hql);
        q.setParameter("deviceId", deviceId);

        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }

        return (long) q.getSingleResult();
    }

    @Override
    public BigDecimal getTotalRepairCostByDevice(int deviceId, Date startDate, Date endDate) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT COALESCE(SUM(r.cost), 0) FROM Repair r WHERE r.deviceId.id = :deviceId";

        if (startDate != null) {
            hql += " AND r.date >= :startDate";
        }
        if (endDate != null) {
            hql += " AND r.date <= :endDate";
        }

        Query q = s.createQuery(hql);
        q.setParameter("deviceId", deviceId);

        if (startDate != null) {
            q.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            q.setParameter("endDate", endDate);
        }

        return (BigDecimal) q.getResultList();
    }
}
