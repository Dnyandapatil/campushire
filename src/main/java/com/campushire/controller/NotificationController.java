package com.campushire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.campushire.entity.Notification;
import com.campushire.entity.Admin;
import com.campushire.entity.Student;
import com.campushire.service.AdminService;
import com.campushire.service.StudentService;
import com.campushire.service.NotificationService;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> request) {
        Long senderId = Long.valueOf(request.get("senderId").toString());
        Long receiverId = Long.valueOf(request.get("receiverId").toString());
        String message = request.get("message").toString();
        String type = request.get("type").toString();
        Admin sender = adminService.getAdminById(senderId);
        Student receiver = studentService.getStudentById(receiverId);
        if (sender == null || receiver == null) {
            return ResponseEntity.badRequest().body("Invalid sender or receiver ID");
        }
        Notification notification = notificationService.sendNotification(sender, receiver, message, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PostMapping("/send/bulk/email")
    public ResponseEntity<?> sendBulkNotificationsByEmail(@RequestBody Map<String, Object> request) {
        Long senderId = Long.valueOf(request.get("senderId").toString());
        String message = request.get("message").toString();
        String type = request.get("type").toString();
        List<String> emails = (List<String>) request.get("emails");
        Admin sender = adminService.getAdminById(senderId);
        List<Student> receivers = new ArrayList<>();
        for (String email : emails) {
            Student student = studentService.getStudentByEmail(email);
            if (student != null) receivers.add(student);
        }
        if (receivers.isEmpty()) {
            return ResponseEntity.badRequest().body("No students found for given emails.");
        }
        List<Notification> notifications = notificationService.sendBulkNotifications(sender, receivers, message, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }

    @PostMapping("/send/bulk/id")
    public ResponseEntity<?> sendBulkNotificationsById(@RequestBody Map<String, Object> request) {
        Long senderId = Long.valueOf(request.get("senderId").toString());
        String message = request.get("message").toString();
        String type = request.get("type").toString();
        List<Long> ids = new ArrayList<>();
        if (request.containsKey("studentIds")) {
            List<?> idObjects = (List<?>) request.get("studentIds");
            for (Object obj : idObjects) {
                ids.add(Long.valueOf(obj.toString()));
            }
        }
        Admin sender = adminService.getAdminById(senderId);
        List<Student> receivers = new ArrayList<>();
        for (Long id : ids) {
            Student student = studentService.getStudentById(id);
            if (student != null) receivers.add(student);
        }
        if (receivers.isEmpty()) {
            return ResponseEntity.badRequest().body("No students found for given IDs.");
        }
        List<Notification> notifications = notificationService.sendBulkNotifications(sender, receivers, message, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }


    @PostMapping("/send/bulk/name")
    public ResponseEntity<?> sendBulkNotificationsByName(@RequestBody Map<String, Object> request) {
        Long senderId = Long.valueOf(request.get("senderId").toString());
        String message = request.get("message").toString();
        String type = request.get("type").toString();
        List<String> names = (List<String>) request.get("names");
        Admin sender = adminService.getAdminById(senderId);
        List<Student> receivers = new ArrayList<>();
        for (String name : names) {
            receivers.addAll(studentService.getStudentsByName(name));
        }
        if (receivers.isEmpty()) {
            return ResponseEntity.badRequest().body("No students found for given names.");
        }
        List<Notification> notifications = notificationService.sendBulkNotifications(sender, receivers, message, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(notifications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsForUser(@PathVariable Long userId) {
        Student student = studentService.getStudentById(userId);
        if (student == null) return ResponseEntity.notFound().build();
        List<Notification> notifications = notificationService.getNotificationsForUser(student);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        boolean deleted = notificationService.deleteNotification(id);
        if (deleted) {
            return ResponseEntity.ok("Notification deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
