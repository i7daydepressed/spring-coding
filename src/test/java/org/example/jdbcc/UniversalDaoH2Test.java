package org.example.jdbcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.jdbcc.Department;
import org.jdbcc.Employee;
import org.jdbcc.UniversalDao;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UniversalDaoH2Test {
    private Connection connection;
    private UniversalDao<Department> departmentDao;
    private UniversalDao<Employee> employeeDao;

    @BeforeEach
    void openConnection() throws SQLException{
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1","sa","");
        createTables();
        departmentDao = new UniversalDao<>(connection, Department.class);
        employeeDao = new UniversalDao<>(connection, Employee.class);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testtest() {
        assertEquals(1, 1);
    }
    
    @Test
    void insertAndFindAllDepartment() {
        Department department = new Department(null, "IT");

        int completedRows = departmentDao.insert(department);
        List<Department> departments = departmentDao.findAll();

        assertEquals(1, completedRows);// асерты
        assertEquals(1, departments.size());
        assertEquals("IT", departments.get(0).getName());
    }

    @Test
    void updateDepartmentName() {
        departmentDao.insert(new Department(null, "IT"));

        List<Department> departmentsBeforeUpdate = departmentDao.findAll();
        Department savedDepartment = departmentsBeforeUpdate.get(0);

        savedDepartment.setName("HR");
        departmentDao.update(savedDepartment);

        List<Department> departmentsAfterUpdate = departmentDao.findAll();


        assertEquals(1, departmentsAfterUpdate.size());
        assertEquals("HR", departmentsAfterUpdate.get(0).getName());
    }


    private void createTables() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Employee");

            st.execute("DROP TABLE IF EXISTS Department");

            st.execute("""
                    CREATE TABLE Department (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL
                    )
                    """);

            st.execute("""
                    CREATE TABLE Employee (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        departmentId INT,
                        CONSTRAINT fk_employee_department
                            FOREIGN KEY (departmentId) REFERENCES Department(id)
                    )
                    """);
        }
    }
}
