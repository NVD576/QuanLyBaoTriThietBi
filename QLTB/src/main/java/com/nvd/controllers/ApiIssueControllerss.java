/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.IncidentLevel;
import com.nvd.pojo.Issue;
import com.nvd.service.IncidentLevelService;
import com.nvd.service.IssueService;
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
public class ApiIssueControllerss {
    @Autowired
    private IssueService issueService;
    @Autowired
    private IncidentLevelService incidentLevelService;
    
    @DeleteMapping("/issue/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssues() {
        return new ResponseEntity<>(this.issueService.getIssues(), HttpStatus.OK);
    }
    
    @GetMapping("/issue/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(this.issueService.getIssueById(id), HttpStatus.OK);
    }
    
    @PostMapping("/issue/add")
    public ResponseEntity<Issue> create(@RequestBody Issue p) {
        return null; // new ResponseEntity<>(this.issueService.addOrUpdateMaintenance(p), HttpStatus.CREATED);
    }
    
    @GetMapping("/incident-levels/")
    public ResponseEntity<List<IncidentLevel>> getIncidentLevels() {
        return new ResponseEntity<>(this.incidentLevelService.getIncidentLevels(), HttpStatus.OK);
    }
    
    
}
