/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.net.URLEncoder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author ADMIN
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.nvd.controllers",
    "com.nvd.repository",
    "com.nvd.service"
})
public class SpringSecurityConfigs {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
            Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(c -> c.disable()).authorizeHttpRequests(requests
                -> requests
                        .requestMatchers("/").authenticated()
                        .requestMatchers("/api/**").permitAll()
                        //account
                        .requestMatchers(HttpMethod.GET, "/api/secure/profile").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/accounts").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/account/edit").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/account/{id}/delete").hasRole("ADMIN")
                        //device
                        .requestMatchers(HttpMethod.GET, "/device").authenticated()
                        .requestMatchers(HttpMethod.GET, "/device/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/device/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/device/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/devices/{deviceId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/device/edit").hasRole("ADMIN")
                        //base
                        .requestMatchers(HttpMethod.GET, "/bases", "/base").authenticated()
                        .requestMatchers(HttpMethod.POST, "/base/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/base/delete").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/base/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/base/delete").hasRole("ADMIN")
                        //maintenance
                        .requestMatchers(HttpMethod.GET, "/maintenances", "/maintenance").authenticated()
                        .requestMatchers(HttpMethod.POST, "/maintenance/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/maintenance/{id}/repair/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/maintenance/{id}/delete").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/maintenances/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/maintenance/{id}/confirm").hasAnyRole("USER", "ADMIN")
                        //category
                        .requestMatchers(HttpMethod.GET, "/categories").authenticated()
                        .requestMatchers(HttpMethod.POST, "/category/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/{id}").hasRole("ADMIN")
                        //issue
                        .requestMatchers(HttpMethod.GET, "/issues", "/issue").authenticated()
                        .requestMatchers(HttpMethod.POST, "/issue/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/issue/{id}/isResolved=true/repair/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/issue/{id}/delete").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/issue/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/issue/{id}/confirm").hasAnyRole("USER", "ADMIN")
                       //repair
                        .requestMatchers(HttpMethod.GET, "/repairs", "/repair").authenticated()
                        .requestMatchers(HttpMethod.POST, "/repair/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/repair/{id}/delete").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/repair/add").hasAnyRole("USER", "ADMIN")
                      
                        .anyRequest().authenticated())

                .formLogin(form -> form.loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true").permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login").permitAll());
        
//                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    @Order(0)
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "",
                        "api_key", "",
                        "api_secret", "",
                        "secure", true));
        return cloudinary;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000/")); // frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); // Nếu dùng cookie/session

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
