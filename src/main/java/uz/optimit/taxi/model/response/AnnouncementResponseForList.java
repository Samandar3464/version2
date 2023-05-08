package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.User;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementResponseForList {
    private UUID userId;
    private String fullName;
    private UUID notificationId;
    private UUID announcementId;

    public static AnnouncementResponseForList from(User user, UUID notificationId, UUID announcementId){
        return AnnouncementResponseForList.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .announcementId(announcementId)
                .notificationId(notificationId)
                .build();
    }
}
