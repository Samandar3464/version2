package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonIgnore
    @ManyToOne
    private AutoCategory autoCategory;

    private byte countSeat;

    private boolean active;
//    @JsonIgnore
//    @OneToMany(mappedBy = "autoModel")
//    private List<Car> car;

    public AutoModel(String name, byte countSeat, AutoCategory autoCategory, boolean active) {
        this.name = name;
        this.countSeat = countSeat;
        this.autoCategory = autoCategory;
        this.active = active;
    }

    public static AutoModel from(AutoModelRegisterRequestDto autoModelRegisterRequestDto, AutoCategory autoCategory) {
        return AutoModel.builder()
                .name(autoModelRegisterRequestDto.getName())
                .countSeat(autoModelRegisterRequestDto.getCountSeat())
                .autoCategory(autoCategory)
                .build();
    }
}
