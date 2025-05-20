/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Repair;
import com.nvd.pojo.RepairType;
import com.nvd.service.RepairService;
import com.nvd.service.RepairTypeService;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiRepairControllers {

    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairTypeService repairTypeService;

    @DeleteMapping("/repair/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.repairService.deleteRepair(id);
    }

    @GetMapping("/repairs")
    public ResponseEntity<List<Repair>> getRepairs() {
        return new ResponseEntity<>(this.repairService.getRepairs(), HttpStatus.OK);
    }

    @GetMapping("/repair/{id}")
    public ResponseEntity<Repair> getRepairById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.repairService.getRepairById(id), HttpStatus.OK);
    }

    @PostMapping("/repair/add")
    public ResponseEntity<Repair> createRepair(@RequestBody Repair p) {
        return null; // new ResponseEntity<>(this.repairService.addOrUpdateMaintenance(p), HttpStatus.CREATED);
    }

    @GetMapping("/repairTypes")
    public ResponseEntity<List<RepairType>> getRepairTyyes() {
        return new ResponseEntity<>(this.repairTypeService.getRepairTypes(), HttpStatus.OK);
    }
}
