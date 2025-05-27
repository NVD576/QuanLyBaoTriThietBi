package com.nvd.service;

import com.nvd.pojo.Device;
import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface RepairService {
    // Các phương thức hiện có
    List<Repair> getRepairs();
    Repair getRepairById(int id);
    Repair addOrUpdateRepair(Repair p);
    Repair addNewMaintenancyOrIssue(Repair p, BigDecimal cost, Device deviceID, RepairType repairTypeId, int accountId);
    void deleteRepair(int id);
    BigDecimal getTotalRepairCost(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    long countRepairs(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    Device getMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    long getRepairCountForMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    RepairType getMostCommonRepairType(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    long getRepairCountForMostCommonType(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    Map<String, BigDecimal> getRepairCostByTimeRange(Date startDate, Date endDate, Integer categoryId, Integer statusId, String timeRange);
    Map<RepairType, Long> getRepairCountByType(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    List<Map<String, Object>> getDeviceRepairStats(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    List<Repair> getRepairsByDevice(int deviceId, Date startDate, Date endDate);
    long countRepairsByDevice(int deviceId, Date startDate, Date endDate);
    BigDecimal getTotalRepairCostByDevice(int deviceId, Date startDate, Date endDate);
    Map<String, BigDecimal> getRepairCostByDeviceCategory(Date startDate, Date endDate, Integer statusId);
    Map<String, BigDecimal> getRepairCostByDeviceStatus(Date startDate, Date endDate, Integer categoryId);
}