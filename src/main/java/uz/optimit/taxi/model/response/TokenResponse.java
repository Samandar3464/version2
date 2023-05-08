package uz.optimit.taxi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.optimit.taxi.model.response.UserResponseDto;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto userResponseDto;
     public TokenResponse(String accessToken) {
          this.accessToken=accessToken;
     }
}
