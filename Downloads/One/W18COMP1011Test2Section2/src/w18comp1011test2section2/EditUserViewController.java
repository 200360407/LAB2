package w18comp1011test2section2;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jwright
 */
public class EditUserViewController implements Initializable {

    @FXML
    private TextField contactIDTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField streetAddressTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private Label errorMsgLabel;
    @FXML
    private ComboBox<String> firstNameComboBox;

    /**
     * Your goal for test 2 is to create this method. It should read from the
     * contactIDTextField and update the view with the infromation stored in the
     * database.
     */
    Connection conn = null;
    Statement statement = null;
    ResultSet resultSet = null;
    int id;

    public void getUserData() throws SQLException {
        updateDB();
        try {
            errorMsgLabel.setText("");
            id = Integer.parseInt(contactIDTextField.getText());
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM contacts Where contactId = " + id);
            resultSet.next();
            firstNameTextField.setText(resultSet.getString("firstName"));
            lastNameTextField.setText(resultSet.getString("lastName"));
            streetAddressTextField.setText(resultSet.getString("streetAddress"));
            cityTextField.setText(resultSet.getString("city"));
            firstNameTextField.setDisable(false);
            lastNameTextField.setDisable(false);
            streetAddressTextField.setDisable(false);
            cityTextField.setDisable(false);

        } catch (NumberFormatException ex) {
            errorMsgLabel.setText("Contact Field can only have numbers");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                errorMsgLabel.setText(ex.getMessage());
            }
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        firstNameTextField.setDisable(true);
        lastNameTextField.setDisable(true);
        streetAddressTextField.setDisable(true);
        cityTextField.setDisable(true);
        errorMsgLabel.setText("");

        updateComboBoxFromDB(); //this is a bonus to populate the combobox methods

    }

    public void comboBoxWasUpdated() {
        updateDB();
        String name = firstNameComboBox.getValue().toString();
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM contacts where firstName = '" + name + "'");
            resultSet.next();
            contactIDTextField.setText(String.valueOf(resultSet.getInt("contactID")));
            firstNameTextField.setText(resultSet.getString("firstName"));
            lastNameTextField.setText(resultSet.getString("lastName"));
            streetAddressTextField.setText(resultSet.getString("streetAddress"));
            cityTextField.setText(resultSet.getString("city"));
            firstNameTextField.setDisable(false);
            lastNameTextField.setDisable(false);
            streetAddressTextField.setDisable(false);
            cityTextField.setDisable(false);

        } catch (Exception ex) {
            errorMsgLabel.setText(ex.toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                errorMsgLabel.setText(ex.getMessage());
            }
        }
    }

    /**
     * Bonus: The goal here is to pull all the first names from the database and
     * populate the comboBox with the first names.
     *
     * @throws SQLException
     */
    public void updateComboBoxFromDB() {
        try {
            ObservableList list = FXCollections.observableArrayList();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT firstName FROM contacts");
            while (resultSet.next()) {
                list.add(resultSet.getString("firstName"));
            }
            firstNameComboBox.setItems(list);
        } catch (Exception ex) {
            errorMsgLabel.setText(ex.toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                errorMsgLabel.setText(ex.getMessage());
            }
        }
    }

    public void updateDB() {
        PreparedStatement preparedStatement = null;

        try {

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "123");

            String sql = "UPDATE contacts SET firstName = ?, lastName = ?, streetAddress = ?, city = ? WHERE contactID = " + id;
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstNameTextField.getText());
            preparedStatement.setString(2, lastNameTextField.getText());
            preparedStatement.setString(3, streetAddressTextField.getText());
            preparedStatement.setString(4, cityTextField.getText());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception ex) {
                errorMsgLabel.setText(ex.getMessage());
            }
        }

    }

}
