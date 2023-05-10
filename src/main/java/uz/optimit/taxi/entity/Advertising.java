package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AdvertisingRequestDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Advertising {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private double price;

    private String name;

    private String ownerName;

    private String url;

    private boolean active;

    @OneToOne
    private Attachment content;

    public static Advertising from(AdvertisingRequestDto advertisingRequestDto){
        return Advertising
                .builder()
                .price(advertisingRequestDto.getPrice())
                .name(advertisingRequestDto.getName())
                .ownerName(advertisingRequestDto.getOwnerName())
                .url(advertisingRequestDto.getUrl())
                .active(true)
                .build();
    }
}
