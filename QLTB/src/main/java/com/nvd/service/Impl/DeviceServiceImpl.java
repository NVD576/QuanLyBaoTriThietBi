/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Device;
import com.nvd.service.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvd.repository.DeviceRepository;

/**
 *
 * @author ADMIN
 */
@Service

public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Device> getDevices(Map<String, String> params) {
        return this.deviceRepo.getDevices(params);
    }

    @Override
    public int countDevice() {
        return this.deviceRepo.countDevice();
    }

    @Override
    public int countDeviceByType(int typeId) {
        return this.deviceRepo.countDeviceByType(typeId);
    }

    @Override
    public Device getDeviceById(int id) {
        return this.deviceRepo.getDeviceById(id);
    }

    @Override
    public boolean addOrUpdateDevice(Device p) {
        p.setImage("https://res.cloudinary.com/dqpoa9ukn/image/upload/v1735652094/ks4nkbgqzm1tuhyfwolc.jpg");
        return this.deviceRepo.addOrUpdateDevice(p);
    }

}
