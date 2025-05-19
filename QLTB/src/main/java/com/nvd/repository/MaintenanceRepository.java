/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceRepository {
    List<Maintenance> getMaintenances();
    Maintenance getMaintenanceById(int id);
    Maintenance addOrUpdateMaintenance(Maintenance p);
    List<Maintenance> getByDeviceId(int id);
    Maintenance addNewDevice(Maintenance p, Device d);
    void deleteMaintenance(int id);
}
