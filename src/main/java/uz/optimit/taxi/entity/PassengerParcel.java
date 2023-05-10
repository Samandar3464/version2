package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.model.request.ParcelRegisterRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PassengerParcel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    private String parcelInfo;

    private double fromLatitude;

    private double fromLongitude;

    private double toLongitude;

    private double toLatitude;

    private boolean active;

    private LocalDateTime timeToSend;

    private double price;

    private LocalDateTime createdTime;

    private boolean deleted;
    public static PassengerParcel from(ParcelRegisterRequestDto parcelRegisterRequestDto) {
        return PassengerParcel.builder()
                .fromLatitude(parcelRegisterRequestDto.getFromLatitude())
                .fromLongitude(parcelRegisterRequestDto.getFromLongitude())
                .toLatitude(parcelRegisterRequestDto.getToLatitude())
                .toLongitude(parcelRegisterRequestDto.getToLongitude())
                .timeToSend(parcelRegisterRequestDto.getTimeToTravel())
                .parcelInfo(parcelRegisterRequestDto.getParcelInfo())
                .createdTime(LocalDateTime.now())
                .price(parcelRegisterRequestDto.getPrice())
                .active(true)
                .deleted(false)
                .build();
    }
}
