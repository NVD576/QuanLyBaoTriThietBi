/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.controllers;

import com.nvd.pojo.Issue;
import com.nvd.pojo.Maintenance;
import com.nvd.service.DeviceService;
import com.nvd.service.IncidentLevelService;
import com.nvd.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/issues")
    public String show(Model model) {
        model.addAttribute("issues", this.issueService.getIssues());
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
    public String resolveIssue(@PathVariable("id") int id) {
        Issue issue = issueService.getIssueById(id);
        if (issue != null && !issue.getIsResolved()) {
            issue.setIsResolved(true);
            issueService.addOrUpdateIssue(issue);  // sử dụng lại hàm đã có
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
