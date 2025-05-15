/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Status;
import com.nvd.service.StatusService;
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
public class ApiStatusControllers {
    @Autowired
    private StatusService statusService;
    
    @DeleteMapping("/statuses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        
    }
    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getstatusess(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.statusService.getStatus(), HttpStatus.OK);
    }
    
    @GetMapping("/statuses/{id}")
    public ResponseEntity<Status> getstatusesById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.statusService.getStatusById(id), HttpStatus.OK);
    }


    
    @PostMapping("/statuses/add")
    public ResponseEntity<Status> create(@RequestBody Status p) {
        return null; //new ResponseEntity<>(this.statusesService.addOrUpdateDevice(p), HttpStatus.CREATED);
    }
}
