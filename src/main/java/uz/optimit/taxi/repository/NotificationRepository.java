package uz.optimit.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.optimit.taxi.entity.Enum.NotificationType;
import uz.optimit.taxi.entity.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByIdAndActive(UUID id, boolean active);

    List<Notification> findAllBySenderIdAndActiveAndReceived(UUID senderId, boolean active, boolean received);

    List<Notification> findAllByReceiverIdAndActiveAndReceivedAndNotificationTypeOrderByCreatedTimeDesc(UUID receiverId, boolean active, boolean received, NotificationType notificationType);
    Optional<Notification> findFirstBySenderIdAndReceiverIdAndActiveAndReceivedOrderByCreatedTimeDesc(UUID senderId, UUID receiverId, boolean active, boolean received);
    List<Notification> findByAnnouncementDriverIdAndActiveAndReceived(UUID announcementId, boolean active, boolean received);
    List<Notification> findAllByReceiverIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(UUID receiverId, NotificationType notificationType);
    List<Notification> findAllBySenderIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(UUID receiverId, NotificationType notificationType);
List<Notification> findAllBySenderIdOrReceiverIdAndNotificationTypeAndReceivedTrueOrderByCreatedTime(UUID senderId, UUID receiverId, NotificationType notificationType);
}
