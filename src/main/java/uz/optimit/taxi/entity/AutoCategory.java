package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AutoCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private boolean active;

    @OneToMany(mappedBy = "autoCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AutoModel> autoModel;

    public AutoCategory(Integer id, String name,boolean active) {
        this.id = id;
        this.name = name;
        this.active=active;
    }
}
