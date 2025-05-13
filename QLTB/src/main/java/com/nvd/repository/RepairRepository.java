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
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface RepairRepository {
    List<Repair> getRepairs();
    Repair getRepairById(int id);
    Repair addOrUpdateRepair(Repair p);
    Repair addNewMaintenancyOrIssue(Repair p, BigDecimal cost, Device deviceID, RepairType repairTypeId ,int accountId);
}
