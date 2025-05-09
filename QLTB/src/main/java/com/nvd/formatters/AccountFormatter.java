/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.formatters;

import com.nvd.pojo.Account;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ADMIN
 */
public class AccountFormatter implements Formatter<Account> {

    @Override
    public String print(Account object, Locale locale) {
        return String.valueOf(object.getId());
    }

    @Override
    public Account parse(String text, Locale locale) throws ParseException {
        int id = Integer.parseInt(text);
        return new Account(id);
    }

}
