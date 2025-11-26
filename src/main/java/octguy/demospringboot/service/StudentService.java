package octguy.demospringboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import octguy.demospringboot.dto.DashboardStats;
import octguy.demospringboot.exception.DuplicateEmailException;
import octguy.demospringboot.exception.StudentNotFoundException;
import octguy.demospringboot.model.Student;
import octguy.demospringboot.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudentService {
    
    private final StudentRepository studentRepository;
    
    public Page<Student> getAllStudents(int page, int size, String sortBy) {
        log.debug("Fetching all students - page: {}, size: {}, sortBy: {}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Student> students = studentRepository.findAll(pageable);
        log.debug("Found {} students", students.getTotalElements());
        return students;
    }
    
    public Page<Student> searchStudents(String keyword, int page, int size, String sortBy) {
        log.debug("Searching students with keyword: '{}' - page: {}, size: {}, sortBy: {}", keyword, page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Student> students = studentRepository.searchByNameOrEmail(keyword, pageable);
        log.debug("Found {} students matching keyword '{}'", students.getTotalElements(), keyword);
        return students;
    }
    
    public Student getStudentById(Long id) {
        log.debug("Fetching student with id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found with id: {}", id);
                    return new StudentNotFoundException("Student not found with id: " + id);
                });
    }
    
    @Transactional
    public Student createStudent(Student student) {
        log.debug("Creating new student with email: {}", student.getEmail());
        
        if (studentRepository.existsByEmail(student.getEmail())) {
            log.error("Student with email {} already exists", student.getEmail());
            throw new DuplicateEmailException("Student with email " + student.getEmail() + " already exists");
        }
        
        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with id: {}", savedStudent.getId());
        return savedStudent;
    }
    
    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        log.debug("Updating student with id: {}", id);
        
        Student student = getStudentById(id);
        
        // Check if email is being changed and if new email already exists
        if (!student.getEmail().equals(studentDetails.getEmail()) && 
            studentRepository.existsByEmail(studentDetails.getEmail())) {
            log.error("Cannot update: email {} already exists", studentDetails.getEmail());
            throw new DuplicateEmailException("Student with email " + studentDetails.getEmail() + " already exists");
        }
        
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setMajor(studentDetails.getMajor());
        student.setGpa(studentDetails.getGpa());
        
        Student updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully with id: {}", updatedStudent.getId());
        return updatedStudent;
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        log.debug("Deleting student with id: {}", id);
        Student student = getStudentById(id);
        studentRepository.delete(student);
        log.info("Student deleted successfully with id: {}", id);
    }
    
    public List<Student> getAllStudentsNoPaging() {
        log.debug("Fetching all students without pagination");
        return studentRepository.findAll();
    }
    
    public DashboardStats getDashboardStats() {
        log.debug("Generating dashboard statistics");
        
        List<Student> allStudents = studentRepository.findAll();
        
        long total = allStudents.size();
        double avgGpa = allStudents.isEmpty() ? 0.0 : 
            allStudents.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
        
        long excellent = allStudents.stream()
            .filter(s -> s.getGpa() >= 3.5)
            .count();
        
        long good = allStudents.stream()
            .filter(s -> s.getGpa() >= 3.0 && s.getGpa() < 3.5)
            .count();
        
        long satisfactory = allStudents.stream()
            .filter(s -> s.getGpa() < 3.0)
            .count();
        
        Map<String, Long> byMajor = allStudents.stream()
            .collect(Collectors.groupingBy(Student::getMajor, Collectors.counting()));
        
        List<Student> topPerformers = allStudents.stream()
            .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
            .limit(5)
            .collect(Collectors.toList());
        
        return DashboardStats.builder()
            .totalStudents(total)
            .averageGpa(avgGpa)
            .excellentStudents(excellent)
            .goodStudents(good)
            .satisfactoryStudents(satisfactory)
            .studentsByMajor(byMajor)
            .topPerformers(topPerformers)
            .build();
    }
    
    public Page<Student> filterStudents(String keyword, String major, Double minGpa, Double maxGpa, int page, int size, String sortBy) {
        log.debug("Filtering students - keyword: {}, major: {}, minGpa: {}, maxGpa: {}", keyword, major, minGpa, maxGpa);
        
        List<Student> filtered = studentRepository.findAll().stream()
            .filter(s -> keyword == null || keyword.isEmpty() || 
                    s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    s.getEmail().toLowerCase().contains(keyword.toLowerCase()))
            .filter(s -> major == null || major.isEmpty() || s.getMajor().equalsIgnoreCase(major))
            .filter(s -> minGpa == null || s.getGpa() >= minGpa)
            .filter(s -> maxGpa == null || s.getGpa() <= maxGpa)
            .collect(Collectors.toList());
        
        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<Student> pageContent = filtered.subList(start, end);
        
        return new org.springframework.data.domain.PageImpl<>(
            pageContent,
            PageRequest.of(page, size, Sort.by(sortBy)),
            filtered.size()
        );
    }
    
    public List<String> getAllMajors() {
        return studentRepository.findAll().stream()
            .map(Student::getMajor)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
}
