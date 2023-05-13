package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.GetByFilter;
import uz.optimit.taxi.model.request.ParcelRegisterRequestDto;
import uz.optimit.taxi.service.ParcelService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/parcel")
public class PassengerParcelController {

     private final ParcelService parcelService;

     @PostMapping("/add")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse addParcel(@RequestBody ParcelRegisterRequestDto parcelRegisterRequestDto) {
          return parcelService.add(parcelRegisterRequestDto);
     }

     @GetMapping("/getList")
     public ApiResponse getParcelList() {
          return parcelService.getParcelList();
     }

     @GetMapping("/getById/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getByIdAndActive(@PathVariable("id") UUID id) {
          return parcelService.getParcelByIdAndActive(id);
     }

     @GetMapping("/byId/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getById(@PathVariable("id") UUID id) {
          return parcelService.getParcelById(id);
     }

     @DeleteMapping("/deleteParcel/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse deletePassengerAnnouncement(@PathVariable UUID id) {
          return parcelService.deleteParcel(id);
     }

     @GetMapping("/getPassengerParcelHistory")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getPassengerParcelHistory(){
          return parcelService.getHistory();
     }

     @PostMapping("/getPassengerParcelByFilter")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getPassengerParcelByFilter(@RequestBody GetByFilter getByFilter) {
          return parcelService.getByFilter(getByFilter);
     }
     @GetMapping("/getPassengerParcels")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getPassengerAnnouncements() {
          return parcelService.getPassengerParcels();
     }

    }
