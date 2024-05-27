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
                return input == null || input.matches("[a-zA-Z]+");
            case "phoneNum":
                return input == null || input.matches("[0-9]+");
            case "email":
                return isEmailValid(input);
            default:
                return true;
        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        return email == null || pattern.matcher(email).matches();
    }
}