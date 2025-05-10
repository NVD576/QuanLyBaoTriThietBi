/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.IncidentLevel;
import com.nvd.repository.IncidentLevelRepository;
import com.nvd.service.IncidentLevelService;
import jakarta.resource.spi.AuthenticationMechanism;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class IncidentLevelServiceImpl implements IncidentLevelService{

    @Autowired
    private IncidentLevelRepository incidentLevelRepo;
    
    @Override
    public List<IncidentLevel> getIncidentLevels() {
        return this.incidentLevelRepo.getIncidentLevels();
    }
    
}
