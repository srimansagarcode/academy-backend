package academy.academy_backend.service.impl;

import academy.academy_backend.domain.student.Student;
import academy.academy_backend.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
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
}
