/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvd.formatters.AccountFormatter;
import com.nvd.formatters.CategoryFormatter;
import com.nvd.formatters.StatusFormatter;
import com.nvd.pojo.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author ADMIN
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.nvd.controllers",
    "com.nvd.repository",
    "com.nvd.service"
})
public class WebAppContextConfigs implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CategoryFormatter());
        registry.addFormatter(new AccountFormatter());
        registry.addFormatter(new StatusFormatter());
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");

    }
}
