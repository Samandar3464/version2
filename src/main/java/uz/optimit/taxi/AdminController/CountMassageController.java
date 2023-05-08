package uz.optimit.taxi.AdminController;

import com.google.protobuf.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CountMassageRequest;
import uz.optimit.taxi.service.CountMassageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/countMassage/")
public class CountMassageController {

    private final CountMassageService countMassageService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getAll() {
        return countMassageService.getAllMassagesCount();
    }

    @PostMapping("/getAllByDate")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getAllByDate(@RequestBody CountMassageRequest countMassageRequest) {
        return countMassageService.getAllMassagesCountByDate(countMassageRequest);
    }

    @GetMapping("/getByPhone")
    public ApiResponse getByPhone(){
        return countMassageService.getAllMassagesCountByPhoneNumbers();
    }
}
