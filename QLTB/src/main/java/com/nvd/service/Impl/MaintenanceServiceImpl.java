/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Maintenance;
import com.nvd.repository.MaintenanceRepository;
import com.nvd.service.MaintenanceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Override
    public List<Maintenance> getMaintenances() {
        return this.maintenanceRepository.getMaintenances();
    }

    @Override
    public Maintenance addOrUpdateMaintenance(Maintenance p) {

        return this.maintenanceRepository.addOrUpdateMaintenance(p);
    }

    @Override
    public List<Maintenance> getByDeviceId(int id) {
        return this.maintenanceRepository.getByDeviceId(id);
    }

}
