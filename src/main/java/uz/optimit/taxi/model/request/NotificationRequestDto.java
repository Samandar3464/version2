package uz.optimit.taxi.model.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Enum.NotificationType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {

    private String title;

    private String body;

    private UUID receiverId;

    private UUID announcementDriverId;

    private UUID announcementPassengerId;

    private List<UUID> seatIdList;

    private HashMap<String, String> date;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
}
