package academy.academy_backend.service.impl;

import academy.academy_backend.api.v1.dto.response.CourseResponseDTO;
import academy.academy_backend.api.v1.mapper.CourseMapper;
import academy.academy_backend.domain.course.Course;
import academy.academy_backend.domain.student.Student;
import academy.academy_backend.repository.CourseRepository;
import academy.academy_backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public CourseService(
            CourseRepository courseRepository,
            StudentRepository studentRepository
    ) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public CourseResponseDTO createCourse(Long studentId, Course course) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        course.setStudent(student);
        Course saved = courseRepository.save(course);

        return CourseMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getCourseByStudent(Long studentId) {
        return courseRepository.findByStudentId(studentId)
                .stream()
                .map(CourseMapper::toDTO)
                .toList();
    }
}
