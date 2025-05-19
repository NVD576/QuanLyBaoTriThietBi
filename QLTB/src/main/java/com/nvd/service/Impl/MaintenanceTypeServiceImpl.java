/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.MaintenanceType;
import com.nvd.repository.MaintenanceTypeRepository;
import com.nvd.service.MaintenanceTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class MaintenanceTypeServiceImpl implements MaintenanceTypeService{
    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;
    @Override
    public List<MaintenanceType> getMaintenanceTypes() {
        return this.maintenanceTypeRepository.getMaintenanceTypes();
    }

    @Override
    public MaintenanceType getMaintenanceTypeById(int id) {
        return this.maintenanceTypeRepository.getMaintenanceTypeById(id);
    }
    
}
