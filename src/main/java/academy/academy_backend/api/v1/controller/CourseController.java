package academy.academy_backend.api.v1.controller;

import academy.academy_backend.api.v1.dto.response.CourseResponseDTO;
import academy.academy_backend.domain.course.Course;
import academy.academy_backend.service.impl.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students/{studentId}/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(
            @PathVariable Long studentId,
            @RequestBody Course course
    ) {
        return ResponseEntity.ok(
                courseService.createCourse(studentId, course)
        );
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                courseService.getCourseByStudent(studentId)
        );
    }
}
