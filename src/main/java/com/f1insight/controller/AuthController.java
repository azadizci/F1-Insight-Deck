package com.f1insight.controller;

import com.f1insight.model.Gender;
import com.f1insight.model.User;
import com.f1insight.service.DriverService;
import com.f1insight.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;
    private final DriverService driverService;

    public AuthController(UserService userService, DriverService driverService) {
        this.userService = userService;
        this.driverService = driverService;
    }

    /**
     * Kayıt sayfası
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("genders", Gender.values());
        return "register";
    }

    /**
     * Kayıt işlemi
     */
    @PostMapping("/register")
    public String register(@RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String gender,
            @RequestParam String birthDate,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            User user = userService.register(
                    firstName,
                    lastName,
                    email,
                    password,
                    Gender.valueOf(gender),
                    LocalDate.parse(birthDate));

            // Otomatik giriş yap
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());

            redirectAttributes.addFlashAttribute("success", "Kayıt başarılı! Hoş geldiniz, " + user.getFirstName());
            return "redirect:/";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Giriş sayfası
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Giriş işlemi
     */
    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        var userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());

            redirectAttributes.addFlashAttribute("success", "Hoş geldiniz, " + user.getFirstName() + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Email veya şifre hatalı!");
            return "redirect:/login";
        }
    }

    /**
     * Çıkış işlemi
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Başarıyla çıkış yaptınız.");
        return "redirect:/";
    }

    /**
     * Profil sayfası
     */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        var userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", userOpt.get());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("drivers", driverService.getAllDrivers());
        model.addAttribute("teams", getTeamsList());

        return "profile";
    }

    /**
     * Favori pilot seç
     */
    @PostMapping("/api/user/favorite-driver")
    @ResponseBody
    public Map<String, Object> setFavoriteDriver(@RequestParam Long driverId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return Map.of("success", false, "message", "Giriş yapmanız gerekiyor!");
        }

        try {
            userService.setFavoriteDriver(userId, driverId);
            return Map.of("success", true, "message", "Favori pilot güncellendi!");
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    /**
     * Favori takım seç
     */
    @PostMapping("/api/user/favorite-team")
    @ResponseBody
    public Map<String, Object> setFavoriteTeam(@RequestParam String teamName, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return Map.of("success", false, "message", "Giriş yapmanız gerekiyor!");
        }

        try {
            userService.setFavoriteTeam(userId, teamName);
            return Map.of("success", true, "message", "Favori takım güncellendi!");
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }

    /**
     * Takım listesi
     */
    private String[] getTeamsList() {
        return new String[] {
                "Red Bull", "Ferrari", "Mercedes", "McLaren", "Aston Martin",
                "Alpine F1 Team", "Williams", "RB F1 Team", "Sauber", "Haas F1 Team"
        };
    }
}
