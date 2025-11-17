package octguy.demospringboot.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            Model model,
            Authentication authentication) {
        
        log.debug("Listing students - page: {}, size: {}, sortBy: {}, keyword: {}", page, size, sortBy, keyword);
        
        Page<Student> studentPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            studentPage = studentService.searchStudents(keyword, page, size, sortBy);
            model.addAttribute("keyword", keyword);
        } else {
            studentPage = studentService.getAllStudents(page, size, sortBy);
        }
        
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
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
}
