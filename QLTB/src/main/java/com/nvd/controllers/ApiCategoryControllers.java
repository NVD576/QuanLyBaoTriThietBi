package com.nvd.controllers;

import com.nvd.pojo.Category;
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
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable(value = "id") int id) {
        
    }

//    @GetMapping("/categories/{id}")
//    public ResponseEntity<Status> getstatusesById(@PathVariable(value = "id") int id) {
//        return new ResponseEntity<>(this.categoryService.getCates(id), HttpStatus.OK);
//    }


    
//    @PostMapping("/categories/add")
//    public ResponseEntity<Status> create(@RequestBody Status p) {
//        return new ResponseEntity<>(this.categoryService.addOrUpdateDevice(p), HttpStatus.CREATED);
//    }
}