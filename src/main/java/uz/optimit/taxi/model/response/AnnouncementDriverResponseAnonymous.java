package uz.optimit.taxi.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.AnnouncementDriver;
import uz.optimit.taxi.entity.City;
import uz.optimit.taxi.entity.Region;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDriverResponseAnonymous {
     private UUID id;
     private RegionResponseDto fromRegion;
     private RegionResponseDto toRegion;
     private City fromCity;
     private City toCity;
     private double frontSeatPrice;
     private double backSeatPrice;
     private String timeToDrive;

     public  static AnnouncementDriverResponseAnonymous from(AnnouncementDriver announcementDriver) {
          return AnnouncementDriverResponseAnonymous
              .builder()
              .id(announcementDriver.getId())
              .fromRegion(RegionResponseDto.from(announcementDriver.getFromRegion()))
              .toRegion(RegionResponseDto.from(announcementDriver.getToRegion()))
              .fromCity(announcementDriver.getFromCity())
              .toCity(announcementDriver.getToCity())
              .frontSeatPrice(announcementDriver.getFrontSeatPrice())
              .backSeatPrice(announcementDriver.getBackSeatPrice())
              .timeToDrive(announcementDriver.getTimeToDrive().toString())
              .build();
     }
}
