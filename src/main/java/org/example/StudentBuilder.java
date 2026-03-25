package org.example;

import java.util.List;

public class StudentBuilder {
    private final Student.GradesPolicy policy;

    public StudentBuilder(Student.GradesPolicy policy) {
        this.policy = policy;
    }

    public Student create(String name) {
        return new Student(name, policy);
    }

    public Student create(String name, List<Integer> grades) {
        return new Student(name, grades, policy);
    }
}
