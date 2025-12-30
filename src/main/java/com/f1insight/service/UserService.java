package com.f1insight.service;

import com.f1insight.model.Driver;
import com.f1insight.model.Gender;
import com.f1insight.model.User;
import com.f1insight.repository.DriverRepository;
import com.f1insight.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Yeni kullanıcı kaydet
     */
    @Transactional
    public User register(String firstName, String lastName, String email, String password,
            Gender gender, LocalDate birthDate) {
        // Email kontrolü
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Bu email adresi zaten kayıtlı!");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Şifreyi hash'le
        user.setGender(gender);
        user.setBirthDate(birthDate);

        return userRepository.save(user);
    }

    /**
     * Kullanıcı girişi
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * ID'ye göre kullanıcı getir
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdWithFavoriteDriver(id);
    }

    /**
     * Favori pilot seç
     */
    @Transactional
    public User setFavoriteDriver(Long userId, Long driverId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Pilot bulunamadı!"));

        user.setFavoriteDriver(driver);
        return userRepository.save(user);
    }

    /**
     * Favori takım seç
     */
    @Transactional
    public User setFavoriteTeam(Long userId, String teamName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        user.setFavoriteTeam(teamName);
        return userRepository.save(user);
    }

    /**
     * Profil güncelle
     */
    @Transactional
    public User updateProfile(Long userId, String firstName, String lastName,
            Gender gender, LocalDate birthDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setBirthDate(birthDate);

        return userRepository.save(user);
    }
}
