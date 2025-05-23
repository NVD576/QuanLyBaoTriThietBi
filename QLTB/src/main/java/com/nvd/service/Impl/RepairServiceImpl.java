/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import com.nvd.repository.RepairRepository;
import com.nvd.service.RepairService;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Session;
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

}
