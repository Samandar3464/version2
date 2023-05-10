package uz.optimit.taxi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AutoModelRegisterRequestDto;
import uz.optimit.taxi.service.AutoModelService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/autoModel")
public class AutoModelController {

    private final AutoModelService autoModelService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse addAutoModel(@RequestBody AutoModelRegisterRequestDto autoModelRegisterRequestDto) {
        return autoModelService.addAutoCategory(autoModelRegisterRequestDto);
    }
    @GetMapping("/getModelById/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getModelById(@PathVariable int id){
        return autoModelService.getModelById(id);
    }
    @GetMapping("/getModelList/{id}")
    @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
    public ApiResponse getModelList(@PathVariable int id){
        return autoModelService.getModelList(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse deleteModelById(@PathVariable int id) {
        return autoModelService.deleteModelById(id);
    }
}
