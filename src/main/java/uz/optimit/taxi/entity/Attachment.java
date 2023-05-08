package uz.optimit.taxi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Attachment  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String originName;
    private long size;
    private String newName;
    private String type;
    private String contentType;
    private String path;
    @ManyToOne
    private Car car;
}
