/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.repository;

import com.nvd.pojo.Base;
import com.nvd.pojo.Device;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface BaseRepository {
    List<Base> getBases();
    Base getBaseById(int id);
    List<Device> getDevicesByBaseId(int baseId);
    Base addOrUpdateBase(Base p);
    void deleteBase (int id);
}
