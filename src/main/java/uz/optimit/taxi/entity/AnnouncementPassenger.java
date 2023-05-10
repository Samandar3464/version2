package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AnnouncementPassenger {

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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Familiar> passengersList;

    private double fromLatitude;

    private double fromLongitude;

    private double toLongitude;

    private double toLatitude;

    private boolean baggage;

    private boolean active;

    private LocalDateTime timeToTravel;

    private String info;

    private double price;

    private LocalDateTime createdTime;

    private boolean deleted;

public static AnnouncementPassenger from(AnnouncementPassengerRegisterRequestDto announcementRequestDto) {
    return AnnouncementPassenger.builder()
            .fromLatitude(announcementRequestDto.getFromLatitude())
            .fromLongitude(announcementRequestDto.getFromLongitude())
            .toLatitude(announcementRequestDto.getToLatitude())
            .toLongitude(announcementRequestDto.getToLongitude())
            .timeToTravel(announcementRequestDto.getTimeToTravel())
            .info(announcementRequestDto.getInfo())
            .createdTime(LocalDateTime.now())
            .price(announcementRequestDto.getPrice())
            .baggage(announcementRequestDto.isBaggage())
            .active(true)
            .deleted(false)
            .build();
}
}
