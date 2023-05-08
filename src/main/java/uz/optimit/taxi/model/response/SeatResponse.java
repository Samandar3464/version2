package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.optimit.taxi.entity.Seat;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatResponse {
     private UUID id;
     private int seatNumber;
     private boolean active;

     public static SeatResponse from(Seat seat){
          return SeatResponse
              .builder()
              .active(seat.isActive())
              .seatNumber(seat.getSeatNumber())
              .id(seat.getId())
              .build();
     }

}
