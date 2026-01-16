package academy.academy_backend.api.v1.specification;

import academy.academy_backend.api.v1.dto.request.StudentSearchRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class SortBuilder {
    public static Sort build(List<StudentSearchRequest.SortField> sorting) {
        if(sorting == null || sorting.isEmpty()){
            return Sort.by("id").ascending();
        }

        List<Sort.Order> orders = new ArrayList<>();

        for(var sortField: sorting) {
            if(!StudentSearchFields.SORTABLE_FIELDS.contains(sortField.getField())){
                continue;
            }
            Sort.Direction direction =
                    "desc".equalsIgnoreCase(sortField.getDirection())
                    ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
            orders.add(new Sort.Order(direction, sortField.getField()));
        }

        return orders.isEmpty()
                ? Sort.by("id").ascending()
                : Sort.by(orders);

    };
}
