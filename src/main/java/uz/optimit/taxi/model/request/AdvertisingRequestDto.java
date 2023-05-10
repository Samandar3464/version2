package uz.optimit.taxi.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisingRequestDto {

    private double price;

    private String name;

    private String ownerName;

    private String url;

    private MultipartFile content;
}
