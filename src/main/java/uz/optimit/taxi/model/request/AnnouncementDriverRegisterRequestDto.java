package uz.optimit.taxi.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.service.SeatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDriverRegisterRequestDto {

     private Integer fromRegionId;
     private Integer toRegionId;
     private Integer fromCityId;
     private Integer toCityId;
     @JsonSerialize(using = LocalDateTimeSerializer.class)
     @JsonDeserialize(using = LocalDateTimeDeserializer.class)
     private LocalDateTime timeToDrive;
     private double frontSeatPrice;
     private double backSeatPrice;
     private boolean baggage;
     private String info;
     private List<UUID> seatIdList;

}
