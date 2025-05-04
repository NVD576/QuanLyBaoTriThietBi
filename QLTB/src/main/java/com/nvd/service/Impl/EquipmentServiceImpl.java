/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Equipment;
import com.nvd.repository.EquipmentRepository;
import com.nvd.service.EquipmentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Service

public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Equipment> getEquipments(Map<String, String> params) {
        return this.equipmentRepo.getEquipments(params);
    }

}
