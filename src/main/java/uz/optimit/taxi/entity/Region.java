package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    public static Region from(RegionRegisterRequestDto regionRegisterRequestDto){
        return Region.builder().name(regionRegisterRequestDto.getName()).build();
    }
//    @JsonIgnore
//    @OneToMany(mappedBy = "fromRegion")
//    private List<AnnouncementPassenger> fromAnnouncementUser;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "toRegion")
//    private List<AnnouncementPassenger> toAnnouncementUser;

//    public Region(Integer id, String name) {
//        this.id = id;
//        this.name = name;
//    }
}
