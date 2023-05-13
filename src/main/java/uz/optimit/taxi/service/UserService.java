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
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.exception.UserAlreadyExistException;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.model.request.*;
import uz.optimit.taxi.model.response.TokenResponse;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.*;

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
    private final AnnouncementPassengerRepository announcementPassengerRepository;

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
//                .message("DexTaxi. Tasdiqlash kodi: " + verificationCode + ". Yo'linggiz behatar  bo'lsin.")
//                .from(4546)
//                .callback_url("http://0000.uz/test.php")
//                .build());
        countMassageRepository.save(new CountMassage(userRegisterDto.getPhone(), 1, LocalDateTime.now()));
        User user = userRepository.save(from(userRegisterDto, verificationCode));
        Status status = statusRepository.save(Status.builder().user(user).count(1L).stars(5L).build());
        familiarRepository.save(Familiar.fromUser(user));
        user.setStatus(status);
        return new ApiResponse(SUCCESSFULLY, true, new TokenResponse(JwtGenerate.generateAccessToken(user), JwtGenerate.generateRefreshToken(user), fromUserToResponse(user)));
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userLoginRequestDto.getPhone(), userLoginRequestDto.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authentication);
            User user = (User) authenticate.getPrincipal();
            return new ApiResponse(new TokenResponse(JwtGenerate.generateAccessToken(user), JwtGenerate.generateRefreshToken(user), fromUserToResponse(user)), true);
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
    public ApiResponse getByUserId(UUID id) {
        User user = checkUserExistById(id);
        return new ApiResponse(fromUserToResponse(user), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse setStatus(StatusDto statusDto) {
        User user = checkUserExistById(statusDto.getUserId());
        Status status = Status.from(statusDto, user.getStatus());
        statusRepository.save(status);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse blockUserByID(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        user.setBlocked(true);
        userRepository.save(user);
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
        if (!user.getRoles().contains(byName)) {
            roles.add((byName));
        }
        userRepository.save(user);
    }

    private User from(UserRegisterDto userRegisterDto, int verificationCode) {
        User user = User.from(userRegisterDto, verificationCode);
        Attachment attachment = null;
        if (userRegisterDto.getProfilePhoto() != null) {
            attachment = attachmentService.saveToSystem(userRegisterDto.getProfilePhoto());
        }
        user.setProfilePhoto(attachment);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
//        user.setRoles(List.of(roleRepository.findByName(PASSENGER)));
        user.setRoles(List.of(roleRepository.findByName(PASSENGER), roleRepository.findByName(DRIVER)));
        return user;
    }

    public UserResponseDto fromUserToResponse(User user) {
//       String photoLink = downloadUrl + "avatar.png";
        String photoLink = "https://sb.kaleidousercontent.com/67418/992x558/7632960ff9/people.png";
        if (user.getProfilePhoto() != null) {
            Attachment attachment = user.getProfilePhoto();
            photoLink = attachmentService.attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
        }
        UserResponseDto userResponseDto = UserResponseDto.from(user);
        Optional<AnnouncementPassenger> announcementPassenger = announcementPassengerRepository.findByUserIdAndActiveAndDeletedFalse(user.getId(), true);
        if (announcementPassenger.isPresent()){
            userResponseDto.setPassengersList(announcementPassenger.get().getPassengersList());
        }
        userResponseDto.setProfilePhotoUrl(photoLink);
        return userResponseDto;
    }

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

    private Integer verificationCodeGenerator() {
        return RandomGenerator.getDefault().nextInt(100000, 999999);
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


