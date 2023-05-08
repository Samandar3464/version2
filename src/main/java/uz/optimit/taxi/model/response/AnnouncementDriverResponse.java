package uz.optimit.taxi.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static AnnouncementDriverResponse from(AnnouncementDriver announcementDriver, Car car, List<Familiar> familiars,String downloadUrl) {

        List<Attachment> attachment1 = car.getAutoPhotos();
        List<String> photos = new ArrayList<>();
        attachment1.forEach(attachment -> {
            photos.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
        });

        if (announcementDriver.getFromCity() == null) {
            return getResponse1(announcementDriver, car, downloadUrl, familiars,photos);
        }
        return getResponse(announcementDriver, car, downloadUrl,familiars, photos);
    }

    public static AnnouncementDriverResponse fromDriver(AnnouncementDriver announcementDriver, Car car,String downloadUrl) {

        List<Attachment> attachment1 = car.getAutoPhotos();
        List<String> photos = new ArrayList<>();
        attachment1.forEach(attachment -> {
            photos.add(downloadUrl + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
        });

        if (announcementDriver.getFromCity() == null) {
            return getResponse1(announcementDriver, car, downloadUrl, null,photos);
        }
        return getResponse(announcementDriver, car, downloadUrl,null, photos);
    }

    private static AnnouncementDriverResponse getResponse(AnnouncementDriver announcementDriver, Car car, String downloadUrl, List<Familiar> familiars, List<String> photos) {
        return AnnouncementDriverResponse
                .builder()
                .id(announcementDriver.getId())
                .fromRegion(announcementDriver.getFromRegion().getName())
                .toRegion(announcementDriver.getToRegion().getName())
                .fromCity(announcementDriver.getFromCity().getName())
                .toCity(announcementDriver.getToCity().getName())
                .familiars(familiars)
                .userResponseDto(UserResponseDto.fromDriver(announcementDriver.getUser(), downloadUrl))
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .info(announcementDriver.getInfo())
                .baggage(announcementDriver.isBaggage())
                .timeToDrive(announcementDriver.getTimeToDrive().toString())
                .carPhotoPath(photos)
                .color(car.getColor())
                .seatList(announcementDriver.getCar().getSeatList())
                .carNumber(car.getCarNumber())
                .autoModel(car.getAutoModel().getName())
                .build();
    }

    private static AnnouncementDriverResponse getResponse1(AnnouncementDriver announcementDriver, Car car, String downloadUrl, List<Familiar> familiars, List<String> photos) {
        return AnnouncementDriverResponse
                .builder()
                .id(announcementDriver.getId())
                .fromRegion(announcementDriver.getFromRegion().getName())
                .toRegion(announcementDriver.getToRegion().getName())
                .familiars(familiars)
                .userResponseDto(UserResponseDto.fromDriver(announcementDriver.getUser(), downloadUrl))
                .frontSeatPrice(announcementDriver.getFrontSeatPrice())
                .backSeatPrice(announcementDriver.getBackSeatPrice())
                .info(announcementDriver.getInfo())
                .baggage(announcementDriver.isBaggage())
                .timeToDrive(announcementDriver.getTimeToDrive().toString())
                .carPhotoPath(photos)
                .color(car.getColor())
                .seatList(announcementDriver.getCar().getSeatList())
                .carNumber(car.getCarNumber())
                .autoModel(car.getAutoModel().getName())
                .build();
    }


}
