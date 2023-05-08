package uz.optimit.taxi.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.configuration.jwtConfig.JwtGenerate;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.*;
import uz.optimit.taxi.model.response.TokenResponse;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.FamiliarRepository;
import uz.optimit.taxi.repository.RoleRepository;
import uz.optimit.taxi.repository.StatusRepository;
import uz.optimit.taxi.repository.UserRepository;
import uz.optimit.taxi.entity.CountMassage;
import uz.optimit.taxi.repository.CountMassageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final JwtGenerate jwtGenerate;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final FamiliarRepository familiarRepository;
    private final StatusRepository statusRepository;
    private final SmsService service;
    private final CountMassageRepository countMassageRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse registerUser(UserRegisterDto userRegisterDto) {
        boolean byPhone = userRepository.existsByPhone(userRegisterDto.getPhone());
        if (byPhone) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        Integer verificationCode = verificationCodeGenerator();
//        service.sendSms(SmsModel.builder()
//                .mobile_phone(userRegisterDto.getPhone())
//                .message("DexTaxi. Tasdiqlash kodi: " + verificationCode ., Yo'linggiz behatar  bo'lsin.)
//                .fromForDriver(4546)
//                .callback_url("http://0000.uz/test.php")
//                .build());
        countMassageRepository.save(new CountMassage(userRegisterDto.getPhone(),1,LocalDateTime.now()));
        System.out.println("verificationCode = " + verificationCode);
        User user = User.fromPassenger(userRegisterDto, passwordEncoder, attachmentService, verificationCode, roleRepository);
        User save = userRepository.save(user);
        statusRepository.save(new Status(0, 0,save));
        familiarRepository.save(Familiar.fromUser(save));
        return new ApiResponse(SUCCESSFULLY, true, new TokenResponse(JwtGenerate.generateAccessToken(user), JwtGenerate.generateRefreshToken(user), UserResponseDto.fromDriver(user, attachmentService.attachUploadFolder)));
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            User user = (User) authenticate.getPrincipal();
            return new ApiResponse(new TokenResponse(JwtGenerate.generateAccessToken(user), JwtGenerate.generateRefreshToken(user), UserResponseDto.fromDriver(user, attachmentService.attachUploadFolder)), true);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse verify(UserVerifyRequestDto userVerifyRequestDto) {
        User user = userRepository.findByPhoneAndVerificationCode(userVerifyRequestDto.getPhone(), userVerifyRequestDto.getVerificationCode())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
//        if (!verificationCodeLiveTime(user.getVerificationCodeLiveTime())) {
//            throw new TimeExceededException(CODE_TIME_OUT);
//        }
        user.setVerificationCode(0);
        user.setBlocked(true);
        userRepository.save(user);
        return new ApiResponse(USER_VERIFIED_SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getToken(HttpServletRequest request) throws Exception {
        String accessTokenByRefresh = jwtGenerate.checkRefreshTokenValidAndGetAccessToken(request);
        return new ApiResponse("NEW ACCESS TOKEN ", true, new TokenResponse(accessTokenByRefresh));
    }

    @ResponseStatus(HttpStatus.OK)
    public User checkUserExistByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User user = (User) authentication.getPrincipal();
        return userRepository.findByPhone(user.getPhone()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.OK)
    public User checkUserExistById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.OK)
    private boolean verificationCodeLiveTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();
        int day = now.getDayOfMonth() - localDateTime.getDayOfMonth();
        int hour = now.getHour() - localDateTime.getHour();
        int minute = now.getMinute() - localDateTime.getMinute();
        if (day == 0 && hour == 0 && minute <= 2) {
            return true;
        }
        return false;
    }

    @ResponseStatus(HttpStatus.OK)
    private Integer verificationCodeGenerator() {
        return RandomGenerator.getDefault().nextInt(100000, 999999);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByUserId(UUID id) {
        User user = checkUserExistById(id);
        return new ApiResponse(UserResponseDto.fromDriver(user, attachmentService.attachUploadFolder), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse setStatus(StatusDto statusDto) {
        User user = checkUserExistById(statusDto.getUserId());
        Status status = Status.from(statusDto, user.getStatus());
        statusRepository.save(status);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteUserByID(UUID id) {
        Optional<User> byId = userRepository.findById(id);
        byId.get().setBlocked(true);
        userRepository.save(byId.get());
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse saveFireBaseToken(FireBaseTokenRegisterDto fireBaseTokenRegisterDto) {
        User user = checkUserExistById(fireBaseTokenRegisterDto.getUserId());
        user.setFireBaseToken(fireBaseTokenRegisterDto.getFireBaseToken());
        userRepository.save(user);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    public void addRoleDriver(List<Car> carList) {
        User user = userRepository.findByCarsIn(carList).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        List<Role> roles = user.getRoles();
        Role byName = roleRepository.findByName(DRIVER);
        if (!roles.contains(byName)) {
            roles.add((roleRepository.findByName(DRIVER)));
        }
        userRepository.save(user);
    }

    private void countMassage() {
        List<CountMassage> all = countMassageRepository.findAll();
        if (all.isEmpty()) {
            countMassageRepository.save(CountMassage.builder().count(1).build());
        } else {
            CountMassage countMassage = all.get(0);
            countMassage.setCount(countMassage.getCount() + 1);
            countMassageRepository.save(countMassage);
        }
    }
}


