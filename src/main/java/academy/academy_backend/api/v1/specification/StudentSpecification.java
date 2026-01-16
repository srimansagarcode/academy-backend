package academy.academy_backend.api.v1.specification;

import academy.academy_backend.domain.student.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class StudentSpecification {

    public static Specification<Student> withSearchAndFilters(
            String search,
            Map<String, Object> filters
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if(search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";

                Predicate searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                );
                predicate = cb.and(predicate, searchPredicate);

            }

            if(filters != null) {
                for(Map.Entry<String, Object> entry: filters.entrySet()){
                    predicate = cb.and(predicate,
                            cb.equal(root.get(entry.getKey()), entry.getValue())
                            );
                }
            }

            return predicate;
        };
    }
}
