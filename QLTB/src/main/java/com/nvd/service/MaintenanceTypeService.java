/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.MaintenanceType;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */

public interface MaintenanceTypeService {
    List<MaintenanceType> getMaintenanceTypes();
    MaintenanceType getMaintenanceTypeById(int id);
}
