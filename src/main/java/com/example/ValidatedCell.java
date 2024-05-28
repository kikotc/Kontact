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
            case "birthYear":
                return input.equals("") || (input.matches("[0-9]+") && Integer.parseInt(input) >= 1900
                        && Integer.parseInt(input) <= 2024);
            default:
                return true;
        }
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (columnType.equals("birthYear")) {
            if (empty || item.isEmpty()) {
                setText(null);
                setStyle("");
            } else {
                setText(item);
                try {
                    int age = 2024 - Integer.parseInt(item);
                    int decades = age / 10;

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
                } catch (NumberFormatException e) {
                    setText(null);
                    setStyle("");
                }
            }
        }
    }
}
