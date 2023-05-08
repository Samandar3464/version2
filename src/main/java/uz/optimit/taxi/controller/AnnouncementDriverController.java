package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementDriverService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver")
public class AnnouncementDriverController {

    private final AnnouncementDriverService announcementDriverService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse addDriverAnnouncement(@RequestBody AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto){
        return announcementDriverService.add(announcementDriverRegisterRequestDto);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverById(@PathVariable("id")UUID id){
        return announcementDriverService.getDriverById(id);
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getById(@PathVariable("id")UUID id){
        return announcementDriverService.getById(id);
    }

    @GetMapping("/getListForAnonymousUser")
    public ApiResponse getDriverListForAnonymousUser(){
        return announcementDriverService.getDriverListForAnonymousUser();
    }

    @GetMapping("/getDriverAnnouncements")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverAnnouncements(){
        return announcementDriverService.getDriverAnnouncements();
    }

    @DeleteMapping("/deleteDriverAnnouncements/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id){
        return announcementDriverService.deleteDriverAnnouncement(id);
    }

    @GetMapping("getAnnouncementDriverByFilter/{from}/{to}/{fromCity}/{toCity}/{fromTime}")
    public ApiResponse getByFilter(
        @PathVariable Integer from,
        @PathVariable Integer to,
        @PathVariable Integer  fromCity,
        @PathVariable Integer  toCity,
        @PathVariable String  fromTime
    ){
       return announcementDriverService.getByFilter(from,to ,fromCity,toCity,fromTime+" 00:01",fromTime+" 23:59");
    }

    @GetMapping("getAnnouncementDriverByFilter/{from}/{to}/{fromTime}")
    public ApiResponse getByFilter(
        @PathVariable Integer from,
        @PathVariable Integer to,
        @PathVariable String fromTime
    ){
        return announcementDriverService.getByFilter(from,to ,fromTime+" 00:01",fromTime+" 23:59");
    }
    @GetMapping("/getDriverAnnouncementHistory")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverAnnouncementHistory(){
        return announcementDriverService.getHistory();
    }
}
