package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.example.MainTable.Person;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.cell.TextFieldTableCell;
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

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("firstName"));
        firstNameCol.setCellFactory(column -> new ValidatedCell("name"));
        firstNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setFirstName(t.getNewValue());
                        saveCSV();
                    }
                });

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("lastName"));
        lastNameCol.setCellFactory(column -> new ValidatedCell("name"));
        lastNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setLastName(t.getNewValue());
                        saveCSV();
                    }
                });

        TableColumn companyCol = new TableColumn("Company");
        companyCol.setMinWidth(100);
        companyCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("company"));
        companyCol.setCellFactory(column -> new ValidatedCell("name"));
        companyCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setCompany(t.getNewValue());
                        saveCSV();
                    }
                });

        TableColumn phoneNumCol = new TableColumn("Phone Number");
        phoneNumCol.setMinWidth(200);
        phoneNumCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("phoneNum"));
        phoneNumCol.setCellFactory(column -> new ValidatedCell("phoneNum"));
        phoneNumCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setPhoneNum(t.getNewValue());
                        saveCSV();
                    }
                });

        TableColumn emailCol = new TableColumn("Email");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("email"));
        emailCol.setCellFactory(column -> new ValidatedCell("email"));
        emailCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setEmail(t.getNewValue());
                        saveCSV();
                    }
                });

        TableColumn birthdayCol = new TableColumn("Birthday");
        birthdayCol.setMinWidth(100);
        birthdayCol.setCellValueFactory(
                new PropertyValueFactory<Person, String>("birthday"));
        birthdayCol.setCellFactory(TextFieldTableCell.forTableColumn());
        birthdayCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Person, String>>() {
                    @Override
                    public void handle(CellEditEvent<Person, String> t) {
                        ((Person) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())).setBirthday(t.getNewValue());
                        saveCSV();
                    }
                });

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, companyCol, phoneNumCol, emailCol, birthdayCol);

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
        final TextField addBirthday = new TextField();
        addBirthday.setMaxWidth(100);
        addBirthday.setPromptText("Birthday");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Person newPerson = new Person(
                        addFirstName.getText(),
                        addLastName.getText(),
                        addCompany.getText(),
                        addPhoneNum.getText(),
                        addEmail.getText(),
                        addBirthday.getText());
                ValidatedCell firstNameCell = new ValidatedCell("name");
                ValidatedCell lastNameCell = new ValidatedCell("name");
                ValidatedCell companyCell = new ValidatedCell("name");
                ValidatedCell phoneNumCell = new ValidatedCell("phoneNum");
                ValidatedCell emailCell = new ValidatedCell("email");
                if (firstNameCell.isValid(newPerson.getFirstName())
                        && lastNameCell.isValid(newPerson.getLastName())
                        && companyCell.isValid(newPerson.getCompany())
                        && phoneNumCell.isValid(newPerson.getPhoneNum())
                        && emailCell.isValid(newPerson.getEmail())) {
                    data.add(newPerson);
                    addFirstName.clear();
                    addLastName.clear();
                    addCompany.clear();
                    addPhoneNum.clear();
                    addEmail.clear();
                    addBirthday.clear();
                    saveCSV();
                }
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Person selected = table.getSelectionModel().getSelectedItem();
                table.getItems().remove(selected);
                saveCSV();
            }
        });

        hb.getChildren().addAll(addFirstName, addLastName, addCompany, addPhoneNum, addEmail, addBirthday, addButton,
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
                        person.getEmail() + "," +
                        person.getBirthday());
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
                if (fields.length == 6) {
                    data.add(new Person(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Person {

        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty company;
        private final SimpleStringProperty phoneNum;
        private final SimpleStringProperty email;
        private final SimpleStringProperty birthday;

        private Person(String firstName, String lastName, String company, String phoneNum, String email,
                String birthday) {
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.company = new SimpleStringProperty(company);
            this.phoneNum = new SimpleStringProperty(phoneNum);
            this.email = new SimpleStringProperty(email);
            this.birthday = new SimpleStringProperty(birthday);
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

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String input) {
            email.set(input);
        }

        public String getBirthday() {
            return birthday.get();
        }

        public void setBirthday(String input) {
            birthday.set(input);
        }
    }
}