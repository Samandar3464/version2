package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.service.SeatService;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v2/seat/")
@RequiredArgsConstructor
public class SeatController {
     private final SeatService seatService;

     @PostMapping("ofActive")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
     public ApiResponse ofActive(@RequestBody List<UUID> uuidList){
          return seatService.ofActive(uuidList);
     }

     @GetMapping("getSeatListByCarId/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI')")
     public ApiResponse getSeatListByCarId(@PathVariable UUID id){
          return seatService.getActiveSeatListByCarId(id);
     }
}
