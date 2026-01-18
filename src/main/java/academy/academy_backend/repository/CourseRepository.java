package academy.academy_backend.repository;

import academy.academy_backend.domain.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByStudentId(Long studentId);
}
