package com.examplemonevo.Interfaces;
import com.examplemonevo.Core_Model.Customer;

import java.util.ArrayList;

public interface CustomerDAO {

        void create(Customer customer);
        Customer read(String customerId);
        void update(Customer customer);
        void delete(String customerId);
        ArrayList<Customer> getAllCustomers();
        Customer login(String username, String password);

}
