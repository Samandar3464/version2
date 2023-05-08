package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "autoModel")
    private List<Car> car;

    public AutoModel(String name, byte countSeat,AutoCategory autoCategory) {
        this.name = name;
        this.countSeat = countSeat;
        this.autoCategory = autoCategory;
    }
}
