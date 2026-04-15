package org.jdbcc;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.jdbcc")
public class Config {

    @Bean
    public Connection connection() throws Exception {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "sa",
                ""
        );
    }

    @Qualifier("departmentDao")
    @Bean
    public UniversalDao<Department> departmentDao(Connection connection) {
        return new UniversalDao<>(connection, Department.class);
    }

    @Qualifier("employeeDao")
    @Bean
    public UniversalDao<Employee> employeeDao(Connection connection) {
        return new UniversalDao<>(connection, Employee.class);
    }
}
