/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvd.pojo.Device;
import com.nvd.pojo.Status;
import com.nvd.service.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nvd.repository.DeviceRepository;
import com.nvd.repository.StatusRepository;
import com.nvd.service.StatusService;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;

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
    public int countDeviceByType(int typeId) {
        return this.deviceRepo.countDeviceByType(typeId);
    }

    @Override
    public Device getDeviceById(int id) {
        return this.deviceRepo.getDeviceById(id);
    }

    @Override
    public Device addOrUpdateDevice(Device p) {
        if (!p.getFile().isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(p.getFile().getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                p.setImage(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(DeviceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.deviceRepo.addOrUpdateDevice(p);
    }

}
