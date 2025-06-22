package com.pollify.pollify.controller;

import com.pollify.pollify.model.User;
import com.pollify.pollify.servis.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pollify/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ E-posta ile kullanıcı getir
    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Bu e-posta ile eşleşen kullanıcı bulunamadı"));
        return ResponseEntity.ok(user);
    }

    // ✅ ID ile kullanıcı getir
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("ID'ye sahip kullanıcı bulunamadı"));
        return ResponseEntity.ok(user);
    }

    // ✅ Tüm kullanıcıları listele (Admin)
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 🗑️ Kullanıcı sil (Admin)
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinecek kullanıcı bulunamadı"));
        userService.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }
}
