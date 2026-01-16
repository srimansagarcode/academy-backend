package academy.academy_backend.api.v1.specification;

import java.util.Set;

public class StudentSearchFields {
    public static final Set<String> SORTABLE_FIELDS = Set.of(
            "id",
            "name",
            "email",
            "age"
    );

    public static final Set<String> FILTERABLE_FIELDS = Set.of(
            "email"
    );
}
