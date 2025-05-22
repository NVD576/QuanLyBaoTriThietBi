/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import com.nvd.service.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvd.repository.DeviceRepository;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADMIN
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepo;
    @Autowired
    private Cloudinary cloudinary;

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
    public int countDeviceByType(int cateId) {
        return this.deviceRepo.countDeviceByType(cateId);
    }

    @Override
    public Device getDeviceById(int id) {
        return this.deviceRepo.getDeviceById(id);
    }

    @Override
    public void deleteDevice(int id) {
        this.deviceRepo.deleteDevice(id);
    }

    @Override
    public Device addOrUpdateDevice(Device p) {
        Device currentDeivce = this.getDeviceById(p.getId());
        if (p.getFile() != null && !p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(DeviceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            p.setImage(currentDeivce.getImage());
        }
        return this.deviceRepo.addOrUpdateDevice(p);
    }

    @Override
    public List<Maintenance> getMaintenancesByDeviceId(int deviceId) {
        return this.deviceRepo.getMaintenancesByDeviceId(deviceId);
    }

    @Override
    public List<Issue> getIssuesByDeviceId(int deviceId) {
        return this.deviceRepo.getIssuesByDeviceId(deviceId);
    }

    @Override
    public List<Repair> getRepairsByDeviceId(int deviceId) {
        return this.deviceRepo.getRepairsByDeviceId(deviceId);
    }

    @Override
    public int countDeviceByConditions(String kw, Integer baseId, Integer cateId) {
        return this.deviceRepo.countDeviceByConditions(kw, baseId, cateId);
    }

}
