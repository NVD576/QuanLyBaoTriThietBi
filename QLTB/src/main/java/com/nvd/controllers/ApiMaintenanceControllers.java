/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Frequency;
import com.nvd.pojo.Maintenance;
import com.nvd.pojo.MaintenanceType;
import com.nvd.service.DeviceService;
import com.nvd.service.FrequencyService;
import com.nvd.service.MaintenanceService;
import com.nvd.service.MaintenanceTypeService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired 
    private MaintenanceTypeService maintenanceTypeService;
    @Autowired
    private FrequencyService frequencyService;

    @Autowired
    private DeviceService deviceService;
    @DeleteMapping("/maintenance/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {

    }

    @GetMapping("/maintenances")
    public ResponseEntity<List<Maintenance>> getMaintenances(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.maintenanceService.getMaintenances(), HttpStatus.OK);
    }

    @GetMapping("/maintenance/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.maintenanceService.getMaintenanceById(id), HttpStatus.OK);
    }

    @PostMapping("/maintenances/add")
    public ResponseEntity<Maintenance> create(@ModelAttribute Maintenance p) {
        p.setDeviceId(deviceService.getDeviceById(p.getDeviceId().getId()));
        p.setFrequencyId(frequencyService.getFrequencyById(p.getFrequencyId().getId()));
        p.setTypeId(maintenanceTypeService.getMaintenanceTypeById(p.getTypeId().getId()));
        return new ResponseEntity<>(this.maintenanceService.addOrUpdateMaintenance(p), HttpStatus.CREATED);
    }

    @GetMapping("/maintenanceTypes")
    public ResponseEntity<List<MaintenanceType>> getMaintenanceTypes() {
        return new ResponseEntity<>(this.maintenanceTypeService.getMaintenanceTypes(), HttpStatus.OK);
    }
    @GetMapping("/frequencies")
    public ResponseEntity<List<Frequency>>getFrequencies() {
        return new ResponseEntity<>(this.frequencyService.getFrequency(), HttpStatus.OK);
    }
}
