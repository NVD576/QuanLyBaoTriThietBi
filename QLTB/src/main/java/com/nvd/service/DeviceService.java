/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Device;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface DeviceService {
    List<Device> getDevices(Map<String, String> params);
    int countDevice();
    int countDeviceByType(int typeId);
    Device getDeviceById(int id);
    boolean addOrUpdateDevice(Device p);
}
