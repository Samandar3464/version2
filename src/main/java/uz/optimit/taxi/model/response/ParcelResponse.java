package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.PassengerParcel;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelResponse {
    private UUID id;
    private String fromRegion;
    private String toCity;
    private String fromCity;
    private String toRegion;
    private String parcelInfo;
    private double price;
    private double toLatitude;
    private double toLongitude;
    private double fromLatitude;
    private double fromLongitude;
    private String announcementOwnerPhone;
    private String timeToSend;
    private UserResponseDto userResponseDto;

    public static ParcelResponse from(PassengerParcel passengerParcel, UserResponseDto userResponseDto) {
        return ParcelResponse.builder()
                .id(passengerParcel.getId())
                .fromRegion(passengerParcel.getFromRegion().getName())
                .toRegion(passengerParcel.getToRegion().getName())
                .fromCity(passengerParcel.getFromCity().getName())
                .toCity(passengerParcel.getToCity().getName())
                .price(passengerParcel.getPrice())
                .fromLatitude(passengerParcel.getFromLatitude())
                .fromLongitude(passengerParcel.getFromLongitude())
                .toLatitude(passengerParcel.getToLatitude())
                .toLongitude(passengerParcel.getToLongitude())
                .parcelInfo(passengerParcel.getParcelInfo())
                .announcementOwnerPhone(passengerParcel.getUser().getPhone())
                .timeToSend(passengerParcel.getTimeToSend().toString())
                .userResponseDto(userResponseDto)
                .build();
    }
}
