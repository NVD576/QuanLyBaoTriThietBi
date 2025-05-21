package com.nvd.controllers;

import com.nvd.pojo.Category;
import com.nvd.pojo.Device;
import com.nvd.pojo.Status;
import com.nvd.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCategoryControllers {
    @Autowired
    private CategoryService categoryService;

    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return new ResponseEntity<>(categoryService.getCates(), HttpStatus.OK);
    }
    
    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        this.categoryService.deleteCategory(id);
    }

    @GetMapping("/category/{categoryId}/devices")
    public ResponseEntity<List<Device>> getstatusesById(@PathVariable(value = "categoryId") int id) {
        return new ResponseEntity<>(this.categoryService.getDevicesByCateId(id), HttpStatus.OK);
    }


    
//    @PostMapping("/categories/add")
//    public ResponseEntity<Status> create(@RequestBody Category p) {
//        return new ResponseEntity<>(this.categoryService.addOrUpdateDevice(p), HttpStatus.CREATED);
//    }
}