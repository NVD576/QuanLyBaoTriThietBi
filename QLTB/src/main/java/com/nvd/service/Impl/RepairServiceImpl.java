/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import com.nvd.pojo.Status;
import com.nvd.repository.RepairRepository;
import com.nvd.service.RepairService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairRepository repairRepo;

    @Override
    public List<Repair> getRepairs() {
        return this.repairRepo.getRepairs();
    }

    @Override
    public Repair getRepairById(int id) {
        return this.repairRepo.getRepairById(id);
    }

    @Override
    public Repair addOrUpdateRepair(Repair p) {
        return this.repairRepo.addOrUpdateRepair(p);
    }

    @Override
    public Repair addNewMaintenancyOrIssue(Repair p, BigDecimal cost, Device deviceID, RepairType repairTypeId, int accountId) {
        return this.repairRepo.addNewMaintenancyOrIssue(p, cost, deviceID, repairTypeId, accountId);
    }

    @Override
    public void deleteRepair(int id) {
        this.repairRepo.deleteRepair(id);
    }

    @Override
    public BigDecimal getTotalRepairCost(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        return repairRepo.getTotalRepairCost(startDate, endDate, categoryId, statusId);
    }

    @Override
    public long countRepairs(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        return repairRepo.countRepairs(startDate, endDate, categoryId, statusId);
    }

    @Override
    public Device getMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        return repairRepo.getMostRepairedDevice(startDate, endDate, categoryId, statusId);
    }

    @Override
    public long getRepairCountForMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        Device device = getMostRepairedDevice(startDate, endDate, categoryId, statusId);
        return device != null
                ? repairRepo.countRepairsByDevice(device.getId(), startDate, endDate) : 0;
    }

    @Override
    public RepairType getMostCommonRepairType(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        return repairRepo.getMostCommonRepairType(startDate, endDate, categoryId, statusId);
    }

    @Override
    public long getRepairCountForMostCommonType(Date startDate, Date endDate, Integer categoryId, Integer statusId) {
        RepairType type = getMostCommonRepairType(startDate, endDate, categoryId, statusId);
        if (type == null) {
            return 0;
        }

        List<Object[]> stats = repairRepo.getRepairTypeStats(startDate, endDate, categoryId, statusId);
        return stats.stream()
                .filter(s -> type.equals(s[0]))
                .mapToLong(s -> (long) s[1])
                .findFirst()
                .orElse(0);
    }

    @Override
    public Map<String, BigDecimal> getRepairCostByTimeRange(Date startDate, Date endDate,
            Integer categoryId, Integer statusId, String timeRange) {
        List<Object[]> results = repairRepo.getRepairCostStatsByTimeRange(
                startDate, endDate, categoryId, statusId, timeRange);

        return results.stream()
                .collect(Collectors.toMap(
                        o -> (String) o[0],
                        o -> (BigDecimal) o[1]
                ));
    }

    @Override
    public Map<RepairType, Long> getRepairCountByType(Date startDate, Date endDate,
            Integer categoryId, Integer statusId) {
        List<Object[]> results = repairRepo.getRepairTypeStats(
                startDate, endDate, categoryId, statusId);

        return results.stream()
                .collect(Collectors.toMap(
                        o -> (RepairType) o[0],
                        o -> (Long) o[1]
                ));
    }

    @Override
    public List<Map<String, Object>> getDeviceRepairStats(Date startDate, Date endDate,
        Integer categoryId, Integer statusId) {
    List<Object[]> results = repairRepo.getDeviceRepairStats(
            startDate, endDate, categoryId, statusId);

    return results.stream()
            .map(o -> {
                Map<String, Object> stat = new LinkedHashMap<>();
                Device device = (Device) o[0];
                Category category = (Category) o[1];
                Status status = (Status) o[2];

                stat.put("deviceId", device.getId());
                stat.put("deviceName", device.getName());
                stat.put("deviceCategory", category.getName());
                stat.put("deviceStatus", status.getName());
                stat.put("repairCount", o[3]);
                stat.put("totalCost", o[4]);
                stat.put("lastRepairDate", o[5]);

                return stat;
            })
            .collect(Collectors.toList());
}


    @Override
    public Map<String, BigDecimal> getRepairCostByDeviceCategory(Date startDate, Date endDate, Integer statusId) {
        List<Object[]> results = repairRepo.getDeviceRepairStats(
                startDate, endDate, null, statusId);

        return results.stream()
                .collect(Collectors.groupingBy(
                        o -> ((Device) o[0]).getCategoryId().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                o -> (BigDecimal) o[4],
                                BigDecimal::add
                        )
                ));
    }

    @Override
    public Map<String, BigDecimal> getRepairCostByDeviceStatus(Date startDate, Date endDate, Integer categoryId) {
        List<Object[]> results = repairRepo.getDeviceRepairStats(
                startDate, endDate, categoryId, null);

        return results.stream()
                .collect(Collectors.groupingBy(
                        o -> ((Device) o[0]).getStatusId().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                o -> (BigDecimal) o[4],
                                BigDecimal::add
                        )
                ));
    }

    @Override
    public List<Repair> getRepairsByDevice(int deviceId, Date startDate, Date endDate) {
        return repairRepo.getRepairsByDevice(deviceId, startDate, endDate);
    }

    @Override
    public long countRepairsByDevice(int deviceId, Date startDate, Date endDate) {
        return repairRepo.countRepairsByDevice(deviceId, startDate, endDate);
    }

    @Override
    public BigDecimal getTotalRepairCostByDevice(int deviceId, Date startDate, Date endDate) {
        return repairRepo.getTotalRepairCostByDevice(deviceId, startDate, endDate);
    }
}
