/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.Repair;
import com.nvd.service.DeviceService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



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
    public ResponseEntity<Device> create(@RequestBody Device p) {
        return new ResponseEntity<>(this.deviceService.addOrUpdateDevice(p), HttpStatus.CREATED);
    }
}
