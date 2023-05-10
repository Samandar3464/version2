package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AutoCategoryRegisterRequestDto;
import uz.optimit.taxi.service.AutoCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/autoCategory")
public class AutoCategoryController {

     private final AutoCategoryService autoCategoryService;

     @PostMapping("/add")
     @PreAuthorize("hasRole('ADMIN')")
     public ApiResponse addRegion(@RequestBody AutoCategoryRegisterRequestDto autoCategoryRegisterRequestDto) {
          return autoCategoryService.addAutoCategory(autoCategoryRegisterRequestDto);
     }

     @GetMapping("/getCategoryById/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getCategoryById(@PathVariable int id) {
          return autoCategoryService.getCategoryById(id);
     }

     @GetMapping("/getCategoryList")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getCategoryList() {
          return autoCategoryService.getCategoryList();
     }

     @DeleteMapping("/delete/{id}")
     @PreAuthorize("hasAnyRole('ADMIN')")
     public ApiResponse deleteAutoCategoryById(@PathVariable int id) {
          return autoCategoryService.deleteAutoCategoryById(id);
     }

}
