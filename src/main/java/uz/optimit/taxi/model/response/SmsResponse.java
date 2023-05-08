package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {
     private UUID id;
     private String status;
     private String message;
}
