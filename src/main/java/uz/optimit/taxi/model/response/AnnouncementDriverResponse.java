package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.Familiar;
import uz.optimit.taxi.entity.Seat;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponse {
    private UUID id;
    private String fromRegion;
    private String toRegion;
    private String fromCity;
    private String toCity;
    private UserResponseDto userResponseDto;
    private double frontSeatPrice;
    private double backSeatPrice;
    private String info;
    private boolean baggage;
    private List<String> carPhotoPath;
    private String color;
    private String carNumber;
    private String autoModel;
    private String timeToDrive;
    private List<Seat> seatList;
    private List<Familiar> familiars;

    public static AnnouncementDriverResponse from(AnnouncementDriver announcementDriver) {
        return AnnouncementDriverResponse
                .builder()
                .id(announcementDriver.getId())
                .fromRegion(announcementDriver.getFromRegion().getName())
                .toRegion(announcementDriver.getToRegion().getName())
                .fromCity(announcementDriver.getFromCity() == null ? null : announcementDriver.getFromCity().getName())
                .toCity(announcementDriver.getToCity() == null ? null : announcementDriver.getToCity().getName())
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .info(announcementDriver.getInfo())
                .baggage(announcementDriver.isBaggage())
                .timeToDrive(announcementDriver.getTimeToDrive().toString())
                .seatList(announcementDriver.getCar().getSeatList())
                .build();
    }
}
