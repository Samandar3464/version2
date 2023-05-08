package uz.optimit.taxi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.optimit.taxi.model.request.StatusDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long stars;

    private Long count;

    @JsonIgnore
    @OneToOne(mappedBy = "status")
    private User user;

    public Status(int stars, int count) {

    }

    public static Status from(StatusDto statusDto, Status status){
        return Status.builder()
                .id(status.getId())
                .stars(status.getStars()+statusDto.getStars())
                .count(status.getCount()+1)
                .build();
    }
}
