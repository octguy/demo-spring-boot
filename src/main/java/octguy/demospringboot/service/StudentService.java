package octguy.demospringboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    
    public List<Student> getAllStudents() {
        log.debug("Fetching all students without pagination");
        return studentRepository.findAll();
    }
}
