package org.example;
import java.sql.*;
import java.util.Objects;

class User{
    private String username;
    private String password;
    private String email;

    public User() {

    }
    public void setUsername(String username) {

        if (username.length() < 5) {
            System.err.println("Username too small");
        } else {

            this.username = username;
        }
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password){
        if (password.length() < 8 ){
            System.err.println("Password too short");
        } else {
            this.password = password;
        }

    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

}

class Account extends User {
    private int pin;
    private int amount;

    public String owner() {
        return super.getUsername();
    }
    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

public class Main {

    public static Connection connectToDB(){
        Connection connection = null;
        System.out.println("Connecting to DB...");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:atm.db");
            System.out.println("Successfully connected to DB. \n");
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
        return connection;
    }

    public static void dropTables(String tableName) throws SQLException {
        Connection connection = connectToDB();
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);


        statement.executeUpdate("drop table if exists " + tableName);
        System.out.println("Successfully dropped table " + tableName);
    }

    public static void createTable(String tableName) throws SQLException {
        Connection connection = connectToDB();

        Statement statement = connection.createStatement();
        statement.executeUpdate("create table " + tableName + " (id integer, username string, email string, password string)");

        System.out.println("Successfully created new table called " + tableName);
    }

    public static void createAccountTable() throws SQLException {
        Connection connection = connectToDB();

        Statement statement = connection.createStatement();
        statement.executeUpdate("create table account (id integer, username string, pin integer, amount integer)");

        System.out.println("Successfully create new Account table");
    }

    public static void insertToTable(String newTableName, String username, String email, String password) throws SQLException {
        Connection connection = connectToDB();

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        String insert = "insert into " + newTableName + " values(" + 1 + ", " + user.getUsername() + ", " + user.getEmail() + ", " + user.getPassword() +")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(insert);

        System.out.println("Successfully inserted new records!!");
    }

    public static void insertToAccountTable(String owner, int pin, int amount) throws SQLException {
        Connection connection = connectToDB();

        Account account = new Account();
        account.setPin(pin);
        account.setAmount(amount);
        account.setUsername(owner);

        Statement statement = connection.createStatement();
        String insert = "insert into account " + " values(" + 1 + ", " + account.owner() + ", " + account.getPin() + ", " + account.getAmount() +")";
        statement.executeUpdate(insert);

        System.out.println("Records updated for account table");
    }

    public static boolean checkPin(String username, int pin) throws SQLException {
        // open connection to the db
        Connection connection = connectToDB();

        // execute a query to get a particular user by matching the username
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from user inner join account on user.username=account.username");

        // check if the pin is correct by matching the pin with the existing pin
        int userPin = 0;
        while (rs.next()) {
            if (Objects.equals(rs.getString("username"), username)) {
                userPin += rs.getInt("pin");
            }
            else {
                break;
            }
        }
        return userPin == pin;
    }

    public static boolean checkAccountBalance(String username, int amountForWithdrawal) throws SQLException {
        Connection connection = connectToDB();

        Statement statement = connection.createStatement();

        // SQL Query to return a user with the same username on both the account ans user table.
        ResultSet rs = statement.executeQuery("select * from user inner join account on user.username=account.username");

        int usersBalance = 0;

        while (rs.next()) {

            if (Objects.equals(rs.getString("username"), username)) {
                usersBalance += rs.getInt("amount");
            }
        }

        return amountForWithdrawal > usersBalance;
    }

    public static void main(String[] args) {
        // TODO
        // Add logic to handle updating user amount/account balance after performing a withdrawal.
        // Make the username field on both the account and user tables unique to reduce redundant code.
        // Add logic to handle the ATM system withdrawal.
        // Complete the logic on checkPin function.

    }
}