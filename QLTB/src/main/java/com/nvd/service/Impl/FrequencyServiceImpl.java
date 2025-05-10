/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Frequency;
import com.nvd.repository.FrequencyRepository;
import com.nvd.service.FrequencyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class FrequencyServiceImpl implements FrequencyService{
    @Autowired
    private FrequencyRepository frequencyRepository;
    @Override
    public List<Frequency> getFrequency() {
        return this.frequencyRepository.getFrequencys();
    }
    
}
