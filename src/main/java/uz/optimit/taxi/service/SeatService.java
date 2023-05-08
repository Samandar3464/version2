package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.Seat;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.response.SeatResponse;
import uz.optimit.taxi.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
     private final SeatRepository seatRepository;

    public void createCarSeats(byte count , Car car){
          List<Seat> seatList  = new ArrayList<>();
          for (byte i = 1; i <= count; i++) {
               seatList.add(seatRepository.save(
                   Seat.builder()
                       .seatNumber(i)
                       .active(false)
                       .car(car)
                       .build()));
          }
     }

     @ResponseStatus(HttpStatus.CREATED)
     public ApiResponse onActive(List<UUID> seatIdList) {
          List<Seat> byIdIn = seatRepository.findAllByIdIn(seatIdList);
          byIdIn.forEach(seat -> {
               seat.setActive(true);
          });
          seatRepository.saveAll(byIdIn);
          return new ApiResponse(getSeatResponses(byIdIn),true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse ofActive(List<UUID> seatIdList) {
          List<Seat> byIdIn = seatRepository.findAllByIdIn(seatIdList);
          byIdIn.forEach(seat -> {
               seat.setActive(false);
          });
          seatRepository.saveAll(byIdIn);
          return new ApiResponse(getSeatResponses(byIdIn),true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getSeatListByCarId(UUID carID){
          List<Seat> allByCarId = seatRepository.findAllByCarIdAndActive(carID,true);
          return new ApiResponse(getSeatResponses(allByCarId),true);
     }

     @ResponseStatus(HttpStatus.OK)
     public ApiResponse getActiveSeatListByCarId(UUID carID){
          List<Seat> allByCarId = seatRepository.findAllByCarId(carID);
          return new ApiResponse(getSeatResponses(allByCarId),true);
     }

     private static List<SeatResponse> getSeatResponses(List<Seat> allByCarId) {
          List<SeatResponse> seatList = new ArrayList<>();
          allByCarId.forEach(seat -> {
               seatList.add(SeatResponse.from(seat));
          });
          return seatList;
     }
}
