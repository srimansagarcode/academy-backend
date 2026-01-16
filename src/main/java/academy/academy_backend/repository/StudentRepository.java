package academy.academy_backend.repository;

import academy.academy_backend.domain.student.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {
    @Query("""
            SELECT s FROM Student s
            WHERE s.email = :email
            """)
    Optional<Student> findByEmail(@Param("email") String email);

    //Sort students by ID.
    @Query("""
            SELECT s FROM Student s
            ORDER BY s.id
            """)
    List<Student> findAllStudents();

    // Filter students by min age and sorted by name
    @Query("""
            SELECT s FROM Student s
            WHERE s.age >= :minAge
            ORDER BY s.name
            """)
    List<Student> findStudentsWithMinAge(@Param("minAge") Integer minAge);

    @Query("""
            SELECT s FROM Student s
            ORDER BY s.id
            """)
    Page<Student> findAllStudentsPage(Pageable pageable);
}
