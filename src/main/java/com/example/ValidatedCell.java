package com.example;

import com.example.MainTable.Person;
import java.util.regex.Pattern;
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

    public boolean isValid(String input) {
        switch (columnType) {
            case "name":
                return input.equals("") || input.matches("[a-zA-Z]+");
            case "phoneNum":
                return input.equals("") || input.matches("[0-9]+");
            case "email":
                return input.equals("") || isEmailValid(input);
            case "birthYear":
                return input.equals("") || (input.matches("[0-9]+") && Integer.parseInt(input) >= 1900 && Integer.parseInt(input) <= 2024);
            default:
                return true;
        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
}