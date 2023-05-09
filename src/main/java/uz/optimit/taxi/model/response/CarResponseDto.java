package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Seat;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponseDto {

     private UUID id;

     private String carNumber;

     private String color;

     private String texPassport;

     private String autoModel;

     private List<String> autoPhotosPath;

     private String texPassportPhotoPath;

     private String photoDriverLicense;

     private boolean active;

    private List<Seat> seatList;


     public static CarResponseDto from(Car car){
         return    CarResponseDto.builder()
                 .id(car.getId())
                 .carNumber(car.getCarNumber())
                 .color(car.getColor())
                 .texPassport(car.getTexPassport())
                 .autoModel(car.getAutoModel().getName())
                 .active(car.isActive())
                 .seatList(car.getSeatList())
                 .build();
     }
}
