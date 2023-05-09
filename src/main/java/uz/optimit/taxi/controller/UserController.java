package uz.optimit.taxi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.*;
import uz.optimit.taxi.service.UserService;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

     private UserService userService;
     @PostMapping("/register")
     public ApiResponse registerUser(@ModelAttribute UserRegisterDto userRegisterDto) {
          return userService.registerUser(userRegisterDto);
     }

     @PostMapping("/login")
     public ApiResponse login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto) {
          return userService.login(userLoginRequestDto);
     }

     @PostMapping("/verify")
     public ApiResponse verify(@RequestBody @Validated UserVerifyRequestDto userVerifyRequestDto) {
          return userService.verify(userVerifyRequestDto);
     }

     @PostMapping("get/token/refreshToken")
     public ApiResponse refreshToken(HttpServletRequest httpServletRequest) throws Exception {
          return userService.getToken(httpServletRequest);
     }

     @GetMapping("/getById/{id}")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse getUserById(@PathVariable UUID id){
         return userService.getByUserId(id);
     }

     @PostMapping("/setStatus")
     @PreAuthorize("hasAnyRole('HAYDOVCHI','YOLOVCHI','ADMIN')")
     public ApiResponse setStatus(@RequestBody StatusDto statusDto){
         return userService.setStatus(statusDto);
     }

     @PutMapping("/block/{id}")
     public ApiResponse blockRegionById(@PathVariable UUID id) {
          return userService.blockUserByID(id);
     }

     @PostMapping("/setFireBaseToken")
     public ApiResponse setFireBaseToken(@RequestBody FireBaseTokenRegisterDto fireBaseTokenRegisterDto){
          return userService.saveFireBaseToken(fireBaseTokenRegisterDto);
     }
}
