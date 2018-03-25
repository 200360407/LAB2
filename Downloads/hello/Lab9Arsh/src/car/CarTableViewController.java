/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package car;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CarTableViewController implements Initializable {

    @FXML
    private TableView<Car> carTable;
    @FXML
    private TableColumn<Car, String> makeColumn;
    @FXML
    private TableColumn<Car, String> modelColumn;
    @FXML
    private TableColumn<Car, Float> mileageColumn;
    @FXML
    private TableColumn<Car, Integer> yearColumn;

    @FXML
    private Label minimumYear;
    @FXML
    private Label maximumYear;

    @FXML
    private Label minimumCurrentYear;
    @FXML
    private Label maximumCurrentYear;

    @FXML
    private Slider minimumSlider;
    @FXML
    private Slider maximumSlider;

    @FXML
    private ComboBox<String> makeComboBox;

    int maxSlider, minSlider, currentMax, currentMin;

    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;

    // This method is used to initialise variables and other components of scene
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<Car, Float>("mileage"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Car, Integer>("year"));
        try {
            loadComboBox();
            loadCars();
            loadSliders();
        } catch (SQLException ex) {
        }

    }
    // This method runs query to populate table wiith all cars present in database
    private void loadCars() throws SQLException {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Arshpreet", "root", "");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM cars");
            while (resultSet.next()) {
                Car c = new Car(resultSet.getString("make"), resultSet.getString("model"), resultSet.getFloat("mileage"), resultSet.getInt("year"));
                cars.add(c);
            }
            carTable.getItems().addAll(cars);
        } catch (Exception ex) {

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
    // this method executes db query that results in initialising sliders max min and current value 
    public void loadSliders() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Arshpreet", "root", "");
        statement = conn.createStatement();
        resultSet = statement.executeQuery("Select MIN(year) FROM cars");
        resultSet.next();
        minSlider = resultSet.getInt("MIN(year)");
        resultSet = statement.executeQuery("Select MAX(year) FROM cars");
        resultSet.next();
        maxSlider = resultSet.getInt("MAX(year)");
        currentMax = maxSlider;
        currentMin = minSlider;
        minimumSlider.valueProperty().addListener((obs, oldVal, newVal)
                -> minimumSlider.setValue(newVal.intValue()));
        maximumSlider.valueProperty().addListener((obs, oldVal, newVal)
                -> maximumSlider.setValue(newVal.intValue()));
        minimumSlider.setMax(maxSlider);
        minimumSlider.setMin(minSlider);
        minimumSlider.setValue(Double.parseDouble(String.valueOf(minSlider)));
        minimumYear.setText(String.valueOf(minSlider));
        minimumCurrentYear.setText(String.valueOf(minSlider));
        minimumCurrentYear.textProperty().bindBidirectional(minimumSlider.valueProperty(), NumberFormat.getNumberInstance());
        maximumSlider.setMax(maxSlider);
        maximumSlider.setMin(minSlider);
        maximumSlider.setValue(Double.parseDouble(String.valueOf(maxSlider)));
        maximumYear.setText(String.valueOf(maxSlider));
        maximumCurrentYear.setText(String.valueOf(maxSlider));
        maximumCurrentYear.textProperty().bindBidirectional(maximumSlider.valueProperty(), NumberFormat.getNumberInstance());
    }
    // this method updates value for sliders when changes are made and executes query accordingly to populate Table View
    public void updateSliders() throws SQLException {
        currentMin = (int) minimumSlider.getValue();
        currentMax = (int) maximumSlider.getValue();
        minimumSlider.setValue(currentMin);
        maximumSlider.setValue(currentMax);
        minimumSlider.setMax(currentMax);
        maximumSlider.setMin(currentMin);
        minimumSlider.setMin(minSlider);
        maximumSlider.setMax(maxSlider);
        minimumCurrentYear.setText(String.valueOf(currentMin));
        maximumCurrentYear.setText(String.valueOf(currentMax));
        minimumCurrentYear.textProperty().bindBidirectional(minimumSlider.valueProperty(), NumberFormat.getNumberInstance());
        maximumCurrentYear.textProperty().bindBidirectional(maximumSlider.valueProperty(), NumberFormat.getNumberInstance());
        ObservableList<Car> cars = FXCollections.observableArrayList();
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Arshpreet", "root", "");
        statement = conn.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM cars where year >= " + currentMin + " && year <= " + currentMax);
        while (resultSet.next()) {
            Car c = new Car(resultSet.getString("make"), resultSet.getString("model"), resultSet.getFloat("mileage"), resultSet.getInt("year"));
            cars.add(c);
        }
        carTable.setItems(cars);
    }
    // This method is used to populate combo box by executing DB query
    public void loadComboBox() throws SQLException {
        try {
            ObservableList list = FXCollections.observableArrayList();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Arshpreet", "root", "");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("Select distinct make from cars");
            while (resultSet.next()) {
                list.add(resultSet.getString("make"));
            }
            makeComboBox.setItems(list);

        } catch (Exception ex) {

        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
    // this method updates table accordingly with slider values and selected combo box value
    public void updateDBComboBox() throws SQLException {
        ObservableList<Car> cars = FXCollections.observableArrayList();
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Arshpreet", "root", "");
        statement = conn.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM cars where year >= " + currentMin + " && year <= " + currentMax + "&& make = '" + makeComboBox.getValue().toString() + "'");
        while (resultSet.next()) {
            Car c = new Car(resultSet.getString("make"), resultSet.getString("model"), resultSet.getFloat("mileage"), resultSet.getInt("year"));
            cars.add(c);
        }
        carTable.setItems(cars);
    }
    // this method changes scene to doneView
    public void doneButtonClicked(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("doneView.FXML"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("Happy Making Image");
        stage.setScene(scene);
        stage.show();
    }
}
