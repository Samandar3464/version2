package uz.optimit.taxi.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.AdvertisingRequestDto;
import uz.optimit.taxi.service.AdvertisingService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/advertising")
public class AdvertisingController {

    private final AdvertisingService advertisingService;

    @PostMapping("/add")
    public ApiResponse add(@ModelAttribute AdvertisingRequestDto advertisingRequestDto) {
        return advertisingService.add(advertisingRequestDto);
    }

    @GetMapping("/activate/{id}")
    public ApiResponse activate(@PathVariable UUID id) {
        return advertisingService.deActivate(id);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse delete(@PathVariable UUID id) {
        return advertisingService.deleted(id);
    }
}
