package academy.academy_backend.service.impl;

import academy.academy_backend.api.v1.dto.request.StudentCreateRequest;
import academy.academy_backend.api.v1.dto.request.StudentSearchRequest;
import academy.academy_backend.api.v1.dto.response.StudentResponseDTO;
import academy.academy_backend.api.v1.mapper.StudentMapper;
import academy.academy_backend.api.v1.specification.SortBuilder;
import academy.academy_backend.api.v1.specification.StudentSpecification;
import academy.academy_backend.domain.student.Student;
import academy.academy_backend.exception.ResourceNotFoundException;
import academy.academy_backend.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public StudentResponseDTO create(StudentCreateRequest studentCreateRequest) {
        Student student = StudentMapper.toEntity(studentCreateRequest);
        Student saved = studentRepository.save(student);

        return StudentMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return StudentMapper.toDTO(student);
    }

    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return studentRepository.findAllStudents();
    }

    @Transactional(readOnly = true)
    public Page<Student> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findAllStudentsPage(pageable);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponseDTO> search(StudentSearchRequest studentSearchRequest) {
        Sort sort = SortBuilder.build(studentSearchRequest.getSorting());

        Pageable pageable = PageRequest.of(
                studentSearchRequest.getPage(),
                studentSearchRequest.getSize(),
                sort
        );
        Specification<Student> spec =
                StudentSpecification.withSearchAndFilters(
                        studentSearchRequest.getSearch(),
                        studentSearchRequest.getFilters()
                );
        Page<Student> page = studentRepository.findAll(spec, pageable);
        return page.map(StudentMapper::toDTO);
    }
}
