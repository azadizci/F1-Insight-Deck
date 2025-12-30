package com.f1insight.controller;

import com.f1insight.model.Driver;
import com.f1insight.service.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * Pilotlar sayfası
     */
    @GetMapping("/drivers")
    public String drivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());
        model.addAttribute("activePage", "drivers");
        return "drivers";
    }

    /**
     * Pilot detayları API endpoint
     */
    @GetMapping("/api/driver/{id}")
    @ResponseBody
    public Driver getDriver(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }
}
