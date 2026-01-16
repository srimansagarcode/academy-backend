package academy.academy_backend.api.v1.mapper;

import academy.academy_backend.api.v1.dto.request.StudentCreateRequest;
import academy.academy_backend.api.v1.dto.response.StudentResponseDTO;
import academy.academy_backend.domain.student.Student;

public class StudentMapper {
    public static Student toEntity(StudentCreateRequest request) {
        Student s = new Student();

        s.setName(request.getName());
        s.setEmail(request.getEmail());
        s.setAge(request.getAge());

        return s;
    };

    public static StudentResponseDTO toDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();

        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());

        return dto;
    }

}
