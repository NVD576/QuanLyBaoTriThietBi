/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Device;
import com.nvd.pojo.Maintenance;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface MaintenanceService {
    List<Maintenance> getMaintenances();
    Maintenance addOrUpdateMaintenance(Maintenance p);
    List<Maintenance> getByDeviceId(int id);
    Maintenance addNewDevice(Maintenance p, Device d);
    Maintenance getMaintenanceById(int id);
}
