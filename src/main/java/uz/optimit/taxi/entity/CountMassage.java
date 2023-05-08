package uz.optimit.taxi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CountMassage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String phone;

    private int count;

    private LocalDateTime  sandedTime;

    public CountMassage(String phone, int count,LocalDateTime sandedTime) {
        this.phone = phone;
        this.count = count;
        this.sandedTime=sandedTime;
    }
}
