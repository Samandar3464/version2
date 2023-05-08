package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AcceptDriverRequestDto;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.service.NotificationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/addNotificationToDriver")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse createNotificationToDriver(@RequestBody NotificationRequestDto notificationRequestDto) {
        return notificationService.createNotificationForDriver(notificationRequestDto);
    }
    @PostMapping("/addNotificationToPassenger")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse createNotificationToPassenger(@RequestBody NotificationRequestDto notificationRequestDto) {
        return notificationService.createNotificationForPassenger(notificationRequestDto);
    }

    @GetMapping("/getDriverNotification")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverPostedNotification(){
        return notificationService.getDriverPostedNotification();
    }

    @GetMapping("/getPassengerNotification")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getPassengerPostedNotification(){
        return notificationService.getPassengerPostedNotification();
    }

    @GetMapping("/seeNotificationForDriver")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse seeNotificationComeToDriver(){
        return notificationService.seeNotificationForDriver();
    }

    @GetMapping("/seeNotificationForPassenger")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse seeNotificationComeToPassenger(){
        return notificationService.seeNotificationForPassenger();
    }
    @DeleteMapping("/deleteNotification/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse deleteNotification(@PathVariable UUID id){
        return notificationService.deleteNotification(id);
    }

    @PostMapping("/acceptDiverRequest")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse joinDiverRequest(@RequestBody AcceptDriverRequestDto acceptDriverRequestDto){
        return notificationService.acceptDiverRequest(acceptDriverRequestDto);
    }

    @GetMapping("/acceptPassengerRequest/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse joinPassengerRequest(@PathVariable UUID id){
        return notificationService.acceptPassengerRequest(id);
    }
    @GetMapping("/getAcceptedNotificationsForDriver")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getAcceptedNotificationForDriver(){
        return notificationService.getAcceptedNotificationForDriver();
    }

    @GetMapping("/getAcceptedNotificationsForPassenger")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getAcceptedNotificationForPassenger(){
        return notificationService.getAcceptedNotificationForPassenger();
    }


    @GetMapping("changeToRead/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse changeToRead(@PathVariable UUID id){
       return notificationService.changeToRead(id);
    }
}
