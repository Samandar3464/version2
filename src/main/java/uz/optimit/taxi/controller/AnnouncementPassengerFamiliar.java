package uz.optimit.taxi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.FamiliarRegisterRequestDto;
import uz.optimit.taxi.service.AnnouncementFamiliarService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/forFamiliar")
public class AnnouncementPassengerFamiliar {
     private final AnnouncementFamiliarService announcementFamiliarService;

     @PostMapping("/add")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse add(@RequestBody @Valid FamiliarRegisterRequestDto  familiarRegisterRequestDto) {
         return announcementFamiliarService.addForFamiliar(familiarRegisterRequestDto);
     }

     @GetMapping("/getFamiliarList")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getList() {
          return announcementFamiliarService.getFamiliarListByUser();
     }

     @PostMapping("/getFamiliarListByIdList")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getListByIds(@RequestBody List<UUID> list) {
          return announcementFamiliarService.getFamiliarListById(list);
     }

     @DeleteMapping("/delete/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse deleteFamiliar(@PathVariable UUID id) {
          return announcementFamiliarService.deleteFamiliar(id);
     }

}
