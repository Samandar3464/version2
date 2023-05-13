package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.service.ParcelNotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/parcelController")
public class ParcelNotificationController {

    private final ParcelNotificationService parcelNotificationService;

    @PostMapping("/notificationForDriver")
    public ApiResponse createForDriver(@RequestBody NotificationRequestDto notificationRequestDto){
        return parcelNotificationService.createNotificationForDriver(notificationRequestDto);
    }

    @PostMapping("/notificationForPassenger")
    public ApiResponse createForPassenger(@RequestBody NotificationRequestDto notificationRequestDto){
        return parcelNotificationService.createNotificationForPassenger(notificationRequestDto);
    }
}
