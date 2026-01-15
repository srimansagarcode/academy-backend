package academy.academy_backend.repository;

import academy.academy_backend.domain.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            SELECT s FROM Student s
            WHERE s.email = :email
            """)
    Optional<Student> findByEmail(@Param("email") String email);
}
