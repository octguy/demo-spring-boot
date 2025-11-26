package octguy.demospringboot.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import octguy.demospringboot.exception.DuplicateEmailException;
import octguy.demospringboot.exception.StudentNotFoundException;
import octguy.demospringboot.model.Student;
import octguy.demospringboot.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {
    
    private final StudentService studentService;
    
    @GetMapping
    public String listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) BigDecimal minGpa,
            @RequestParam(required = false) BigDecimal maxGpa,
            Model model,
            Authentication authentication) {
        
        log.debug("Listing students - page: {}, size: {}, sortBy: {}, keyword: {}, major: {}, minGpa: {}, maxGpa: {}", 
                page, size, sortBy, keyword, major, minGpa, maxGpa);
        
        Page<Student> studentPage;
        
        // Advanced filtering
        if ((keyword != null && !keyword.trim().isEmpty()) || 
            (major != null && !major.trim().isEmpty()) ||
            minGpa != null || maxGpa != null) {
            Double minGpaDouble = minGpa != null ? minGpa.doubleValue() : null;
            Double maxGpaDouble = maxGpa != null ? maxGpa.doubleValue() : null;
            studentPage = studentService.filterStudents(keyword, major, minGpaDouble, maxGpaDouble, page, size, sortBy);
            model.addAttribute("keyword", keyword);
            model.addAttribute("selectedMajor", major);
            model.addAttribute("minGpa", minGpa);
            model.addAttribute("maxGpa", maxGpa);
        } else {
            studentPage = studentService.getAllStudents(page, size, sortBy);
        }
        
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("allMajors", studentService.getAllMajors());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        
        return "students/list";
    }
    
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model, Authentication authentication) {
        log.debug("Viewing student with id: {}", id);
        
        try {
            Student student = studentService.getStudentById(id);
            model.addAttribute("student", student);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
            return "students/view";
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return "redirect:/students?error=notfound";
        }
    }
    
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model, Authentication authentication) {
        log.debug("Showing create student form");
        model.addAttribute("student", new Student());
        model.addAttribute("username", authentication.getName());
        return "students/form";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String createStudent(
            @Valid @ModelAttribute Student student,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            Authentication authentication) {
        
        log.debug("Creating student: {}", student);
        
        if (result.hasErrors()) {
            log.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("username", authentication.getName());
            return "students/form";
        }
        
        try {
            Student savedStudent = studentService.createStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            return "redirect:/students/" + savedStudent.getId();
        } catch (DuplicateEmailException e) {
            log.error("Duplicate email error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("username", authentication.getName());
            return "students/form";
        }
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        log.debug("Showing edit form for student id: {}", id);
        
        try {
            Student student = studentService.getStudentById(id);
            model.addAttribute("student", student);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isEdit", true);
            return "students/form";
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return "redirect:/students?error=notfound";
        }
    }
    
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStudent(
            @PathVariable Long id,
            @Valid @ModelAttribute Student student,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            Authentication authentication) {
        
        log.debug("Updating student with id: {}", id);
        
        if (result.hasErrors()) {
            log.error("Validation errors: {}", result.getAllErrors());
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isEdit", true);
            return "students/form";
        }
        
        try {
            studentService.updateStudent(id, student);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            return "redirect:/students/" + id;
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return "redirect:/students?error=notfound";
        } catch (DuplicateEmailException e) {
            log.error("Duplicate email error: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isEdit", true);
            return "students/form";
        }
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.debug("Deleting student with id: {}", id);
        
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
            return "redirect:/students";
        } catch (StudentNotFoundException e) {
            log.error("Student not found: {}", e.getMessage());
            return "redirect:/students?error=notfound";
        }
    }
    
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        log.debug("Exporting students to CSV");
        
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"students_" + 
                LocalDate.now() + ".csv\"");
        
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            // Write header
            String[] header = {"ID", "Name", "Email", "Major", "GPA"};
            writer.writeNext(header);
            
            // Write data
            List<Student> students = studentService.getAllStudentsNoPaging();
            for (Student student : students) {
                String[] data = {
                    student.getId().toString(),
                    student.getName(),
                    student.getEmail(),
                    student.getMajor(),
                    student.getGpa().toString()
                };
                writer.writeNext(data);
            }
            
            log.info("Successfully exported {} students to CSV", students.size());
        }
    }
    
    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String importFromCSV(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        
        log.debug("Importing students from CSV: {}", file.getOriginalFilename());
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a CSV file to upload.");
            return "redirect:/students";
        }
        
        if (!file.getOriginalFilename().endsWith(".csv")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Only CSV files are allowed.");
            return "redirect:/students";
        }
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "CSV file is empty.");
                return "redirect:/students";
            }
            
            // Skip header row
            List<Student> studentsToImport = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                
                if (row.length < 4) {
                    errors.add("Row " + (i + 1) + ": Insufficient columns");
                    continue;
                }
                
                try {
                    Student student = new Student();
                    student.setName(row[0].trim());
                    student.setEmail(row[1].trim());
                    student.setMajor(row[2].trim());
                    student.setGpa(Double.parseDouble(row[3].trim()));
                    
                    studentsToImport.add(student);
                } catch (NumberFormatException e) {
                    errors.add("Row " + (i + 1) + ": Invalid GPA format");
                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                }
            }
            
            // Save valid students
            int successCount = 0;
            for (Student student : studentsToImport) {
                try {
                    studentService.createStudent(student);
                    successCount++;
                } catch (DuplicateEmailException e) {
                    errors.add("Duplicate email: " + student.getEmail());
                }
            }
            
            if (successCount > 0) {
                redirectAttributes.addFlashAttribute("successMessage", 
                        "Successfully imported " + successCount + " students.");
            }
            
            if (!errors.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", 
                        "Errors occurred: " + String.join("; ", errors));
            }
            
            log.info("Import completed: {} successful, {} errors", successCount, errors.size());
            
        } catch (IOException | CsvException e) {
            log.error("Error reading CSV file", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error reading CSV file: " + e.getMessage());
        }
        
        return "redirect:/students";
    }
}
