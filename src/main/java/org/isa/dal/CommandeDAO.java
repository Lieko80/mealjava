package org.isa.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {

    MealwithDB mealwithDB = new MealwithDB();

    public CommandeDAO() throws SQLException {
    }

    public List<Commande> ListAll(){
        List<Commande> list = new ArrayList<>();
        try{
            Statement stm = mealwithDB.getConn().createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM orders");
            while (res.next()){
                Commande c = new Commande(
                        res.getInt("id"),
                        res.getDate("order_date"),
                        res.getString("delivery_address"),
                        res.getDate("delivery_date"),
                        res.getInt("cart_id"),
                        res.getString("firstname"),
                        res.getString("lastname"),
                        res.getString("email"));
                list.add(c);
            }
            stm.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    public List<Commande> ListByEmail(String email){
        List<Commande> list = new ArrayList<>();
        try{
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM orders WHERE email = ?");
            stm.setString(1, email);
            ResultSet res =stm.executeQuery ();

            while (res.next()){
                Commande c = new Commande(
                        res.getInt("id"),
                        res.getDate("order_date"),
                        res.getString("delivery_address"),
                        res.getDate("delivery_date"),
                        res.getInt("cart_id"),
                        res.getString("firstname"),
                        res.getString("lastname"),
                        res.getString("email"));
                list.add(c);
            }
            stm.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }


    public Commande findByEmail(String email)     {
        // méthode de recherche d'un enregistrement :
        Commande c = new Commande();

        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM orders WHERE email = ?");
            stm.setString(1, email);
            ResultSet result =stm.executeQuery ();
            while (result.next()) {
                c.setId(result.getInt("id"));
                c.setOrderDate(result.getDate("order_date"));
                c.setDeliveryAddress(result.getString("delivery_address"));
                c.setDeliveryDate(result.getDate("delivery_date"));
                c.setCartId(result.getInt("cart_id"));
            }
            stm.close();
            result.close();

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        return c;
    }


}
