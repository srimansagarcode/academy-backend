package academy.academy_backend.service.impl;

import academy.academy_backend.domain.student.Student;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import academy.academy_backend.repository.StudentRepository;

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

    @Transactional
    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student Not Found"));
    }

}
