package octguy.demospringboot.service;

import octguy.demospringboot.dto.DashboardStats;
import octguy.demospringboot.exception.DuplicateEmailException;
import octguy.demospringboot.exception.StudentNotFoundException;
import octguy.demospringboot.model.Student;
import octguy.demospringboot.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    void setUp() {
        student1 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .major("Computer Science")
                .gpa(3.8)
                .build();

        student2 = Student.builder()
                .id(2L)
                .name("Jane Smith")
                .email("jane@example.com")
                .major("Mathematics")
                .gpa(3.5)
                .build();

        student3 = Student.builder()
                .id(3L)
                .name("Bob Johnson")
                .email("bob@example.com")
                .major("Computer Science")
                .gpa(2.9)
                .build();
    }

    @Test
    @DisplayName("Should get all students with pagination")
    void shouldGetAllStudentsWithPagination() {
        // Given
        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> studentPage = new PageImpl<>(students);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));

        when(studentRepository.findAll(any(Pageable.class))).thenReturn(studentPage);

        // When
        Page<Student> result = studentService.getAllStudents(0, 5, "id");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).contains(student1, student2);
        verify(studentRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should search students by keyword")
    void shouldSearchStudentsByKeyword() {
        // Given
        List<Student> students = Arrays.asList(student1);
        Page<Student> studentPage = new PageImpl<>(students);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));

        when(studentRepository.searchByNameOrEmail(anyString(), any(Pageable.class)))
                .thenReturn(studentPage);

        // When
        Page<Student> result = studentService.searchStudents("john", 0, 5, "id");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).searchByNameOrEmail(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("Should get student by id successfully")
    void shouldGetStudentByIdSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        // When
        Student result = studentService.getStudentById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when student not found")
    void shouldThrowExceptionWhenStudentNotFound() {
        // Given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> studentService.getStudentById(999L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id: 999");
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create student successfully")
    void shouldCreateStudentSuccessfully() {
        // Given
        Student newStudent = Student.builder()
                .name("Alice Brown")
                .email("alice@example.com")
                .major("Physics")
                .gpa(3.9)
                .build();

        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // When
        Student result = studentService.createStudent(newStudent);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        verify(studentRepository, times(1)).existsByEmail("alice@example.com");
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        Student newStudent = Student.builder()
                .name("John Clone")
                .email("john@example.com")
                .major("Business")
                .gpa(3.0)
                .build();

        when(studentRepository.existsByEmail("john@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> studentService.createStudent(newStudent))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Student with email john@example.com already exists");
        verify(studentRepository, times(1)).existsByEmail("john@example.com");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should update student successfully")
    void shouldUpdateStudentSuccessfully() {
        // Given
        Student updatedDetails = Student.builder()
                .name("John Updated")
                .email("john.new@example.com")
                .major("Data Science")
                .gpa(3.9)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.existsByEmail("john.new@example.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // When
        Student result = studentService.updateStudent(1L, updatedDetails);

        // Then
        assertThat(result).isNotNull();
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).existsByEmail("john.new@example.com");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw exception when updating with duplicate email")
    void shouldThrowExceptionWhenUpdatingWithDuplicateEmail() {
        // Given
        Student updatedDetails = Student.builder()
                .name("John Updated")
                .email("jane@example.com") // Jane's email
                .major("Data Science")
                .gpa(3.9)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.existsByEmail("jane@example.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> studentService.updateStudent(1L, updatedDetails))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Student with email jane@example.com already exists");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).existsByEmail("jane@example.com");
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should allow updating student with same email")
    void shouldAllowUpdatingStudentWithSameEmail() {
        // Given
        Student updatedDetails = Student.builder()
                .name("John Updated")
                .email("john@example.com") // Same email
                .major("Data Science")
                .gpa(3.9)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        // When
        Student result = studentService.updateStudent(1L, updatedDetails);

        // Then
        assertThat(result).isNotNull();
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).existsByEmail(anyString());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully() {
        // Given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        doNothing().when(studentRepository).delete(student1);

        // When
        studentService.deleteStudent(1L);

        // Then
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(student1);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent student")
    void shouldThrowExceptionWhenDeletingNonExistentStudent() {
        // Given
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> studentService.deleteStudent(999L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found with id: 999");
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).delete(any(Student.class));
    }

    @Test
    @DisplayName("Should get all students without pagination")
    void shouldGetAllStudentsWithoutPagination() {
        // Given
        List<Student> students = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(students);

        // When
        List<Student> result = studentService.getAllStudentsNoPaging();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).contains(student1, student2, student3);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get dashboard statistics correctly")
    void shouldGetDashboardStatistics() {
        // Given
        List<Student> students = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(students);

        // When
        DashboardStats stats = studentService.getDashboardStats();

        // Then
        assertThat(stats).isNotNull();
        assertThat(stats.getTotalStudents()).isEqualTo(3);
        assertThat(stats.getAverageGpa()).isCloseTo(3.4, within(0.1));
        assertThat(stats.getExcellentStudents()).isEqualTo(2); // GPA >= 3.5
        assertThat(stats.getGoodStudents()).isEqualTo(0); // 3.0 <= GPA < 3.5
        assertThat(stats.getSatisfactoryStudents()).isEqualTo(1); // GPA < 3.0
        assertThat(stats.getStudentsByMajor()).containsKeys("Computer Science", "Mathematics");
        assertThat(stats.getTopPerformers()).hasSize(3);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter students by major")
    void shouldFilterStudentsByMajor() {
        // Given
        List<Student> allStudents = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(allStudents);

        // When
        Page<Student> result = studentService.filterStudents(null, "Computer Science", null, null, 0, 10, "id");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(s -> s.getMajor().equals("Computer Science"));
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter students by GPA range")
    void shouldFilterStudentsByGpaRange() {
        // Given
        List<Student> allStudents = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(allStudents);

        // When
        Page<Student> result = studentService.filterStudents(null, null, 3.5, 4.0, 0, 10, "id");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(s -> s.getGpa() >= 3.5 && s.getGpa() <= 4.0);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter students by keyword")
    void shouldFilterStudentsByKeyword() {
        // Given
        List<Student> allStudents = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(allStudents);

        // When
        Page<Student> result = studentService.filterStudents("john", null, null, null, 0, 10, "id");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2); // John Doe and Bob Johnson
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should filter students with combined filters")
    void shouldFilterStudentsWithCombinedFilters() {
        // Given
        List<Student> allStudents = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(allStudents);

        // When
        Page<Student> result = studentService.filterStudents(
                "john", "Computer Science", 3.0, 4.0, 0, 10, "id"
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1); // Only John Doe matches all criteria
        assertThat(result.getContent().get(0).getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get all distinct majors")
    void shouldGetAllDistinctMajors() {
        // Given
        List<Student> students = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(students);

        // When
        List<String> majors = studentService.getAllMajors();

        // Then
        assertThat(majors).isNotNull();
        assertThat(majors).hasSize(2);
        assertThat(majors).containsExactlyInAnyOrder("Computer Science", "Mathematics");
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle empty student list in dashboard stats")
    void shouldHandleEmptyStudentListInDashboardStats() {
        // Given
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // When
        DashboardStats stats = studentService.getDashboardStats();

        // Then
        assertThat(stats).isNotNull();
        assertThat(stats.getTotalStudents()).isEqualTo(0);
        assertThat(stats.getAverageGpa()).isEqualTo(0.0);
        assertThat(stats.getTopPerformers()).isEmpty();
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle pagination correctly in filter")
    void shouldHandlePaginationCorrectlyInFilter() {
        // Given
        List<Student> allStudents = Arrays.asList(student1, student2, student3);
        when(studentRepository.findAll()).thenReturn(allStudents);

        // When
        Page<Student> page1 = studentService.filterStudents(null, null, null, null, 0, 2, "id");
        Page<Student> page2 = studentService.filterStudents(null, null, null, null, 1, 2, "id");

        // Then
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page2.getContent()).hasSize(1);
        assertThat(page1.getTotalElements()).isEqualTo(3);
        assertThat(page2.getTotalElements()).isEqualTo(3);
        verify(studentRepository, times(2)).findAll();
    }
}
