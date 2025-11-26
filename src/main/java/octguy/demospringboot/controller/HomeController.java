package octguy.demospringboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    
    @GetMapping({"/", "/home"})
    public String home(Authentication authentication, Model model) {
        log.debug("Home page accessed by user: {}", authentication.getName());
        model.addAttribute("username", authentication.getName());
        return "redirect:/dashboard";
    }
    
    @GetMapping("/login")
    public String login() {
        log.debug("Login page accessed");
        return "login";
    }
}
