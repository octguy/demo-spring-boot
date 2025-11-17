package octguy.demospringboot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import octguy.demospringboot.model.Student;
import octguy.demospringboot.model.User;
import octguy.demospringboot.repository.StudentRepository;
import octguy.demospringboot.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    
    @Override
    public void run(String... args) {
        loadUsers();
        loadStudents();
    }
    
    private void loadUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .email("admin")
                    .password("admin")
                    .role("ADMIN")
                    .enabled(true)
                    .build();
            
            User user = User.builder()
                    .email("user")
                    .password("user")
                    .role("USER")
                    .enabled(true)
                    .build();
            
            userRepository.saveAll(Arrays.asList(admin, user));
            log.info("Default users created: admin/admin (ADMIN), user/user (USER)");
        }
    }
    
    private void loadStudents() {
        if (studentRepository.count() == 0) {
            Student[] students = {
                Student.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .major("Computer Science")
                    .gpa(3.8)
                    .build(),
                Student.builder()
                    .name("Jane Smith")
                    .email("jane.smith@example.com")
                    .major("Electrical Engineering")
                    .gpa(3.9)
                    .build(),
                Student.builder()
                    .name("Bob Johnson")
                    .email("bob.johnson@example.com")
                    .major("Mathematics")
                    .gpa(3.5)
                    .build(),
                Student.builder()
                    .name("Alice Williams")
                    .email("alice.williams@example.com")
                    .major("Physics")
                    .gpa(3.7)
                    .build(),
                Student.builder()
                    .name("Charlie Brown")
                    .email("charlie.brown@example.com")
                    .major("Chemistry")
                    .gpa(3.6)
                    .build(),
                Student.builder()
                    .name("Diana Prince")
                    .email("diana.prince@example.com")
                    .major("Biology")
                    .gpa(3.95)
                    .build(),
                Student.builder()
                    .name("Edward Norton")
                    .email("edward.norton@example.com")
                    .major("Economics")
                    .gpa(3.4)
                    .build(),
                Student.builder()
                    .name("Fiona Green")
                    .email("fiona.green@example.com")
                    .major("Psychology")
                    .gpa(3.75)
                    .build()
            };
            
            studentRepository.saveAll(Arrays.asList(students));
            log.info("Sample students data loaded: {} students", students.length);
        }
    }
}