/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Issue;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IssueService {
    List<Issue> getIssues();
    Issue addOrUpdateIssue(Issue p);
}
