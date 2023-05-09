package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.CityRequestDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonIgnore
    @ManyToOne
    private Region region;

    public static City from(CityRequestDto cityRequestDto, Region region) {
        return City.builder()
                .name(cityRequestDto.getName()).region(region).build();
    }

    public City(String name, Region region) {
        this.name = name;
        this.region = region;
    }
}
