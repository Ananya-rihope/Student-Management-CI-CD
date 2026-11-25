
package com.student.management.controller;

import com.student.management.entity.Student;
import com.student.management.service.StudentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private StudentController studentController;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Rahul");
        student.setEmail("rahul@gmail.com");
        student.setCourse("Java");
    }

    // Test 1: Display Registration Form
    @Test
    void testShowForm() {
        when(studentService.getAllStudents())
                .thenReturn(Arrays.asList(student));

        String result = studentController.showForm(model);

        assertEquals("students", result);

        verify(model).addAttribute(eq("student"), any(Student.class));
        verify(model).addAttribute("students", Arrays.asList(student));
        verify(studentService).getAllStudents();
    }

    // Test 2: Save Student Successfully
    @Test
    void testSaveStudentSuccessfully() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = studentController.saveStudent(
                student, bindingResult, model);

        assertEquals("redirect:/", result);

        verify(studentService).saveStudent(student);
        verify(studentService, never()).getAllStudents();
    }

    // Test 3: Save Student With Validation Errors
    @Test
    void testSaveStudentWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(studentService.getAllStudents())
                .thenReturn(Arrays.asList(student));

        String result = studentController.saveStudent(
                student, bindingResult, model);

        assertEquals("students", result);

        verify(studentService).getAllStudents();
        verify(model).addAttribute("students", Arrays.asList(student));
        verify(studentService, never()).saveStudent(any(Student.class));
    }

    // Test 4: Delete Student
    @Test
    void testDeleteStudent() {
        Long studentId = 1L;

        String result = studentController.deleteStudent(studentId);

        assertEquals("redirect:/", result);

        verify(studentService).deleteStudent(studentId);
    }
}
