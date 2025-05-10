/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nvd.service;

import com.nvd.pojo.Status;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface StatusService {
    List<Status> getStatus();
    Status getStatusById(int id);
}
