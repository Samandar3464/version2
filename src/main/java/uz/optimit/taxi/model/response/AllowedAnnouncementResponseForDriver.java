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
public class AllowedAnnouncementResponseForDriver {
    private UUID userId;

    private String fullName;

    private String phone;

    private String fromRegion;

    private String toRegion;

    private String fromCity;

    private String toCity;

    private double fromLatitude;

    private double fromLongitude;

    private double toLongitude;

    private double toLatitude;

    private UUID notificationId;

    private UUID announcementId;

    public static AllowedAnnouncementResponseForDriver fromForDriver(User user, Notification notification, AnnouncementPassenger announcementPassenger){
        return AllowedAnnouncementResponseForDriver.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .announcementId(notification.getAnnouncementPassengerId())
                .notificationId(notification.getId())
                .fromRegion(announcementPassenger.getFromRegion().getName())
                .toRegion(announcementPassenger.getToRegion().getName())
                .fromCity(announcementPassenger.getFromCity().getName())
                .toCity(announcementPassenger.getToCity().getName())
                .fromLatitude(announcementPassenger.getFromLatitude())
                .fromLongitude(announcementPassenger.getFromLongitude())
                .toLatitude(announcementPassenger.getToLatitude())
                .toLongitude(announcementPassenger.getToLongitude())
                .build();
    }

}
