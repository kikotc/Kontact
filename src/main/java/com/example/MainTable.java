package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.MainTable.Person;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainTable extends Application {

    private TableView<Person> table = new TableView<Person>();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    final HBox hb = new HBox();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Kontact");
        stage.setWidth(840);
        stage.setHeight(520);

        final Label label = new Label("Kontacts Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setCellFactory(column -> new ValidatedCell("name"));
        firstNameCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setFirstName(event.getNewValue());
            saveCSV();
        });

        TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setCellFactory(column -> new ValidatedCell("name"));
        lastNameCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setLastName(event.getNewValue());
            saveCSV();
        });

        TableColumn<Person, String> companyCol = new TableColumn<>("Company");
        companyCol.setMinWidth(100);
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        companyCol.setCellFactory(column -> new ValidatedCell("name"));
        companyCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setCompany(event.getNewValue());
            saveCSV();
        });

        TableColumn<Person, String> phoneNumCol = new TableColumn<>("Phone Number");
        phoneNumCol.setMinWidth(200);
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        phoneNumCol.setCellFactory(column -> new ValidatedCell("phoneNum"));
        phoneNumCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setPhoneNum(event.getNewValue());
            saveCSV();
        });

        TableColumn<Person, ArrayList<String>> emailCol = new TableColumn<>("Emails");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("emails"));
        emailCol.setCellFactory(column -> new EmailColumn());

        TableColumn<Person, String> birthYearCol = new TableColumn<>("Birth Year");
        birthYearCol.setMinWidth(100);
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        birthYearCol.setCellFactory(column -> new ValidatedCell("birthYear"));
        birthYearCol.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setBirthYear(event.getNewValue());
            saveCSV();
        });

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, companyCol, phoneNumCol, emailCol, birthYearCol);

        final TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(100);
        addFirstName.setPromptText("First Name");
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(100);
        addLastName.setPromptText("Last Name");
        final TextField addCompany = new TextField();
        addCompany.setMaxWidth(100);
        addCompany.setPromptText("Company");
        final TextField addPhoneNum = new TextField();
        addPhoneNum.setMaxWidth(200);
        addPhoneNum.setPromptText("Phone Number");
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(200);
        addEmail.setPromptText("Email");
        final TextField addBirthYear = new TextField();
        addBirthYear.setMaxWidth(100);
        addBirthYear.setPromptText("Birth Year");

        final Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String firstName = addFirstName.getText();
            String lastName = addLastName.getText();
            String company = addCompany.getText();
            String phoneNum = addPhoneNum.getText();
            String birthYear = addBirthYear.getText();
            List<String> emails = Arrays.asList(addEmail.getText().split(","));

            if (!isEmailsValid(emails)) {
                return;
            }

            Person newPerson = new Person(firstName, lastName, company, phoneNum, birthYear, new ArrayList<>(emails));
            ValidatedCell firstNameCell = new ValidatedCell("name");
            ValidatedCell lastNameCell = new ValidatedCell("name");
            ValidatedCell companyCell = new ValidatedCell("name");
            ValidatedCell phoneNumCell = new ValidatedCell("phoneNum");
            ValidatedCell birthYearCell = new ValidatedCell("birthYear");

            if (firstNameCell.isValid(newPerson.getFirstName())
                    && lastNameCell.isValid(newPerson.getLastName())
                    && companyCell.isValid(newPerson.getCompany())
                    && phoneNumCell.isValid(newPerson.getPhoneNum())
                    && birthYearCell.isValid(newPerson.getBirthYear())) {
                data.add(newPerson);
                addFirstName.clear();
                addLastName.clear();
                addCompany.clear();
                addPhoneNum.clear();
                addEmail.clear();
                addBirthYear.clear();
                saveCSV();
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Person selected = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selected);
            saveCSV();
        });

        hb.getChildren().addAll(addFirstName, addLastName, addCompany, addPhoneNum, addEmail, addBirthYear, addButton,
                deleteButton);
        hb.setSpacing(2);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

        loadCSV();
    }

    public void saveCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Save.csv"))) {
            for (Person person : data) {
                writer.write(person.getFirstName() + "," +
                        person.getLastName() + "," +
                        person.getCompany() + "," +
                        person.getPhoneNum() + "," +
                        person.getBirthYear());
                for (String email : person.getEmails()) {
                    writer.write("," + email);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Save.csv"))) {
            String line;
            data.clear();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                ArrayList<String> emails = new ArrayList<>();
                for (int i = 5; i < fields.length; i++) {
                    emails.add(fields[i]);
                }
                if (fields.length >= 5) {
                    data.add(new Person(fields[0], fields[1], fields[2], fields[3], fields[4], emails));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailsValid(List<String> emails) {
        for (String email : emails) {
            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$") && !email.equals("")) {
                return false;
            }
        }
        return true;
    }

    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty company;
        private final SimpleStringProperty phoneNum;
        private final SimpleStringProperty birthYear;
        private final ArrayList<String> emails;

        private Person(String firstName, String lastName, String company, String phoneNum,
                String birthYear, ArrayList<String> emails) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.company = new SimpleStringProperty(company);
            this.phoneNum = new SimpleStringProperty(phoneNum);
            this.birthYear = new SimpleStringProperty(birthYear);
            this.emails = emails;
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String input) {
            firstName.set(input);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String input) {
            lastName.set(input);
        }

        public String getCompany() {
            return company.get();
        }

        public void setCompany(String input) {
            company.set(input);
        }

        public String getPhoneNum() {
            return phoneNum.get();
        }

        public void setPhoneNum(String input) {
            phoneNum.set(input);
        }

        public String getBirthYear() {
            return birthYear.get();
        }

        public void setBirthYear(String input) {
            birthYear.set(input);
        }

        public ArrayList<String> getEmails() {
            return emails;
        }

        public void setEmails(List<String> input) {
            emails.clear();
            emails.addAll(input);
        }
    }
}
