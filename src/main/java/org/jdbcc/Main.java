package org.jdbcc;

import org.jdbcc.dao.DepartmentDao;
import org.jdbcc.dao.EmployeeDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        // DepartmentDao departmentDao = context.getBean(DepartmentDao.class);
        // EmployeeDao employeeDao = context.getBean(EmployeeDao.class);

        UniversalDao<Department> dptDao = (UniversalDao<Department>) context.getBean("departmentDao");
        UniversalDao<Employee> empDao = (UniversalDao<Employee>) context.getBean("employeeDao");

        context.close();
    }
}
