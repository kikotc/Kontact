package com.example;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

import com.example.MainTable.Person;

public class EmailColumn extends TableCell<Person, ArrayList<String>> {

    private final VBox vbox = new VBox();
    private final HBox hbox = new HBox();
    private final TextField newEmailField = new TextField();
    private final Button addButton = new Button("Add Email");

    public EmailColumn() {
        newEmailField.setPromptText("New Email");
        addButton.setOnAction(e -> addNewEmail());
        hbox.getChildren().addAll(newEmailField, addButton);
    }

    @Override
    protected void updateItem(ArrayList<String> emails, boolean empty) {
        super.updateItem(emails, empty);
        if (empty || emails == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                vbox.getChildren().clear();
                int[] index = {0};
                for (String email : emails) {
                    TextField emailField = new TextField(email);
                    final int emailIndex = index[0];
                    emailField.setOnAction(event -> updateEmail(emailIndex, emailField.getText()));
                    vbox.getChildren().add(emailField);
                    index[0]++;
                }
                vbox.getChildren().add(hbox);
                setGraphic(vbox);
                setText(null);
            } else {
                setText(String.join(", ", emails));
                setGraphic(null);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (getItem() != null) {
            vbox.getChildren().clear();
            int[] index = {0};
            for (String email : getItem()) {
                TextField emailField = new TextField(email);
                final int emailIndex = index[0];
                emailField.setOnAction(event -> updateEmail(emailIndex, emailField.getText()));
                vbox.getChildren().add(emailField);
                index[0]++;
            }
            vbox.getChildren().add(hbox);
            setGraphic(vbox);
            setText(null);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        ArrayList<String> emails = getItem();
        if (emails != null) {
            setText(String.join(", ", emails));
        } else {
            setText(null);
        }
        setGraphic(null);
    }

    private void addNewEmail() {
        String newEmail = newEmailField.getText();
        if (!newEmail.isEmpty() && isEmailValid(newEmail)) {
            Person person = getTableView().getItems().get(getIndex());
            person.getEmails().add(newEmail);
            newEmailField.clear();
            getTableView().refresh();
        }
    }

    private void updateEmail(int index, String newEmail) {
        if (isEmailValid(newEmail)) {
            Person person = getTableView().getItems().get(getIndex());
            person.getEmails().set(index, newEmail);
            getTableView().refresh();
        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(regex);
    }
}
