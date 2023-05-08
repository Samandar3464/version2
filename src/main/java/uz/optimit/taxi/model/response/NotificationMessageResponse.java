package uz.optimit.taxi.model.response;

import lombok.*;
import uz.optimit.taxi.model.request.NotificationRequestDto;

import java.util.HashMap;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationMessageResponse {

    private String receiverToken;
    private String title;

    private String body;
    private HashMap<String, String> data;

    public static NotificationMessageResponse reCreate(String token,HashMap<String,String> data) {

        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(YOU_COME_TO_MESSAGE_FROM_DRIVER)
                .body(CAR_HAS_ENOUGH_SEAT_BUT_NOT_SUIT_YOUR_CHOOSE)
                .data(data)
                .build();

    }

    public static NotificationMessageResponse fromForPassenger(NotificationRequestDto notificationRequestDto, String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .data(notificationRequestDto.getDate())
                .build();
    }

    public static NotificationMessageResponse fromForDriver(NotificationRequestDto notificationRequestDto, String token) {
        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(notificationRequestDto.getTitle())
                .data(notificationRequestDto.getDate())
                .build();
    }

    public static NotificationMessageResponse afterAgreeRequestForPassenger(String token) {

        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(DRIVER_AGREE)
                .data(new HashMap<>())
                .build();
    }
    public static NotificationMessageResponse afterAgreeRequestForDriver(String token) {

        return NotificationMessageResponse.builder()
                .receiverToken(token)
                .title(PASSENGER_AGREE)
                .data(new HashMap<>())
                .build();
    }
}
