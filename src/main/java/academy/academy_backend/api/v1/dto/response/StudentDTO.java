package academy.academy_backend.api.v1.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Min(18)
    private Integer age;
}
