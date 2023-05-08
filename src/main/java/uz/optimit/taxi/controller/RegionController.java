package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.service.RegionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
public class RegionController {

     private final RegionService regionService;
     @PostMapping("/add")
     @PreAuthorize("hasRole('ADMIN')")
     public ApiResponse addRegion(@RequestBody RegionRegisterRequestDto regionRegisterRequestDto) {
          return regionService.addRegion(regionRegisterRequestDto);
     }

     @GetMapping("/getRegionList")
     public ApiResponse getRegionList() {
          return regionService.getRegionList();
     }

     @GetMapping("/regionById/{id}")
     public ApiResponse getRegionById(@PathVariable Integer id){
          return regionService.getRegionById(id);
     }

     @DeleteMapping("/delete/{id}")
     @PreAuthorize("hasAnyRole('ADMIN')")
     public ApiResponse deleteRegionById(@PathVariable Integer id) {
          return regionService.deleteRegionById(id);
     }
}
