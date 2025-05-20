/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.IncidentLevel;
import com.nvd.pojo.Issue;
import com.nvd.pojo.Repair;
import com.nvd.service.IncidentLevelService;
import com.nvd.service.IssueService;
import com.nvd.service.RepairService;
import com.nvd.service.RepairTypeService;
import java.math.BigDecimal;
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
public class ApiIssueControllerss {

    @Autowired
    private IssueService issueService;
    @Autowired
    private IncidentLevelService incidentLevelService;
    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairTypeService repairTypeService;

    @DeleteMapping("/issue/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.issueService.deleteIssue(id);
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
        return new ResponseEntity<>(this.issueService.addOrUpdateIssue(p), HttpStatus.CREATED);
    }

    @GetMapping("/levels")
    public ResponseEntity<List<IncidentLevel>> getIncidentLevels() {
        return new ResponseEntity<>(this.incidentLevelService.getIncidentLevels(), HttpStatus.OK);
    }

    @PostMapping("/issue/{id}/confirm")
    public ResponseEntity<Issue> edit(@PathVariable(value = "id") int id,
            @RequestParam("cost") BigDecimal cost,
            @RequestParam("accountId") int accountId) {
        Issue p = this.issueService.getIssueById(id);
        if (p == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (p != null && !p.getIsResolved()) {
            p.setIsResolved(true);
            issueService.addOrUpdateIssue(p);
            Repair repair = new Repair();
            repairService.addNewMaintenancyOrIssue(repair, cost, p.getDeviceId(), this.repairTypeService.getTypeById(2), accountId);
        }
        p.setIsResolved(true);
        return new ResponseEntity<>(this.issueService.addOrUpdateIssue(p), HttpStatus.OK);
    }
}
