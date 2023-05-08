package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Familiar;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementPassengerResponse {
    private UUID id;
    private String toCity;
    private String info;
    private double price;
    private String fromCity;
    private boolean baggage;
    private double toLatitude;
    private double toLongitude;
    private double fromLatitude;
    private double fromLongitude;
    private String announcementOwnerPhone;
    private String toRegion;
    private String fromRegion;
    private List<Familiar> passengersList;
    private String timeToTravel;
    private UserResponseDto userResponseDto;

    public static AnnouncementPassengerResponse from(AnnouncementPassenger announcementPassenger, UserResponseDto userResponseDto) {
        return AnnouncementPassengerResponse.builder()
                .id(announcementPassenger.getId())
                .fromRegion(announcementPassenger.getFromRegion().getName())
                .toRegion(announcementPassenger.getToRegion().getName())
                .fromCity(announcementPassenger.getFromCity().getName())
                .toCity(announcementPassenger.getToCity().getName())
                .price(announcementPassenger.getPrice())
                .fromLatitude(announcementPassenger.getFromLatitude())
                .fromLongitude(announcementPassenger.getFromLongitude())
                .toLatitude(announcementPassenger.getToLatitude())
                .toLongitude(announcementPassenger.getToLongitude())
                .baggage(announcementPassenger.isBaggage())
                .passengersList(announcementPassenger.getPassengersList())
                .info(announcementPassenger.getInfo())
                .announcementOwnerPhone(announcementPassenger.getUser().getPhone())
                .timeToTravel(announcementPassenger.getTimeToTravel().toString())
                .userResponseDto(userResponseDto)
                .build();
    }
}
