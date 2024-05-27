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
            if (columnType.equals("birthYear")) {
                updateItem(input, false);
            }
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
                return input.equals("") || (input.matches("[0-9]+") && Integer.parseInt(input) >= 1900
                        && Integer.parseInt(input) <= 2024);
            default:
                return true;
        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    @Override
    public void updateItem(String birthYear, boolean empty) {
        super.updateItem(birthYear, empty);
        if (empty || birthYear.isEmpty()) {
            setText(null);
            setStyle("");
        } else {
            setText(birthYear);
            int age =  2024 - Integer.parseInt(birthYear);
            int decades = age / 10;

            // Change the cell color based on the number of decades
            if (decades < 2) {
                setStyle("-fx-background-color: #d8f3dc;");
            } else if (decades < 4) {
                setStyle("-fx-background-color: #b7e4c7;");
            } else if (decades < 6) {
                setStyle("-fx-background-color: #95d5b2;");
            } else if (decades < 8) {
                setStyle("-fx-background-color: #74c69d;");
            } else {
                setStyle("-fx-background-color: #52b788;");
            }

        }
    }
}