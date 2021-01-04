package org.isa.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    MealwithDB mealwithDB = new MealwithDB();

    public UserDAO() throws SQLException {
    }

    public void addUser(User person){
        try {

            PreparedStatement stm = mealwithDB.getConn().prepareStatement("INSERT INTO users (firstname, lastname, email, roles, password) VALUES (?, ?, ?, ?, ?)");

            stm.setString(1, person.getFirstName());
            stm.setString(2, person.getLastName());
            stm.setString(3, person.getEmail());
            stm.setString(4, person.getRole());
            stm.setString(5, "fauxpasswordpourtests");

            stm.execute();

            stm.close();
            mealwithDB.getConn().close();

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    public List<User> ListAll() {
        List<User> list = new ArrayList<>();
        try {
            Statement stm = mealwithDB.getConn().createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM users");
            while (res.next()) {
                User c = new User(
                        res.getInt("id"),
                        res.getString("firstname"),
                        res.getString("lastname"),
                        res.getString("address"),
                        res.getInt("zipcode"),
                        res.getString("city"),
                        res.getString("email"),
                        res.getString("roles")
                        //, res.getString("phone")
                );
                list.add(c);
            }
            stm.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    public User find(int id) {
        // méthode de recherche d'un enregistrement :
        User c = new User();

        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM users WHERE id = ?");
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                c.setId(result.getInt("id"));
                c.setFirstName(result.getString("firstname"));
                c.setLastName(result.getString("lastname"));
                c.setAddress(result.getString("address"));
                c.setZipcode(result.getInt("zipcode"));
                c.setCity(result.getString("city"));
                c.setEmail(result.getString("email"));
                c.setRole(result.getString("roles"));
                //c.setPhone(result.getString("phone"));
            }
            stm.close();
            result.close();

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        return c;
    }

    public User findByEmail(String email) {
        // méthode de recherche d'un enregistrement :
        User c = new User();

        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("SELECT * FROM users WHERE email = ?");
            stm.setString(1, email);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                c.setId(result.getInt("id"));
                c.setFirstName(result.getString("firstname"));
                c.setLastName(result.getString("lastname"));
                c.setAddress(result.getString("address"));
                c.setZipcode(result.getInt("zipcode"));
                c.setCity(result.getString("city"));
                c.setEmail(result.getString("email"));
                c.setRole(result.getString("roles"));
                //c.setPhone(result.getString("phone"));
            }
            stm.close();
            result.close();

        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        return c;
    }


    public void update(User person) {
        // méthode d'édition
        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("UPDATE users SET email = ?, firstname = ?, lastname = ?, address = ?, zipcode = ?, city = ?, roles = ? WHERE id = ?;");

            stm.setString(1, person.getEmail());
            stm.setString(2, person.getFirstName());
            stm.setString(3, person.getLastName());
            stm.setString(4, person.getAddress());
            stm.setInt(5, person.getZipcode());
            stm.setString(6, person.getCity());
            stm.setString(7, person.getRole());
            stm.setInt(8, person.getId());

            stm.execute();

            stm.close();

            System.out.println("la modification s’est bien effectuée");

        } catch (Exception e) {
            System.out.println("Erreur lors de la modification 'utilisateur'");
            System.out.println(e.getMessage());
        }
    }

    public void delete(User person) {
        // méthode de suppression - req type "DELETE FROM table_name WHERE condition;"
        try {
            PreparedStatement stm = mealwithDB.getConn().prepareStatement("DELETE FROM users WHERE id = ?;");

            stm.setInt(1, person.getId());

            stm.execute();

            stm.close();
            System.out.println("la suppression s’est bien effectuée");

        }
        catch (Exception e) {
            System.out.println("Erreur lors de la suppression 'utilisateur': cet utilisateur a des commandes en cours!");
            System.out.println(e.getMessage());
        }

    }

}