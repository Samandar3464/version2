package uz.optimit.taxi.entity;


import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;

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

    private boolean deleted;

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
    public static AnnouncementDriver from(AnnouncementDriverRegisterRequestDto announcementRequestDto) {
        return AnnouncementDriver.builder()
                .frontSeatPrice(announcementRequestDto.getFrontSeatPrice())
                .backSeatPrice(announcementRequestDto.getBackSeatPrice())
                .baggage(announcementRequestDto.isBaggage())
                .timeToDrive(announcementRequestDto.getTimeToDrive())
                .info(announcementRequestDto.getInfo())
                .createdTime(LocalDateTime.now())
                .active(true)
                .deleted(false)
                .build();
    }


}
