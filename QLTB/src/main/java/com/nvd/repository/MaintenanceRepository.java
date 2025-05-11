/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Maintenance;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceRepository {
    List<Maintenance> getMaintenances();
    Maintenance getMaintenanceById(int id);
    Maintenance addOrUpdateMaintenance(Maintenance p);
    List<Maintenance> getByDeviceId(int id);
}
