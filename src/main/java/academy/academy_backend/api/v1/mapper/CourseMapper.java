package academy.academy_backend.api.v1.mapper;

import academy.academy_backend.api.v1.dto.response.CourseResponseDTO;
import academy.academy_backend.domain.course.Course;

public class CourseMapper {
    public static CourseResponseDTO toDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setCredits(course.getCredits());
        dto.setStudentId(course.getStudent().getId());

        return dto;
    }
}
