package octguy.demospringboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import octguy.demospringboot.dto.DashboardStats;
import octguy.demospringboot.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {
    
    private final StudentService studentService;
    
    @GetMapping
    public String dashboard(Authentication authentication, Model model) {
        log.debug("Dashboard accessed by user: {}", authentication.getName());
        
        DashboardStats stats = studentService.getDashboardStats();
        
        model.addAttribute("stats", stats);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        
        return "dashboard";
    }
}
