package org.isa.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartItemsDAO {

    MealwithDB mealwithDB = new MealwithDB();

    public CartItemsDAO() throws SQLException {
    }

    public List<CartItems> ListAll(){
        List<CartItems> list = new ArrayList<>();
        try{
            Statement stm = mealwithDB.getConn().createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM cart_items");
            while (res.next()){
                CartItems c = new CartItems(
                        res.getInt("id"),
                        res.getInt("id_ingredient_id"),
                        res.getInt("id_cart_id"),
                        res.getInt("quantity"),
                        res.getFloat("price_when_bought"));
                list.add(c);
            }
            stm.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    //essai 16/12 18h
    public List<CartItems> ListByCart(int id){
        List<CartItems> list = new ArrayList<>();
        try{
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM cart_items WHERE id_cart_id = ?");
            stm.setInt(1, id);
            ResultSet res =stm.executeQuery ();
            while (res.next()){
                CartItems c = new CartItems(
                        res.getInt("id"),
                        res.getInt("id_ingredient_id"),
                        res.getInt("id_cart_id"),
                        res.getInt("quantity"),
                        res.getFloat("price_when_bought"));
                list.add(c);
            }
            stm.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }


    public CartItems find(int id)     {
        // méthode de recherche d'un enregistrement :
        CartItems c = new CartItems();

        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM cart_items WHERE id_cart_id = ?");
            stm.setInt(1, id);
            ResultSet result =stm.executeQuery ();
            while (result.next()) {
                c.setId(result.getInt("id"));
                c.setIngredientId(result.getInt("id_ingredient_id"));
                c.setCartId(result.getInt("id_cart_id"));
                c.setQuantity(result.getInt("quantity"));
                c.setPrice(result.getFloat("price_when_bought"));
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
