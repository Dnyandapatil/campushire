package com.campushire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.campushire.entity.Notification;
import com.campushire.entity.Student;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiver(Student student);
}
