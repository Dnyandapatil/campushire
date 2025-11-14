package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campushire.entity.Notification;
import com.campushire.entity.Admin;
import com.campushire.entity.Student;
import com.campushire.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendNotification(Admin sender, Student receiver, String message, String type) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setMessage(message);
        notification.setType(type);
        notification.setTimestamp(LocalDateTime.now());
        notification.setReadStatus(false);
        return notificationRepository.save(notification);
    }

    public List<Notification> sendBulkNotifications(Admin sender, List<Student> receivers, String message, String type) {
        List<Notification> notifications = new ArrayList<>();
        for (Student student : receivers) {
            Notification notification = new Notification();
            notification.setSender(sender);
            notification.setReceiver(student);
            notification.setMessage(message);
            notification.setType(type);
            notification.setTimestamp(LocalDateTime.now());
            notification.setReadStatus(false);
            notifications.add(notification);
        }
        return notificationRepository.saveAll(notifications);
    }

    public List<Notification> getNotificationsForUser(Student student) {
        return notificationRepository.findByReceiver(student);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
    }

    public boolean deleteNotification(Long notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
            return true;
        }
        return false;
    }
}
