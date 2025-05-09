/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Base;
import com.nvd.repository.BaseRepository;
import com.nvd.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class BaseServiceImpl implements BaseService{
    @Autowired
    private BaseRepository baseRepo;
    @Override
    public List<Base> getBases() {
        return this.baseRepo.getBases();
    }
    
}
