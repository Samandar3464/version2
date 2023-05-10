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
import uz.optimit.taxi.model.request.GetByFilter;
import uz.optimit.taxi.service.AnnouncementDriverService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/driver")
public class AnnouncementDriverController {

    private final AnnouncementDriverService announcementDriverService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse addDriverAnnouncement(@RequestBody AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto){
        return announcementDriverService.add(announcementDriverRegisterRequestDto);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getDriverById(@PathVariable("id")UUID id){
        return announcementDriverService.getDriverAnnouncementById(id);
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getById(@PathVariable("id")UUID id){
        return announcementDriverService.getById(id);
    }

    @GetMapping("/getListForAnonymousUser")
    public ApiResponse getDriverAnnouncementListForAnonymousUser(){
        return announcementDriverService.getDriverAnnouncementListForAnonymousUser();
    }

    @GetMapping("/getDriverAnnouncements")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse getDriverAnnouncements(){
        return announcementDriverService.getDriverAnnouncements();
    }

    @DeleteMapping("/deleteDriverAnnouncements/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse deleteDriverAnnouncement(@PathVariable UUID id){
        return announcementDriverService.deleteDriverAnnouncement(id);
    }

    @PostMapping("/getAnnouncementDriverByFilterWithoutCity")
    public ApiResponse getByFilterWithCity(@RequestBody GetByFilter getByFilter){
       return announcementDriverService.getByFilterWithoutCity(getByFilter);
    }

    @PostMapping("/getAnnouncementDriverByFilterWithCity")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getByFilterWithoutCity(@RequestBody GetByFilter getByFilter){
        return announcementDriverService.getByFilterWithCity(getByFilter);
    }
    @GetMapping("/getDriverAnnouncementHistory")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','ADMIN')")
    public ApiResponse getDriverAnnouncementHistory(){
        return announcementDriverService.getHistory();
    }
}
