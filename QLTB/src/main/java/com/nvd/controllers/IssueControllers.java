/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Issue;
import com.nvd.pojo.Repair;
import com.nvd.service.AccountService;
import com.nvd.service.DeviceService;
import com.nvd.service.IncidentLevelService;
import com.nvd.service.IssueService;
import com.nvd.service.RepairService;
import com.nvd.service.RepairTypeService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
@ControllerAdvice
public class IssueControllers {

    @Autowired
    private IssueService issueService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private IncidentLevelService incidentLevelService;
    @Autowired
    private RepairService repairService;
    @Autowired
    private RepairTypeService repairTypeService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/issues")
    public String show(Model model) {
        model.addAttribute("issues", this.issueService.getIssues());
        model.addAttribute("accounts", accountService.getAccount());
        return "issues";
    }

    @PostMapping("/issue/add")
    public String add(@ModelAttribute(value = "issue") Issue p, BindingResult result,
            Model model) {
        if (this.issueService.addOrUpdateIssue(p) != null) {
            return "redirect:/issues";
        }
        return "issue-add";
    }

    @PostMapping("/issue/resolve/{id}")
    public String resolveIssue(@PathVariable("id") int id,
             @RequestParam("cost") BigDecimal cost,
             @RequestParam("accountId") int accountId) {
        Issue issue = issueService.getIssueById(id);
        if (issue != null && !issue.getIsResolved()) {
            issue.setIsResolved(true);
            issueService.addOrUpdateIssue(issue);
            Repair repair = new Repair();
            repairService.addNewMaintenancyOrIssue(repair, cost, issue.getDeviceId(), this.repairTypeService.getTypeById(2), accountId);
        }
        return "redirect:/issues";
    }

    @GetMapping("/issue")
    public String insert(Model model) {
        model.addAttribute("issue", new Issue());
        model.addAttribute("levels", this.incidentLevelService.getIncidentLevels());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        return "issue-add";
    }

    @GetMapping("/issue/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("issue", this.issueService.getIssueById(id));
        model.addAttribute("levels", this.incidentLevelService.getIncidentLevels());
        model.addAttribute("devices", this.deviceService.getDevices(null));
        return "issue-add";
    }
}
