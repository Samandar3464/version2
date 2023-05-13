package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.PassengerParcel;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPassengerResponseAnonymous {
    private UUID id;
    private RegionResponseDto fromRegion;
    private RegionResponseDto toRegion;
    private City fromCity;
    private City toCity;
    private double price;
    private String timeToTravel;

    public static AnnouncementPassengerResponseAnonymous from(AnnouncementPassenger announcementPassenger) {
        return AnnouncementPassengerResponseAnonymous
                .builder()
                .id(announcementPassenger.getId())
                .fromRegion(RegionResponseDto.from(announcementPassenger.getFromRegion()))
                .toRegion(RegionResponseDto.from(announcementPassenger.getToRegion()))
                .fromCity(announcementPassenger.getFromCity())
                .toCity(announcementPassenger.getToCity())
                .price(announcementPassenger.getPrice())
                .timeToTravel(announcementPassenger.getTimeToTravel().toString())
                .build();
    }
    public static AnnouncementPassengerResponseAnonymous from(PassengerParcel passengerParcel) {
        return AnnouncementPassengerResponseAnonymous
                .builder()
                .id(passengerParcel.getId())
                .fromRegion(RegionResponseDto.from(passengerParcel.getFromRegion()))
                .toRegion(RegionResponseDto.from(passengerParcel.getToRegion()))
                .fromCity(passengerParcel.getFromCity())
                .toCity(passengerParcel.getToCity())
                .price(passengerParcel.getPrice())
                .timeToTravel(passengerParcel.getTimeToSend().toString())
                .build();
    }
}
