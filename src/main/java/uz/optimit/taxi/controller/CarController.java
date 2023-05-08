package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.service.CarService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('YOLOVCHI','ADMIN')")
    public ApiResponse addCar(@ModelAttribute  @Validated  CarRegisterRequestDto carRegisterRequestDto) {
        return carService.addCar(carRegisterRequestDto);
    }

    @GetMapping("/getCar")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse getCar(){
        return carService.getCar();
    }

    @GetMapping("/getCarSeats")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse getCarSeats(){
        return carService.getCarSeat();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse deleteCarByID(@PathVariable UUID id) {
        return carService.deleteCarByID(id);
    }
}
