package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.PassengerParcel;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParcelResponseForList {
    private UUID id;
    private String fromRegion;
    private String toRegion;
    private String toCity;
    private String fromCity;
    private String timeToSend;

    public static ParcelResponseForList from(PassengerParcel passengerParcel) {
        return ParcelResponseForList.builder()
                .id(passengerParcel.getId())
                .fromRegion(passengerParcel.getFromRegion().getName())
                .toRegion(passengerParcel.getToRegion().getName())
                .fromCity(passengerParcel.getFromCity().getName())
                .toCity(passengerParcel.getToCity().getName())
                .timeToSend(passengerParcel.getTimeToSend().toString())
                .build();
    }
}
