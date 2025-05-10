/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.MaintenanceType;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceTypeRepository {
    List<MaintenanceType> getMaintenanceTypes();
}
