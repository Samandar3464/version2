package uz.optimit.taxi.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AnnouncementDriver {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

     private double frontSeatPrice;

     private double backSeatPrice;

     private boolean baggage;

     private boolean active;

     private LocalDateTime timeToDrive;

     private LocalDateTime createdTime;

     private String info;

     @ManyToOne
     private Region fromRegion;

     @ManyToOne
     private Region toRegion;

     @ManyToOne
     private City fromCity;

     @ManyToOne
     private City toCity;

     @ManyToOne
     private User user;

     @ManyToOne
     private Car car;



     public static AnnouncementDriver from(AnnouncementDriverRegisterRequestDto announcementRequestDto, User user, RegionRepository regionRepository, CityRepository cityRepository, Car car) {
          return AnnouncementDriver.builder()
              .user(user)
              .fromRegion(regionRepository.findById(announcementRequestDto.getFromRegionId()).get())
              .toRegion(regionRepository.findById(announcementRequestDto.getToRegionId()).get())
              .fromCity(cityRepository.findById(announcementRequestDto.getFromCityId()).get())
              .toCity(cityRepository.findById(announcementRequestDto.getToCityId()).get())
              .frontSeatPrice(announcementRequestDto.getFrontSeatPrice())
              .backSeatPrice(announcementRequestDto.getBackSeatPrice())
              .baggage(announcementRequestDto.isBaggage())
              .timeToDrive(announcementRequestDto.getTimeToDrive())
              .info(announcementRequestDto.getInfo())
              .createdTime(LocalDateTime.now())
              .car(car)
              .active(true)
              .build();
     }
     public static AnnouncementDriver from1(AnnouncementDriverRegisterRequestDto announcementRequestDto, User user, RegionRepository regionRepository, CityRepository cityRepository, Car car) {
          return AnnouncementDriver.builder()
              .user(user)
              .fromRegion(regionRepository.findById(announcementRequestDto.getFromRegionId()).get())
              .toRegion(regionRepository.findById(announcementRequestDto.getToRegionId()).get())
              .frontSeatPrice(announcementRequestDto.getFrontSeatPrice())
              .backSeatPrice(announcementRequestDto.getBackSeatPrice())
              .baggage(announcementRequestDto.isBaggage())
              .timeToDrive(announcementRequestDto.getTimeToDrive())
              .info(announcementRequestDto.getInfo())
              .createdTime(LocalDateTime.now())
              .car(car)
              .active(true)
              .build();
     }



}
