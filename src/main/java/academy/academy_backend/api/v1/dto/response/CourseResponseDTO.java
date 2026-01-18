package academy.academy_backend.api.v1.dto.response;

public class CourseResponseDTO {
    private Long id;
    private String title;
    private Integer credits;
    private Long studentId;

    //getter and setter
    public Long getId() {return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }


}
