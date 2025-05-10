/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Issue;
import com.nvd.repository.IssueRepository;
import com.nvd.service.IssueService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class IssueServiceImpl implements IssueService{
    @Autowired
    private IssueRepository issueRepo;
    @Override
    public List<Issue> getIssues() {
        return this.issueRepo.getIssues();
    }

    @Override
    public Issue addOrUpdateIssue(Issue p) {
        return this.issueRepo.addOrUpdateIssue(p);
    }
    
}
