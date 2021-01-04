package org.isa.dal;

import java.sql.Date;

public class Commande {

    private int id;
    private Date orderdate;
    private String deliveryaddress;
    private Date deliverydate;
    private int cartId;
    private String firstname;
    private String lastname;
    private String email;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderdate;
    }

    public void setOrderDate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getDeliveryAddress() {
        return deliveryaddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryaddress = deliveryAddress;
    }

    public Date getDeliveryDate() {
        return deliverydate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliverydate = deliveryDate;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Commande() {}

    public Commande(int id, Date orderDate, String deliveryAddress, Date deliveryDate, int cartId, String firstname, String lastname, String email) {
        this.id = id;
        this.orderdate = orderDate;
        this.deliveryaddress = deliveryAddress;
        this.deliverydate = deliveryDate;
        this.cartId = cartId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }


}
