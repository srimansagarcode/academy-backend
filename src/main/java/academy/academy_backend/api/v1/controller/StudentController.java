package academy.academy_backend.api.v1.controller;

import academy.academy_backend.api.v1.dto.request.StudentCreateRequest;
import academy.academy_backend.api.v1.dto.request.StudentSearchRequest;
import academy.academy_backend.api.v1.dto.response.StudentResponseDTO;
import academy.academy_backend.domain.student.Student;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import academy.academy_backend.service.impl.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(
            @RequestBody @Valid StudentCreateRequest student) {
        return ResponseEntity.ok(studentService.create(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Student>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(studentService.getAllPaged(page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<StudentResponseDTO>> search(
            @RequestBody StudentSearchRequest studentSearchRequest) {
        return ResponseEntity.ok(studentService.search(studentSearchRequest));
    }
}
