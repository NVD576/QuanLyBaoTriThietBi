/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Status;
import com.nvd.repository.StatusRepository;
import com.nvd.service.StatusService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.nvd.repository.CategoryRepository;
import org.springframework.security.config.web.server.ServerSecurityMarker;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class StatusServiceImpl implements StatusService{
    @Autowired
    private StatusRepository statusRepo;

    @Override
    public List<Status> getStatus() {
        return this.statusRepo.getStatus();
    }
    
}
