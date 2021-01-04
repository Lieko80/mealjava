package org.isa.dal;

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private int zipcode;
    private String city;
    private String email;
    private String role;
    //private String phone;

    public User() {
    }

    //autre constructeur sans id et tous critères autorisant null as default value pour tests
    public User(String firstName, String lastName, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public User(int id, String firstName, String lastName, String address, int zipcode, String city, String email, String role) {

        //si on remplace this.field = field par setField(field), on peut appliquer des critères de vérification des champs au lieu d'accepter n'imp quel paramètre :
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
        setZipcode(zipcode);
        setCity(city);
        setEmail(email);
        setRole(role);
        //setPhone(phone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    /**
     * This method validates the e-mail address
     * and informs user with a message if error so that they can correct input
     * @ param email
     */
    public void setEmail(String email) {
        //if(email.matches("/^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$/"))
            this.email = email;
        /*else
            throw new IllegalArgumentException("This is not a valid e-mail address; please verify your input.");*/
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
/*
    public String getPhone() {
        return phone;
    }

    /**
     * This method validates the phone number
     * and informs user with a message if error so that they can correct input
     * @ param phone

    public void setPhone(String phone) {
        if(phone.matches("[0-9]{10}"))
            this.phone = phone;
        else
            throw new IllegalArgumentException("Phone numbers must be 10 digits long without any other characters.");
    }
*/
    @Override
    public String toString(){
        return firstName + lastName;
    }
}
