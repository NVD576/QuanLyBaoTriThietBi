/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.service.Impl;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nvd.service.CategoryService;
import com.nvd.repository.CategoryRepository;

/**
 *
 * @author ADMIN
 */
@Service
public class CategoryServiveImpl implements  CategoryService{

    @Autowired
    private CategoryRepository cateRepo;

    @Override
    public List<Category> getCates() {
        return this.cateRepo.getCates();
    }

    @Override
    public List<Device> getDevicesByCateId(int categoryId) {
        return this.cateRepo.getDevicesByCateId(categoryId);
    }

    @Override
    public Category getCategotryById(int id) {
        return this.cateRepo.getCategotryById(id);
    }
}
