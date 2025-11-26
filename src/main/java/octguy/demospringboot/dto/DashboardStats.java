package octguy.demospringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {
    private long totalStudents;
    private double averageGpa;
    private long excellentStudents; // GPA >= 3.5
    private long goodStudents; // 3.0 <= GPA < 3.5
    private long satisfactoryStudents; // GPA < 3.0
    private Map<String, Long> studentsByMajor;
    private java.util.List<octguy.demospringboot.model.Student> topPerformers;
}
