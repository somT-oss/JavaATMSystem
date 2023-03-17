package org.example;
import javax.swing.plaf.nimbus.State;
import java.sql.*;

class User{
    private String username;
    private String password;
    private String email;

    public User() {

    };
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
    };

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

    public static void main(String[] args) throws SQLException {

        Connection connection = connectToDB();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from user inner join account on user.username=account.username");
        int defaultAmount = 0;
        while(rs.next())
            {
                // read the result set
                System.out.println("username = " + rs.getString("username"));
                System.out.println("id = " + rs.getInt("id"));
                System.out.println("amount = " + rs.getInt("amount"));
                System.out.println("amount = " + rs.getInt("pin"));

                defaultAmount += rs.getInt("amount");
            }
        System.out.println(defaultAmount);


//        connectToDB();
//        dropTables("account");
//        createTable("user");
//        createAccountTable();
//        insertToTable("'user'", "'james'", "'james@gmail.com'", "'testing321'");
//        insertToAccountTable("'james'", 2342, 2322323);


//        String oldTable = "persons";
//        String newTable = "user";
//        dropTables(oldTable);
//        createTable(newTable);
//        insertToTable(newTable, "'somto'", "'somto@gmail.com'", "'testing321'");

    }

    // CREATE FUNCTION TO HANDLE ATM CREATION
    // CREATE FUNCTION TO HANDLE JOINING ATM TABLE WITH USER TABLE
    // ADD ATM FUNCTIONALITIES FOR USER CLASS
    // ANYTHING ELSE THAT COMES TO MIND

//        Connection connection = null;
//
//        try {
//
//            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
//            Statement statement = connection.createStatement();
//
//            statement.setQueryTimeout(30);
//
//            statement.executeUpdate("drop table if exists users");
//            statement.executeUpdate("create table person (id integer, name string)");
//            statement.executeUpdate("insert into person values(1, 'leo')");
//            statement.executeUpdate("insert into person values(2, 'yui')");
//            ResultSet rs = statement.executeQuery("select * from person where id = 1");
//
////            System.out.println(rs);
//            while(rs.next())
//            {
//                // read the result set
//                System.out.println("name = " + rs.getString("name"));
//                System.out.println("id = " + rs.getInt("id"));
//            }
//        } catch(SQLException e) {
//            System.err.println(e.getMessage());
//        }
//        finally {
//            try{
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e){
//                System.err.println(e.getMessage());
//            }
//        }
//    }
}