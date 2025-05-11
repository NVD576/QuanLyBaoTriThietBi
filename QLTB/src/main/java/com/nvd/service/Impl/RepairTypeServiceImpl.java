/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.RepairType;
import com.nvd.repository.RepairTypeRepository;
import com.nvd.service.RepairTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class RepairTypeServiceImpl implements RepairTypeService {

    @Autowired
    private RepairTypeRepository repairTypeRepo;

    @Override
    public List<RepairType> getRepairTypes() {
        return this.repairTypeRepo.getRepairTypes();
    }

}
