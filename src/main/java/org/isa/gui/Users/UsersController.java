package org.isa.gui.Users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.isa.dal.*;

import javax.swing.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    public TableView<User> table_list;
    public AnchorPane pane_details;
    public TextField txt_search;
    public TextField txt_email;
    public TextField txt_firstname;
    public TextField txt_lastname;
    public TextField txt_address;
    public TextField txt_zipcode;
    public TextField txt_city;
    public ComboBox<String> combo_role;
    public Button btn_cancel;
    public Button btn_modify;
    public Button btn_delete;
    public Label error_email;
    public Label error_firstname;
    public Label error_lastname;
    public Label error_address;
    public Label error_zipcode;
    public Label error_city;
    public TableColumn<User, String> col_email;
    public TableColumn<User, String> col_firstname;
    public TableColumn<User, String> col_lastname;
    public TableColumn<User, String> col_city;
    public Label msgErreur;
    public Button btn_details;
    public Button btn_orders;
    public AnchorPane ordersPane;
    public TextField orderId;
    public TableView<Commande> ordersTable;
    public TableColumn<Commande, Integer> colOrderId;
    public TableColumn<Commande, LocalDate> colOrderDate;
    public TableColumn<Commande, LocalDate> colDeliveryDate;
    public TableColumn<Commande, String> colDeliveryAddress;
    public TableView<CartItems> cartItemsTable;
    public TableColumn<CartItems, Integer> colCartIngdts;
    public TableColumn<CartItems, Integer> colCartQuantity;
    public TableColumn<CartItems, Float> colCartPrice;
    public VBox cartDetailsBox;
    public Label labelNoOrders;
    public Label msgNoOrderChosen;
    public Label user;

    ObservableList<User> model = FXCollections.observableArrayList();
    ObservableList<String> comboList = FXCollections.observableArrayList("Utilisateur", "Administrateur");
    ObservableList<Commande> ordersList = FXCollections.observableArrayList();
    ObservableList<CartItems> cartItemsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle rb) {

        //initialisation pour remplir le tableview depuis la bdd
        UserDAO repo = null;
        try {
            repo = new UserDAO();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assert repo != null;
        model.addAll(repo.ListAll());
        table_list.setEditable(false);

        // Jonction du tableau avec les données
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("city"));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<User> filteredData = new FilteredList<>(model, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txt_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all users.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare email of every user with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches email
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<User> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table_list.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table_list.setItems(sortedData);
    }

    public void details(User person) {
        try {
            //affiche la fenetre detail quand on click sur la liste
            if (table_list.isFocused()) {
                pane_details.setVisible(true);
            }

            if (person != null) {
                // Fill the labels with info from the User object given as parameter:
                txt_email.setText(person.getEmail());
                txt_firstname.setText(person.getFirstName());
                txt_lastname.setText(person.getLastName());
                txt_address.setText(person.getAddress());
                txt_zipcode.setText(String.valueOf(person.getZipcode()));
                txt_city.setText(person.getCity());
                combo_role.setItems(comboList);
                int index = 0;
                if(person.getRole().equals("[\"ROLE_USER\"]")){
                    index = 0;
                }
                if(person.getRole().equals("[\"ROLE_ADMIN\"]")){
                    index = 1;
                }
                combo_role.getSelectionModel().select(index);
            } else {
                // Person is null, so remove all the text:
                txt_email.setText("");
                txt_firstname.setText("");
                txt_lastname.setText("");
                txt_address.setText("");
                txt_zipcode.setText("");
                txt_city.setText("");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void showDetails(ActionEvent actionEvent) {
        //rendre le ordersPane caché:
        ordersPane.setVisible(false);
        //récupérer utilisateur sélectionné :
        User c = table_list.getSelectionModel().getSelectedItem();
        //afficher message erreur si rien de sélectionné dans le tableau :
        if (c == null) {
            msgErreur.setVisible(true);
        } else {
            msgErreur.setVisible(false);
            pane_details.setVisible(true);
            //remplir champs avec infos:
            details(c);
        }
    }

    public void modify() throws SQLException {

        if (verfiEmail() && verfiFirst() && verfiLast() && verfiCity() && verfiPost()){
            UserDAO repo = new UserDAO();
            //creation de la boite d'alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modification");
            alert.setHeaderText("Modifier le Client " + txt_firstname.getText() + " ?");
            alert.setContentText("Etes-vous s\u00FBr de cela ?");

            //si ok suppression de la ligne, sinon rien
            Optional<ButtonType> result = alert.showAndWait();

            //noinspection OptionalGetWithoutIsPresent
            if (result.get() == ButtonType.OK) {
                //récupérer l'utilisateur choisi :
                User c = table_list.getSelectionModel().getSelectedItem();
                c.setEmail(txt_email.getText());
                c.setFirstName(txt_firstname.getText());
                c.setLastName(txt_lastname.getText());
                c.setAddress(txt_address.getText());
                c.setZipcode(Integer.parseInt(txt_zipcode.getText()));
                c.setCity(txt_city.getText());
                String value = combo_role.getValue();
                if (value.equals("Administrateur")) {
                    value = "[\"ROLE_ADMIN\"]";
                } else if (value.equals("Utilisateur")) {
                    value = "[\"ROLE_USER\"]";
                }
                c.setRole(value);//valeur à modifier en fonction du choix dans la combobox

                repo.update(c);
                //mise à jour du tableview pour montrer modification :
                int selectedIndex = table_list.getSelectionModel().getSelectedIndex();
                table_list.getItems().set(selectedIndex, c);
            }
        }

    }

    public void delete() throws SQLException {
        UserDAO repo = new UserDAO();

        //creation de la boite d'alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setHeaderText("Supprimer l'utilisateur " + txt_firstname.getText() + " " + txt_lastname.getText() + " ?");
        alert.setContentText("Etes-vous sûr de cela ?");

        //si ok suppression de la ligne, sinon rien
        Optional<ButtonType> result = alert.showAndWait();

        //noinspection OptionalGetWithoutIsPresent
        if (result.get() == ButtonType.OK) {

            User c = table_list.getSelectionModel().getSelectedItem();

            //supprimer de la bdd :
            repo.delete(c);
            //mise à jour du tableview pour montrer suppression :
            model.remove(c);
            table_list.setItems(model);

            //refermer panneau détails :
            pane_details.setVisible(false);
        }
    }

    public void Cancel() {
        //quand annuler vide les textfield et ferme la fenetre detail
        pane_details.setVisible(false);
        txt_email.clear();
        txt_firstname.clear();
        txt_lastname.clear();
        txt_address.clear();
        txt_zipcode.clear();
        txt_city.clear();
    }

    //regex mail
    public boolean Checkmail(String check) {
        return check.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$");
    }
    //regex firstname lastname
    public boolean CheckString(String check) {
        return check.matches("^[A-Za-z ]+$");
    }
    //regex firstname postale
    public boolean CheckPost(String check) {
        return check.matches("^((0[1-9])|([1-8][0-9])|(9[0-8])|(2A)|(2B))[0-9]{3}$");
    }


    //verif regex email
    public boolean verfiEmail() {
        //si ok
        if (Checkmail(txt_email.getText())) {
            error_email.setVisible(false);
            error_email.setText("");
            return true;
            //si pas ok
        } else {
            error_email.setText("exemple@dev.com");
            error_email.setVisible(true);
        }
        return false;
    }
    //verif regex firstname
    public boolean verfiFirst() {
        //si ok
        if (CheckString(txt_firstname.getText())) {
            error_firstname.setVisible(false);
            error_firstname.setText("");
            return true;
            //si pas ok
        } else {
            error_firstname.setText("faux");
            error_firstname.setVisible(true);
        }
        return false;
    }
    //verif regex lastname
    public boolean verfiLast() {
        //si ok
        if (CheckString(txt_lastname.getText())) {
            error_lastname.setVisible(false);
            error_lastname.setText("");
            return true;
            //si pas ok
        } else {
            error_lastname.setText("faux");
            error_lastname.setVisible(true);
        }
        return false;
    }
    //verif regex city
    public boolean verfiCity() {
        //si ok
        if (CheckString(txt_city.getText())) {
            error_city.setVisible(false);
            error_city.setText("");
            return true;
            //si pas ok
        } else {
            error_city.setText("faux");
            error_city.setVisible(true);
        }
        return false;
    }
    //verif regex code postale
    public boolean verfiPost() {
        //si ok
        if (CheckPost(txt_zipcode.getText())) {
            error_zipcode.setVisible(false);
            error_zipcode.setText("");
            return true;
            //si pas ok
        } else {
            error_zipcode.setText("faux");
            error_zipcode.setVisible(true);
        }
        return false;
    }

    public void showOrders(ActionEvent actionEvent) {
        msgNoOrderChosen.setVisible(false);
        User c = table_list.getSelectionModel().getSelectedItem();
        //afficher message erreur si rien de sélectionné dans le tableau :
        if (c == null) {
            msgErreur.setVisible(true);
        } else {
            ordersTable.getItems().clear();//clear entre chaque demande
            cartDetailsBox.setVisible(false);//remettre la box detail invisible
            ordersPane.setVisible(true);
            msgErreur.setVisible(false);
            pane_details.setVisible(false);
            user.setText("Commandes de " + table_list.getSelectionModel().getSelectedItem().getFirstName() + " " + table_list.getSelectionModel().getSelectedItem().getLastName());

            //for ordersTable :

            //initialisation pour remplir le tableview depuis la bdd
            CommandeDAO ordersrepo = null;
            try {
                ordersrepo = new CommandeDAO();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert ordersrepo != null;

            //if no orders for chosen user, show label below empty tableview :

            if (ordersrepo.ListByEmail(table_list.getSelectionModel().getSelectedItem().getEmail()).size() == 0) {
                labelNoOrders.setVisible(true);
            } else {
                labelNoOrders.setVisible(false);
            }

            ordersList.addAll(ordersrepo.ListByEmail(table_list.getSelectionModel().getSelectedItem().getEmail()));
            ordersTable.setEditable(false);

            // Jonction du tableau avec les données
            colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
            colDeliveryAddress.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));

            ordersTable.setItems(ordersList);
        }
    }
    public void showcart(MouseEvent mouseEvent) {
        Commande cde = ordersTable.getSelectionModel().getSelectedItem();
        //afficher message erreur si rien de sélectionné dans le tableau :
        if (cde == null) {
            msgNoOrderChosen.setVisible(true);
        } else {
            cartItemsTable.getItems().clear();//clear entre chaque demande
            msgNoOrderChosen.setVisible(false);
            cartDetailsBox.setVisible(true);
            orderId.setText(String.valueOf(ordersTable.getSelectionModel().getSelectedItem().getId()));

            //to show cartItemsTable :

            CartItemsDAO cartrepo = null;
            try {
                cartrepo = new CartItemsDAO();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert cartrepo != null;
            cartItemsList.addAll(cartrepo.ListByCart(ordersTable.getSelectionModel().getSelectedItem().getCartId()));
            cartItemsTable.setEditable(false);

            // Jonction du tableau avec les données
            colCartIngdts.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
            colCartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            cartItemsTable.setItems(cartItemsList);
        }
    }
}
