/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.EquipmentType;
import com.nvd.repository.EquipmentTypeRepositoy;
import com.nvd.service.EquipmentTypeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class EquipmentTypeServiveImpl implements  EquipmentTypeService{

    @Autowired
    private EquipmentTypeRepositoy equipmentTypeRepo;

    @Override
    public List<EquipmentType> getEquipmentType() {
        return this.equipmentTypeRepo.getEquipmentType();
    }
}
