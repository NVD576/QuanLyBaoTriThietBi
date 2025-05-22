/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;
import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface DeviceService {
    List<Device> getDevices(Map<String, String> params);
    int countDevice();
    int countDeviceByType(int cateId);
    Device getDeviceById(int id);
    Device addOrUpdateDevice(Device p);
    void deleteDevice(int id);
    List<Maintenance> getMaintenancesByDeviceId(int deviceId);
    List<Issue> getIssuesByDeviceId(int deviceId);
    List<Repair> getRepairsByDeviceId(int deviceId);
    int countDeviceByConditions(String kw, Integer baseId, Integer cateId);
}
