package org.jdbcc.dao;

import java.sql.Connection;

import org.jdbcc.Department;
import org.jdbcc.UniversalDao;
import org.springframework.stereotype.Repository;

// @Repository
public class DepartmentDao extends UniversalDao<Department> {
    public DepartmentDao(Connection connect) {
        super(connect, Department.class);
    }
}