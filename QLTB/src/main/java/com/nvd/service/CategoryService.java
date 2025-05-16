/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface CategoryService {
    List<Category> getCates();
    List<Device> getDevicesByCateId(int categoryId);
    Category getCategotryById(int id);
}
