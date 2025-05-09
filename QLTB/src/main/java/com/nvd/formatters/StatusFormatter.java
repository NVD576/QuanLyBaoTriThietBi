/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.formatters;

import com.nvd.pojo.Account;
import com.nvd.pojo.Status;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class StatusFormatter implements Formatter<Status>{

    @Override
    public String print(Status object, Locale locale) {
        return String.valueOf(object.getId());
    }

    @Override
    public Status parse(String text, Locale locale) throws ParseException {
        int id = Integer.parseInt(text);
        return new Status(id);
    }
}
