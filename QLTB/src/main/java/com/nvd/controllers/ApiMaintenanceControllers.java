/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Maintenance;
import com.nvd.service.MaintenanceService;
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
 * @author admin
 */

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiMaintenanceControllers {
    @Autowired
    private MaintenanceService maintenanceService;
    
    @DeleteMapping("/maintenances/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "deviceId") int id) {
        //this.maintenanceService.deleteDevice(id);
    }

    @GetMapping("/maintenances/")
    public ResponseEntity<List<Maintenance>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.maintenanceService.getMaintenances(), HttpStatus.OK);
    }
    
    @GetMapping("/maintenances/{id}")
    public ResponseEntity<Maintenance> getDeviceById(@PathVariable(value = "productId") int id) {
        return new ResponseEntity<>(this.maintenanceService.getMaintenanceById(id), HttpStatus.OK);
    }
    
    @GetMapping("/maintenances/{id}/frequency/")
    public ResponseEntity<List<Maintenance>> getMaintenances(@PathVariable(value = "productId") int id) {
        return null; //new ResponseEntity<>(this.deviceService.getDevices(id), HttpStatus.OK)
    }
    
    @PostMapping("/maintenances/")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Maintenance p) {
        this.maintenanceService.addOrUpdateMaintenance(p);
    }

}
