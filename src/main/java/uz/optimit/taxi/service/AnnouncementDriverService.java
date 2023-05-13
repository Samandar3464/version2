package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.AnnouncementAlreadyExistException;
import uz.optimit.taxi.exception.AnnouncementAvailable;
import uz.optimit.taxi.exception.AnnouncementNotFoundException;
import uz.optimit.taxi.model.request.AnnouncementDriverRegisterRequestDto;
import uz.optimit.taxi.model.request.GetByFilter;
import uz.optimit.taxi.model.response.AnnouncementDriverActiveResponse;
import uz.optimit.taxi.model.response.AnnouncementDriverResponse;
import uz.optimit.taxi.model.response.AnnouncementDriverResponseAnonymous;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.NotificationRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AnnouncementDriverService {

    private final AnnouncementDriverRepository announcementDriverRepository;
    private final CarService carService;
    private final RegionRepository regionRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final SeatService seatService;
    private final NotificationRepository notificationRepository;
    private final AnnouncementPassengerService announcementPassengerService;
    private final CityRepository cityRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(AnnouncementDriverRegisterRequestDto announcementDriverRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        Car car = carService.getCarByUserId(user.getId());
        if (announcementPassengerService.existByUserIdAndActiveTrueAndDeletedFalse(user.getId())) {
            throw new AnnouncementAvailable(ANNOUNCEMENT_PASSENGER_ALREADY_EXIST);
        }
        if (existByUserIdAndActiveAndDeletedFalse(user.getId())) {
            throw new AnnouncementAlreadyExistException(ANNOUNCEMENT_DRIVER_ALREADY_EXIST);
        }
        fromAnnouncementDriver(announcementDriverRegisterRequestDto, user, car);
        seatService.onActive(announcementDriverRegisterRequestDto.getSeatIdList());
        return new ApiResponse(SUCCESSFULLY, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverAnnouncementListForAnonymousUser() {
        List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
        announcementDriverRepository.findAllByActiveTrueAndTimeToDriveAfterOrderByCreatedTimeDesc(LocalDateTime.now().minusDays(1)).forEach(announcementDriver ->
                driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver)));
        return new ApiResponse(driverResponses, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverAnnouncementById(UUID id) {
        AnnouncementDriver announcementDriver = getByIdAndActiveAndDeletedFalse(id, true);
        Car car = carService.getCarByUserId(announcementDriver.getUser().getId());
        List<Notification> notifications = notificationRepository.findByAnnouncementDriverIdAndActiveAndReceived(announcementDriver.getId(), false, true);
        List<Familiar> familiars = new ArrayList<>();
        notifications.forEach(obj ->
                familiars.addAll(announcementPassengerService.getByIdAndActive(obj.getAnnouncementPassengerId(), false).getPassengersList()));
        AnnouncementDriverResponse announcementDriverResponse = fromAnnouncementDriverResponse(announcementDriver, car, familiars);
        return new ApiResponse(announcementDriverResponse, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getById(UUID id) {
        AnnouncementDriver announcementDriver = announcementDriverRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(DRIVER_ANNOUNCEMENT_NOT_FOUND));
        Car car = carService.getCarByUserId(announcementDriver.getUser().getId());
        List<Notification> notifications = notificationRepository.findByAnnouncementDriverIdAndActiveAndReceived(announcementDriver.getId(), false, true);
        List<Familiar> familiars = new ArrayList<>();
        notifications.forEach(obj ->
                familiars.addAll(announcementPassengerService.getByIdAndActive(obj.getAnnouncementPassengerId(), false).getPassengersList()));
        return new ApiResponse(fromAnnouncementDriverResponse(announcementDriver, car, familiars), true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverAnnouncements() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementDriver> announcementDrivers = announcementDriverRepository.findAllByUserIdAndActiveAndDeletedFalse(user.getId(), true);
        List<AnnouncementDriverActiveResponse> announcementDriverResponses = new ArrayList<>();
        for (AnnouncementDriver announcementDriver : announcementDrivers) {
            announcementDriverResponses.add(AnnouncementDriverActiveResponse.from(announcementDriver));
        }
        return new ApiResponse(announcementDriverResponses, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteDriverAnnouncement(UUID id) {
        AnnouncementDriver announcementDriver = announcementDriverRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException(DRIVER_ANNOUNCEMENT_NOT_FOUND));
        announcementDriver.setDeleted(true);
        announcementDriverRepository.save(announcementDriver);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getHistory() {
        User user = userService.checkUserExistByContext();
        List<AnnouncementDriver> allByActive = announcementDriverRepository.findAllByUserIdAndDeletedFalse(user.getId());
        List<Notification> notifications = notificationRepository.findByAnnouncementDriverIdAndActiveAndReceived(allByActive.get(0).getId(), false, true);
        List<Familiar> familiars = new ArrayList<>();
        notifications.forEach(obj ->
                familiars.addAll(announcementPassengerService.getByIdAndActiveAndDeletedFalse(obj.getAnnouncementPassengerId(), false).getPassengersList()));
        List<AnnouncementDriverResponse> response = new ArrayList<>();
        allByActive.forEach((announcementDriver) -> response.add(fromAnnouncementDriverResponse(announcementDriver, announcementDriver.getCar(), familiars)));
        return new ApiResponse(response, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByFilterWithoutCity(GetByFilter getByFilter) {
        List<AnnouncementDriver> all = announcementDriverRepository.findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToDriveAfterAndTimeToDriveBetweenOrderByCreatedTimeDesc(
                getByFilter.getFromRegionId()
                , getByFilter.getToRegionId()
                , LocalDateTime.now().minusDays(1)
                , getByFilter.getTime1()
                , getByFilter.getTime2());
        List<AnnouncementDriverResponseAnonymous> driverResponses = new ArrayList<>();
        all.forEach(announcementDriver -> {
            driverResponses.add(AnnouncementDriverResponseAnonymous.from(announcementDriver));
        });
        return new ApiResponse(driverResponses, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByFilterWithCity(GetByFilter getByFilter) {
        List<AnnouncementDriver> driverList = announcementDriverRepository
                .findAllByActiveTrueAndFromRegionIdAndToRegionIdAndFromCityIdAndToCityIdAndDeletedFalseAndTimeToDriveAfterAndTimeToDriveBetweenOrderByCreatedTimeDesc(
                        getByFilter.getFromRegionId()
                        , getByFilter.getToRegionId()
                        , getByFilter.getFromCityId()
                        , getByFilter.getToCityId()
                        , LocalDateTime.now().minusDays(1)
                        , getByFilter.getTime1()
                        , getByFilter.getTime2());
        List<AnnouncementDriverResponse> announcementDrivers = new ArrayList<>();
        driverList.forEach(announcementDriver -> announcementDrivers.add(fromAnnouncementDriverResponse(announcementDriver, announcementDriver.getCar(), null)));
        return new ApiResponse(announcementDrivers, true);
    }

    public AnnouncementDriver getByIdAndActive(UUID announcement_id, boolean active) {
        return announcementDriverRepository.findByIdAndActive(announcement_id, active)
                .orElseThrow(() -> new AnnouncementNotFoundException(DRIVER_ANNOUNCEMENT_NOT_FOUND));
    }

    public AnnouncementDriver getByIdAndActiveAndDeletedFalse(UUID announcement_id, boolean active) {
        return announcementDriverRepository.findByIdAndActiveAndDeletedFalse(announcement_id, active)
                .orElseThrow(() -> new AnnouncementNotFoundException(DRIVER_ANNOUNCEMENT_NOT_FOUND));
    }

    public List<AnnouncementDriver> getByUserIdAndActiveAndDeletedFalse(UUID user_id, boolean active) {
        return announcementDriverRepository.findAllByUserIdAndActiveAndDeletedFalse(user_id, active);
    }

    public List<AnnouncementDriver> getByUserIdAndActiveAndParcelAndDeletedFalse(UUID user_id, boolean active) {
        return announcementDriverRepository.findAllByUserIdAndActiveAndParcelTrueAndDeletedFalse(user_id, active);
    }

    public boolean existByUserIdAndActiveAndDeletedFalse(UUID userId) {
        return announcementDriverRepository.existsByUserIdAndActiveTrueAndDeletedFalse(userId);
    }

    private void fromAnnouncementDriver(AnnouncementDriverRegisterRequestDto announcement, User user, Car car) {
        AnnouncementDriver announcementDriver = AnnouncementDriver.from(announcement);
        announcementDriver.setCar(car);
        announcementDriver.setUser(user);
        announcementDriver.setFromRegion(regionRepository.getById(announcement.getFromRegionId()));
        announcementDriver.setToRegion(regionRepository.getById(announcement.getToRegionId()));
        announcementDriver.setFromCity(announcement.getFromCityId() == null ? null : cityRepository.getById(announcement.getFromCityId()));
        announcementDriver.setToCity(announcement.getToCityId() == null ? null : cityRepository.getById(announcement.getToCityId()));
        announcementDriverRepository.save(announcementDriver);
    }

    private AnnouncementDriverResponse fromAnnouncementDriverResponse(AnnouncementDriver announcementDriver, Car car, List<Familiar> familiars) {
        List<Attachment> attachmentList = car.getAutoPhotos();
        List<String> photos = new ArrayList<>();
        attachmentList.forEach(attach -> {
            photos.add(attachmentService.attachUploadFolder + attach.getPath() + "/" + attach.getNewName() + "." + attach.getType());
        });
        AnnouncementDriverResponse announcement = AnnouncementDriverResponse.from(announcementDriver);
        announcement.setFamiliars(familiars);
        announcement.setCarPhotoPath(photos);
        announcement.setColor(car.getColor());
        announcement.setCarNumber(car.getCarNumber());
        announcement.setAutoModel(car.getAutoModel().getName());
        announcement.setUserResponseDto(userService.fromUserToResponse(announcementDriver.getUser()));
        return announcement;
    }

}
