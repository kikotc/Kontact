package com.example;

import java.util.regex.Pattern;

import com.example.MainTable.Person;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class ValidatedCell extends TextFieldTableCell<Person, String> {

    private final String columnType;

    public ValidatedCell(String columnType) {
        super(new DefaultStringConverter());
        this.columnType = columnType;
    }

    @Override
    public void commitEdit(String input) {
        if (isValid(input)) {
            super.commitEdit(input);
        } else {
            cancelEdit();
        }
    }

    private boolean isValid(String input) {
        switch (columnType) {
            case "name":
                return input.matches("[a-zA-Z]+") && input.length() > 1;
            case "phoneNum":
                return input.matches("[0-9]+") && input != null;
            case "email":
                return isEmailValid(input);
            default:
                return true;
        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        return email != null && pattern.matcher(email).matches();
    }
}