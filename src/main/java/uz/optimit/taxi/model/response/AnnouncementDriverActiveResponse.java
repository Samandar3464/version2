package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverActiveResponse {
    private UUID id;
    private String fromRegion;
    private String toRegion;
    private String fromCity;
    private String toCity;
    private double frontSeatPrice;
    private double backSeatPrice;
    private boolean parcel;

    public static AnnouncementDriverActiveResponse from(AnnouncementDriver announcementDriver) {
        return AnnouncementDriverActiveResponse.builder()
                .id(announcementDriver.getId())
                .fromRegion(announcementDriver.getFromRegion().getName())
                .toRegion(announcementDriver.getToRegion().getName())
                .fromCity(announcementDriver.getFromCity()!=null ? announcementDriver.getFromCity().getName():null)
                .toCity(announcementDriver.getToCity()!=null ? announcementDriver.getToCity().getName():null)
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .parcel(announcementDriver.isParcel())
                .build();
    }
}
