package uz.optimit.taxi.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetByFilter {

    private Integer fromRegionId;

    private Integer toRegionId;

    private Integer fromCityId;

    private Integer toCityId;

    private LocalDateTime time1;

    private LocalDateTime time2;
}
