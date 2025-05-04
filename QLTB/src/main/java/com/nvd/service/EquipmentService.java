/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Equipment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface EquipmentService {
    List<Equipment> getEquipments(Map<String, String> params);
}
