package org.isa.gui.Orders;

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
import org.isa.dal.CartItems;
import org.isa.dal.CartItemsDAO;
import org.isa.dal.Commande;
import org.isa.dal.CommandeDAO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    public TableView<Commande> table_list;
    public TableColumn<Commande, String> col_orders_number;
    public TableColumn<Commande, String> col_email;
    public TableColumn<Commande, String> col_firstname;
    public TableColumn<Commande, String> col_lastname;
    public TextField txt_search;
    public AnchorPane pane_details;
    public TextField txt_email;
    public TextField txt_firstname;
    public TextField txt_lastname;
    public TextField txt_address;
    public TextField txt_order;
    public TextField txt_delivery;
    public Button btn_order_detail;
    public VBox cartDetailsBox;
    public TextField orderId;
    public TableView<CartItems> cartItemsTable;
    public TableColumn<CartItems, Integer> colCartIngdts;
    public TableColumn<CartItems, Integer> colCartQuantity;
    public TableColumn<CartItems, Float> colCartPrice;
    public Label msgNoOrderChosen;

    ObservableList<Commande> model = FXCollections.observableArrayList();
    ObservableList<CartItems> cartItemsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        //initialisation pour remplir le tableview depuis la bdd
        CommandeDAO repo = null;
        try {
            repo = new CommandeDAO();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assert repo != null;
        model.addAll(repo.ListAll());
        table_list.setEditable(false);

        // Jonction du tableau avec les données
        col_orders_number.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Commande> filteredData = new FilteredList<>(model, p -> true);

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
        SortedList<Commande> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table_list.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table_list.setItems(sortedData);
    }

    public void ShowPanel(MouseEvent mouseEvent) {
        //récupérer utilisateur sélectionné :
        Commande c = (Commande) table_list.getSelectionModel().getSelectedItem();
        //afficher message erreur si rien de sélectionné dans le tableau :
            details(c);
        }

    public void details(Commande person) {
        try {
            //affiche la fenetre detail quand on click sur la liste
            if (table_list.isFocused()) {
                btn_order_detail.setVisible(true);
                cartItemsTable.getItems().clear();//clear entre chaque demande
                cartDetailsBox.setVisible(false);
            }

            if (person != null) {
                // Fill the labels with info from the User object given as parameter:
                txt_email.setText(person.getEmail());
                txt_firstname.setText(person.getFirstname());
                txt_lastname.setText(person.getLastname());
                txt_address.setText(person.getDeliveryAddress());
                txt_order.setText(String.valueOf(person.getOrderDate()));
                txt_delivery.setText(String.valueOf(person.getDeliveryDate()));
            } else {
                // Person is null, so remove all the text:
                txt_email.setText("");
                txt_firstname.setText("");
                txt_lastname.setText("");
                txt_address.setText("");
                txt_order.setText("");
                txt_delivery.setText("");

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void ShowCart(ActionEvent actionEvent) {
        Commande cde = table_list.getSelectionModel().getSelectedItem();
        //afficher message erreur si rien de sélectionné dans le tableau :
        if (cde == null) {
            msgNoOrderChosen.setVisible(true);
        } else {
            cartItemsTable.getItems().clear();//clear entre chaque demande
            msgNoOrderChosen.setVisible(false);
            cartDetailsBox.setVisible(true);
            orderId.setText(String.valueOf(table_list.getSelectionModel().getSelectedItem().getId()));

            //to show cartItemsTable :

            CartItemsDAO cartrepo = null;
            try {
                cartrepo = new CartItemsDAO();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert cartrepo != null;
            cartItemsList.addAll(cartrepo.ListByCart(table_list.getSelectionModel().getSelectedItem().getCartId()));
            cartItemsTable.setEditable(false);

            // Jonction du tableau avec les données
            colCartIngdts.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
            colCartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

            cartItemsTable.setItems(cartItemsList);
        }
    }
}

