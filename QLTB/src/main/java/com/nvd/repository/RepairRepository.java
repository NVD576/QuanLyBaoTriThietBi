/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Account;
import com.nvd.pojo.Device;
import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairRepository {
    // Các phương thức hiện có
    List<Repair> getRepairs();
    Repair getRepairById(int id);
    Repair addOrUpdateRepair(Repair p);
    Repair addNewMaintenancyOrIssue(Repair p, BigDecimal cost, Device deviceID, RepairType repairTypeId, int accountId);
    void deleteRepair(int id);
    
    // Thêm các phương thức mới cho thống kê
    
    /**
     * Tính tổng chi phí sửa chữa theo điều kiện lọc
     */
    BigDecimal getTotalRepairCost(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Đếm tổng số lần sửa chữa theo điều kiện lọc
     */
    long countRepairs(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Lấy thiết bị có nhiều lần sửa chữa nhất
     */
    Device getMostRepairedDevice(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Lấy loại sửa chữa phổ biến nhất
     */
    RepairType getMostCommonRepairType(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Thống kê chi phí sửa chữa theo khoảng thời gian
     */
    List<Object[]> getRepairCostStatsByTimeRange(Date startDate, Date endDate, Integer categoryId, Integer statusId, String timeRange);
    
    /**
     * Thống kê số lần sửa chữa theo loại
     */
    List<Object[]> getRepairTypeStats(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Thống kê thiết bị và số lần sửa chữa
     */
    List<Object[]> getDeviceRepairStats(Date startDate, Date endDate, Integer categoryId, Integer statusId);
    
    /**
     * Lấy danh sách sửa chữa theo thiết bị
     */
    List<Repair> getRepairsByDevice(int deviceId, Date startDate, Date endDate);
    
    /**
     * Đếm số lần sửa chữa của một thiết bị cụ thể
     */
    long countRepairsByDevice(int deviceId, Date startDate, Date endDate);
    
    /**
     * Tính tổng chi phí sửa chữa của một thiết bị cụ thể
     */
    BigDecimal getTotalRepairCostByDevice(int deviceId, Date startDate, Date endDate);
}