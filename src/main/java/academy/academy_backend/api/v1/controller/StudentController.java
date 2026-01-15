package academy.academy_backend.api.v1.controller;

import academy.academy_backend.domain.student.Student;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import academy.academy_backend.service.impl.StudentService;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody @Valid Student student) {
        return ResponseEntity.ok(studentService.create(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }
}
