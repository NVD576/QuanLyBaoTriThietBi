/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Base;
import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import com.nvd.pojo.Status;
import com.nvd.service.BaseService;
import com.nvd.service.CategoryService;
import com.nvd.service.DeviceService;
import com.nvd.service.MaintenanceService;
import com.nvd.service.StatusService;
import static jakarta.persistence.GenerationType.UUID;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.criteria.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiDeviceControllers {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private MaintenanceService maintenanceService;

    @DeleteMapping("/devices/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "deviceId") int id) {
        this.deviceService.deleteDevice(id);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.deviceService.getDevices(params), HttpStatus.OK);
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.deviceService.getDeviceById(id), HttpStatus.OK);
    }

    @GetMapping("/device/{deviceId}/maintenances")
    public ResponseEntity<List<Maintenance>> getMaintenances(@PathVariable(value = "deviceId") int id) {
        return new ResponseEntity<>(this.deviceService.getMaintenancesByDeviceId(id), HttpStatus.OK);
    }

    @GetMapping("/device/{deviceId}/issues")
    public ResponseEntity<List<Issue>> getIssues(@PathVariable(value = "deviceId") int id) {
        return new ResponseEntity<>(this.deviceService.getIssuesByDeviceId(id), HttpStatus.OK);
    }

    @GetMapping("/device/{deviceId}/repairs")
    public ResponseEntity<List<Repair>> getRepairs(@PathVariable(value = "deviceId") int id) {
        return new ResponseEntity<>(this.deviceService.getRepairsByDeviceId(id), HttpStatus.OK);
    }

    @PostMapping("/device/add")
    public ResponseEntity<?> addDevice(
            @ModelAttribute Device p, // chứa các trường cơ bản và các quan hệ ManyToOne
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            // Xử lý ảnh (nếu có)
//            if (image != null && !image.isEmpty()) {
//                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
//                Path path = Paths.get("uploads/" + fileName); // thư mục bạn tự cấu hình
//                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                device.setImage(fileName);
//            }
            // Lấy các entity từ id:
            p.setBaseId(baseService.getBaseById(p.getBaseId().getId()));
            p.setCategoryId(categoryService.getCategotryById(p.getCategoryId().getId()));
            p.setStatusId(statusService.getStatusById(p.getStatusId().getId()));

            // Lưu device
            Device device = deviceService.addOrUpdateDevice(p);

            // Tạo Maintenance
            Maintenance m = new Maintenance();
            maintenanceService.addNewDevice(m, device);

            return new ResponseEntity<>(device, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi khi thêm thiết bị", HttpStatus.BAD_REQUEST);
        }
    }

}
