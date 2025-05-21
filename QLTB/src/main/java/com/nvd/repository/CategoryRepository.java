/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface CategoryRepository {
    List<Category> getCates();
    List<Device> getDevicesByCateId(int categoryId);
    Category getCategotryById(int id);
    Category addOrUpdateCategory(Category p);
    void deleteCategory(int id);
}
