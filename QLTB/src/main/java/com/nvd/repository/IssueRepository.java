/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Issue;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IssueRepository {
    List<Issue> getIssues();
    Issue getIssueById(int id);
    Issue addOrUpdateIssue(Issue p);
    void deleteIssue(int id);
}
