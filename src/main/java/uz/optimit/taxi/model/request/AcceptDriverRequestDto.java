package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptDriverRequestDto {
    private UUID senderId;
    private List<UUID> seatIdList;
}
