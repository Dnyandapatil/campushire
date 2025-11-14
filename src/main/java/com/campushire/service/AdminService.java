package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campushire.entity.Admin;
import com.campushire.repository.AdminRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin registerAdmin(Admin admin) {
        admin.setCreatedAt(LocalDateTime.now());
        return adminRepository.save(admin);
    }

    public Admin getAdminById(Long adminId) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        return admin.orElse(null);
    }

    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public boolean deleteAdmin(Long adminId) {
        if (adminRepository.existsById(adminId)) {
            adminRepository.deleteById(adminId);
            return true;
        }
        return false;
    }

    public Admin login(String email, String password) {
        Admin admin = getAdminByEmail(email);
        if (admin != null && admin.getPasswordHash().equals(password)) {
            return admin;
        }
        return null;
    }
}
