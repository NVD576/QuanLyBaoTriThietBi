/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Base;
import com.nvd.pojo.Device;
import com.nvd.service.BaseService;
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
public class ApiBaseControllers {
    @Autowired
    private BaseService baseService;

    @DeleteMapping("/base/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        
    }
    @GetMapping("/base/")
    public ResponseEntity<List<Base>> getBases(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.baseService.getBases(), HttpStatus.OK);
    }
    
    @GetMapping("/base/{id}")
    public ResponseEntity<Base> getBaseById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.baseService.getBaseById(id), HttpStatus.OK);
    }

    @GetMapping("/base/{baseId}/devices/")
    public ResponseEntity<List<Device>> getDevices(@PathVariable(value = "deviceId") int id) {
        return null; //new ResponseEntity<>(this.baseService.getMaintenancesByDeviceId(id), HttpStatus.OK);
    }
    
    @PostMapping("/device/add/")
    public ResponseEntity<Base> create(@RequestBody Base p) {
        return null; //new ResponseEntity<>(this.baseService.addOrUpdateDevice(p), HttpStatus.CREATED);
    }
}
