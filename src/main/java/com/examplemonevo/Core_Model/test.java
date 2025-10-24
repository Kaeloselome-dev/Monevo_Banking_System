package com.examplemonevo.Core_Model;

import com.examplemonevo.DAOs.AccountDAOImpl;
import com.examplemonevo.Utility.DB_Connection;

import java.sql.Connection;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) {
        try {
            Connection connection = DB_Connection.getConnection();
            AccountDAOImpl accountDAO = new AccountDAOImpl(connection);

            accountDAO.applyMonthlyInterest();

            System.out.println("Interest applied to all applicable accounts!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

