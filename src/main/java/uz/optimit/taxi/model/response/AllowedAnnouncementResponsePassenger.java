package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.Notification;
import uz.optimit.taxi.entity.User;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllowedAnnouncementResponsePassenger {

    private UUID userId;

    private String fullName;

    private String phone;

    private UUID notificationId;

    private UUID announcementId;
    public static AllowedAnnouncementResponsePassenger fromForPassenger(User user, Notification notification){
        return AllowedAnnouncementResponsePassenger.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .announcementId(notification.getAnnouncementPassengerId())
                .notificationId(notification.getId())
                .build();
    }
}
