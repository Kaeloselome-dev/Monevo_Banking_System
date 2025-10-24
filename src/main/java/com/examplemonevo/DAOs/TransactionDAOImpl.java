package com.examplemonevo.DAOs;

import com.examplemonevo.Core_Model.Transaction;
import com.examplemonevo.Interfaces.TransactionDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class TransactionDAOImpl implements TransactionDAO {
    private Connection connection;
    

    public TransactionDAOImpl(Connection connection) {
        this.connection = connection;

    }



    public void create(Transaction transaction) {
        String sql = "INSERT INTO transactions (AccountNumber, Amount, TransactionDate, TransactionType, Description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getAccountNumber());
            stmt.setDouble(2, transaction.getAmount());

            // Format Date → String (SQLite stores TEXT)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            stmt.setString(3, sdf.format(transaction.getDate()));

            stmt.setString(4, transaction.getType());
            stmt.setString(5, transaction.getDescription());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public Transaction read(String transactionId) {
        // Adjusted SQL to match the column names
        String sql = "SELECT * FROM transactions WHERE TransactionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transactionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Transaction(
                        resultSet.getString("TransactionId"),
                        resultSet.getString("AccountNumber"),
                        resultSet.getDouble("Amount"),
                        resultSet.getDate("Date"),  // Adjusted for Date column
                        resultSet.getString("TransactionType"),
                        resultSet.getString("Description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Transaction transaction) {
        // Adjusted SQL to match the column names
        String sql = "UPDATE transactions SET Amount = ?, Date = ?, TransactionType = ?, Description = ? WHERE TransactionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, transaction.getAmount());
            statement.setDate(2, new java.sql.Date(transaction.getDate().getTime()));  // Assuming Date type
            statement.setString(3, transaction.getType());
            statement.setString(4, transaction.getDescription());
            statement.setString(5, transaction.getTransactionId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String transactionId) {
        // Adjusted SQL to match the column names
        String sql = "DELETE FROM transactions WHERE TransactionId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transactionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Transaction> getTransactionsByAccount(String accountNumber) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        // Adjusted SQL to match the column names
        String sql = "SELECT * FROM transactions WHERE AccountNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getString("TransactionId"),
                        resultSet.getString("AccountNumber"),
                        resultSet.getDouble("Amount"),
                        resultSet.getDate("Date"),  // Adjusted for Date column
                        resultSet.getString("TransactionType"),
                        resultSet.getString("Description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void addInterestTransaction(String accountNumber, double interestAmount) {
        String sql = "INSERT INTO transactions (AccountNumber, Amount, TransactionDate, TransactionType, Description) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setDouble(2, interestAmount);
            pstmt.setString(3, "Interest");
            pstmt.setString(4, "Monthly interest applied");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
