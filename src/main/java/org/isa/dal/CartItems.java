package org.isa.dal;

public class CartItems {

    private int id;
    private int ingredientId;
    private int cartId;
    private int quantity;
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CartItems() {
    }

    public CartItems(int id, int ingredientId, int cartId, int quantity, float price) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.cartId = cartId;
        this.quantity = quantity;
        this.price = price;
    }
}
