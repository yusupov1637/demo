package com.example.demo.student;

public class Student {
    private final Integer studentId;
    private final String student_name;

    public Student(Integer studentId, String student_name) {
        this.studentId = studentId;
        this.student_name = student_name;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getStudent_name() {
        return student_name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", student_name='" + student_name + '\'' +
                '}';
    }
}
