package org.jdbcc.dao;

import java.sql.Connection;

import org.jdbcc.Employee;
import org.jdbcc.UniversalDao;
import org.springframework.stereotype.Repository;

// @Repository
public class EmployeeDao extends UniversalDao<Employee> {
    public EmployeeDao(Connection connect) {
        super(connect, Employee.class);
    }
}
