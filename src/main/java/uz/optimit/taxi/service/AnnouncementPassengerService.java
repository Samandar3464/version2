package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AnnouncementPassenger;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementAlreadyExistException;
import uz.optimit.taxi.exception.AnnouncementAvailable;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementPassengerRegisterRequestDto;
import uz.optimit.taxi.model.request.GetByFilter;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponse;
import uz.optimit.taxi.model.response.AnnouncementPassengerResponseAnonymous;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementPassengerService {

    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final UserService userService;
    private final FamiliarRepository familiarRepository;
    private final AnnouncementPassengerRepository announcementPassengerRepository;
    private final AnnouncementDriverRepository announcementDriverRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        if (announcementDriverRepository.existsByUserIdAndActiveTrueAndDeletedFalse(user.getId())) {
            throw new AnnouncementAvailable(ANNOUNCEMENT_DRIVER_ALREADY_EXIST);
        }
        if (existByUserIdAndActiveTrueAndDeletedFalse(user.getId())) {
            throw new AnnouncementAlreadyExistException(ANNOUNCEMENT_PASSENGER_ALREADY_EXIST);
        }
        AnnouncementPassenger announcementPassenger = fromRequest(announcementPassengerRegisterRequestDto, user);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerListForAnonymousUser() {
        List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
        announcementPassengerRepository.findAllByActiveTrueAndTimeToTravelAfterOrderByCreatedTimeDesc(LocalDateTime.now().minusDays(1)).forEach(a ->
                passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a)));
        return new ApiResponse(passengerResponses, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAnnouncementById(UUID id) {
        AnnouncementPassenger active = getByIdAndActiveAndDeletedFalse(id, true);
        User user = userService.checkUserExistById(active.getUser().getId());
        UserResponseDto userResponseDto = userService.fromUserToResponse(user);
        AnnouncementPassengerResponse passengerResponse =
                AnnouncementPassengerResponse.from(active, userResponseDto);
        return new ApiResponse(passengerResponse, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getById(UUID id) {
        AnnouncementPassenger active = announcementPassengerRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND));
        User user = userService.checkUserExistById(active.getUser().getId());
        UserResponseDto userResponseDto = userService.fromUserToResponse(user);
        AnnouncementPassengerResponse passengerResponse =
                AnnouncementPassengerResponse.from(active, userResponseDto);
        return new ApiResponse(passengerResponse, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerAnnouncements() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementPassenger> announcementPassengers = announcementPassengerRepository.findAllByUserIdAndActiveAndDeletedFalseAndTimeToTravelAfter(user.getId(), true, LocalDateTime.now().minusDays(1));
        List<AnnouncementPassengerResponseAnonymous> anonymousList = new ArrayList<>();
        announcementPassengers.forEach(obj -> anonymousList.add(AnnouncementPassengerResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deletePassengerAnnouncement(UUID id) {
        AnnouncementPassenger announcementPassenger = announcementPassengerRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND));
        announcementPassenger.setDeleted(false);
        announcementPassengerRepository.save(announcementPassenger);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByFilter(GetByFilter getByFilter) {
        List<AnnouncementPassenger> byFilter = announcementPassengerRepository
                .findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToTravelAfterAndTimeToTravelBetweenOrderByCreatedTimeDesc(
                        getByFilter.getFromRegionId(),
                        getByFilter.getToRegionId(),
                        LocalDateTime.now().minusDays(1),
                        getByFilter.getTime1(),
                        getByFilter.getTime2());
        List<AnnouncementPassengerResponseAnonymous> passengerResponses = new ArrayList<>();
        byFilter.forEach(a -> passengerResponses.add(AnnouncementPassengerResponseAnonymous.from(a)));
        return new ApiResponse(passengerResponses, true);
    }

    public ApiResponse getHistory() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementPassenger> allByActive = announcementPassengerRepository.findAllByUserIdAndDeletedFalse(user.getId());
        List<AnnouncementPassengerResponse> response = new ArrayList<>();
        UserResponseDto userResponseDto = userService.fromUserToResponse(userService.checkUserExistById(user.getId()));
        allByActive.forEach(announcementPassenger -> response.add(AnnouncementPassengerResponse.from(announcementPassenger, userResponseDto)));
        return new ApiResponse(response, true);
    }

    public AnnouncementPassenger getByIdAndActive(UUID announcement_id, boolean active) {
        return announcementPassengerRepository.findByIdAndActive(announcement_id, active)
                .orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND));
    }

    public AnnouncementPassenger getByIdAndActiveAndDeletedFalse(UUID announcement_id, boolean active) {
        return announcementPassengerRepository.findByIdAndActiveAndDeletedFalse(announcement_id, active)
                .orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND));
    }

    public AnnouncementPassenger getByUserId(UUID user_id) {
        return announcementPassengerRepository.findByUserId(user_id)
                .orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND));
    }

    public boolean existByUserIdAndActiveTrueAndDeletedFalse(UUID user_id) {
        return announcementPassengerRepository.existsByUserIdAndActiveTrueAndDeletedFalse(user_id);
    }

    public List<AnnouncementPassenger> getAnnouncementPassenger(User passenger) {
        return announcementPassengerRepository.findAllByUserIdAndActiveAndDeletedFalseAndTimeToTravelAfter(passenger.getId(), true, LocalDateTime.now().minusDays(1));
    }

    private AnnouncementPassenger fromRequest(AnnouncementPassengerRegisterRequestDto announcementPassengerRegisterRequestDto, User user) {
        AnnouncementPassenger announcementPassenger = AnnouncementPassenger.from(announcementPassengerRegisterRequestDto);
        announcementPassenger.setUser(user);
        announcementPassenger.setFromRegion(regionRepository.getById(announcementPassengerRegisterRequestDto.getFromRegionId()));
        announcementPassenger.setToRegion(regionRepository.getById(announcementPassengerRegisterRequestDto.getToRegionId()));
        announcementPassenger.setFromCity(cityRepository.getById(announcementPassengerRegisterRequestDto.getFromCityId()));
        announcementPassenger.setToCity(cityRepository.getById(announcementPassengerRegisterRequestDto.getToCityId()));
        announcementPassenger.setPassengersList(familiarRepository.findByIdInAndActive(announcementPassengerRegisterRequestDto.getPassengersList(), true));
        return announcementPassenger;
    }
}
