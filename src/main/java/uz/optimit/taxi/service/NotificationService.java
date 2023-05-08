package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.*;
import uz.optimit.taxi.entity.Enum.NotificationType;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.*;
import uz.optimit.taxi.model.request.AcceptDriverRequestDto;
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.model.response.*;
import uz.optimit.taxi.repository.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AnnouncementPassengerRepository announcementPassengerRepository;
    private final AnnouncementDriverRepository announcementDriverRepository;
    private final UserService userService;
    private final CarRepository carRepository;
    private final SeatRepository seatRepository;
    private final FireBaseMessagingService fireBaseMessagingService;

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createNotificationForDriver(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        announcementDriverRepository.findByIdAndActive(notificationRequestDto.getAnnouncementDriverId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        List<AnnouncementPassenger> allByUserIdAndActive = announcementPassengerRepository.findAllByUserIdAndActive(user.getId(), true);
        if (allByUserIdAndActive.isEmpty()) {
            throw new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND);
        }
        AnnouncementPassenger announcementPassenger = allByUserIdAndActive.get(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("announcementId", announcementPassenger.getId().toString());
        notificationRequestDto.setDate(data);
        notificationRequestDto.setAnnouncementPassengerId(announcementPassenger.getId());
        notificationRequestDto.setTitle(YOU_COME_TO_MESSAGE_FROM_PASSENGER);
        notificationRequestDto.setNotificationType(NotificationType.DRIVER);
        Notification notification = from(notificationRequestDto, user);
//        UserResponseDto userResponseDto = UserResponseDto.fromForDriver(user, attachmentService.attachDownloadUrl, announcementPassengerRepository,notification.getId());
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForDriver(notificationRequestDto, notification.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

        return new ApiResponse(SUCCESSFULLY, true);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createNotificationForPassenger(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        announcementPassengerRepository.findByIdAndActive(notificationRequestDto.getAnnouncementPassengerId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
        List<AnnouncementDriver> byUserIdAndActive = announcementDriverRepository.findAllByUserIdAndActive(user.getId(), true);
        if (byUserIdAndActive.isEmpty()) {
            throw new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND);
        }
        AnnouncementDriver announcementDriver = byUserIdAndActive.get(0);
        notificationRequestDto.setAnnouncementDriverId(announcementDriver.getId());

        HashMap<String, String> data = new HashMap<>();
        data.put("announcementId", announcementDriver.getId().toString());
        notificationRequestDto.setDate(data);

        notificationRequestDto.setTitle(YOU_COME_TO_MESSAGE_FROM_DRIVER);
        notificationRequestDto.setNotificationType(NotificationType.PASSENGER);
        Notification notification = from(notificationRequestDto, user);
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForPassenger(notificationRequestDto, notification.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getPassengerPostedNotification() {
        User user = userService.checkUserExistByContext();
        List<Notification> notifications = notificationRepository.findAllBySenderIdAndActiveAndReceived(user.getId(), true, false);

        List<AnnouncementDriver> announcementDrivers = new ArrayList<>();
        notifications.forEach(obj -> announcementDrivers.add(announcementDriverRepository.findByIdAndActive(obj.getAnnouncementDriverId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementDriverResponseAnonymous> anonymousList = new ArrayList<>();
        announcementDrivers.forEach(obj -> anonymousList.add(AnnouncementDriverResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getDriverPostedNotification() {
        User user = userService.checkUserExistByContext();

        List<Notification> notification = notificationRepository
                .findAllBySenderIdAndActiveAndReceived(user.getId(), true, false);

        List<AnnouncementPassenger> announcementPassengers = new ArrayList<>();
        notification.forEach(obj -> announcementPassengers.add(announcementPassengerRepository.findByIdAndActive(obj.getAnnouncementPassengerId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND))));

        List<AnnouncementPassengerResponseAnonymous> anonymousList = new ArrayList<>();
        announcementPassengers.forEach(obj -> anonymousList.add(AnnouncementPassengerResponseAnonymous.from(obj)));
        return new ApiResponse(anonymousList, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse seeNotificationForDriver() {
        User user = userService.checkUserExistByContext();

        List<Notification> notifications = notificationRepository
                .findAllByReceiverIdAndActiveAndReceivedAndNotificationTypeOrderByCreatedTimeDesc(user.getId(), true, false, NotificationType.DRIVER);

        List<AnnouncementResponseForList> announcementResponseForLists = new ArrayList<>();
        notifications.forEach(obj -> announcementResponseForLists.add(
                AnnouncementResponseForList.
                        from(userService.checkUserExistById(obj.getSenderId()), obj.getId(), obj.getAnnouncementPassengerId())));

        return new ApiResponse(announcementResponseForLists, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse seeNotificationForPassenger() {
        User user = userService.checkUserExistByContext();

        List<Notification> notifications = notificationRepository
                .findAllByReceiverIdAndActiveAndReceivedAndNotificationTypeOrderByCreatedTimeDesc(user.getId(), true, false, NotificationType.PASSENGER);

        List<AnnouncementResponseForList> announcementResponseForLists = new ArrayList<>();
        notifications.forEach(obj -> announcementResponseForLists.add(
                AnnouncementResponseForList.
                        from(userService.checkUserExistById(obj.getSenderId()), obj.getId(), obj.getAnnouncementDriverId())));

        return new ApiResponse(announcementResponseForLists, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteNotification(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));
        notification.setActive(false);
        notificationRepository.save(notification);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = {NotEnoughSeat.class, CarNotFound.class, UserNotFoundException.class, AnnouncementNotFoundException.class, RecordNotFoundException.class})
    public ApiResponse acceptDiverRequest(AcceptDriverRequestDto acceptDriverRequestDto) throws NotEnoughSeat {
        User user = userService.checkUserExistByContext();
        User driver = userService.checkUserExistById(acceptDriverRequestDto.getSenderId());
        Notification fromDriverToUser = getNotification(user, driver);
        AnnouncementPassenger announcementPassenger = getAnnouncementPassenger(fromDriverToUser);
        AnnouncementDriver announcementDriver = getAnnouncementDriver(driver);
        List<Car> activeCars = new ArrayList<>();
        driver.getCars().forEach(car -> activeCars.add(carRepository.findByUserIdAndActiveTrue(driver.getId()).orElseThrow(() -> new CarNotFound(CAR_NOT_FOUND))));
        List<Seat> driverCarSeatList = activeCars.get(0).getSeatList();
        int countActiveSeat = 0;
        for (Seat seat : driverCarSeatList) {
            if (seat.isActive()) {
                countActiveSeat++;
            }
        }
        if (countActiveSeat < announcementPassenger.getPassengersList().size()) {
            throw new NotEnoughSeat(NOT_ENOUGH_SEAT);
        }
        for (Seat seat : driverCarSeatList) {
            if (acceptDriverRequestDto.getSeatIdList().contains(seat.getId())) {
                seat.setActive(false);
                seatRepository.save(seat);
                countActiveSeat--;
                break;
            }
        }
        if (countActiveSeat == 0) {
            announcementDriver.setActive(false);
        }

        announcementPassenger.setActive(false);
        fromDriverToUser.setReceived(true);
        fromDriverToUser.setActive(false);
        notificationRepository.save(fromDriverToUser);
        announcementPassengerRepository.save(announcementPassenger);

        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.afterAgreeRequestForDriver(fromDriverToUser.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);
        return new ApiResponse(YOU_ACCEPTED_REQUEST, true);
    }

    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = {NotEnoughSeat.class, CarNotFound.class, UserNotFoundException.class, AnnouncementNotFoundException.class, RecordNotFoundException.class})
    public ApiResponse acceptPassengerRequest(UUID userId) {
        User driver = userService.checkUserExistByContext();
        User passenger = userService.checkUserExistById(userId);
        Notification fromUserToDriver = getNotification(driver, passenger);
        AnnouncementDriver announcementDriver = getAnnouncementDriver(fromUserToDriver);
        AnnouncementPassenger announcementPassenger = getAnnouncementPassenger(passenger);

        List<Car> activeCars = new ArrayList<>();
        driver.getCars().forEach(car -> activeCars.add(carRepository.findByUserIdAndActiveTrue(driver.getId()).orElseThrow(() -> new CarNotFound(CAR_NOT_FOUND))));
        Car car = activeCars.get(0);

        List<Seat> driverCarSeatList = seatRepository.findAllByCarIdAndActive(car.getId(), true);
        int countActiveSeat = driverCarSeatList.size();

        if (countActiveSeat < announcementPassenger.getPassengersList().size()) {
            throw new NotEnoughSeat(NOT_ENOUGH_SEAT);
        }
        for (Seat seat : fromUserToDriver.getCarSeats()) {
            if (driverCarSeatList.contains(seat)) {
                seat.setActive(false);
                seatRepository.save(seat);
                countActiveSeat--;
            } else {
                List<Seat> activeSeats = seatRepository.findAllByCarIdAndActive(car.getId(), true);
                Notification notification = reCreateNotification(passenger.getId(), announcementDriver.getId(), driver.getId(), announcementPassenger.getId(), activeSeats, passenger.getFireBaseToken());
                notificationRepository.save(notification);

                HashMap<String, String> data = new HashMap<>();
                data.put("announcementId", announcementDriver.getId().toString());
                NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.reCreate(passenger.getFireBaseToken(), data);
                fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

                return new ApiResponse(HttpStatus.CREATED, true);
            }
        }
        if (countActiveSeat == 0) {
            announcementDriver.setActive(false);
        }
        announcementPassenger.setActive(false);
        fromUserToDriver.setReceived(true);
        fromUserToDriver.setActive(false);
        notificationRepository.save(fromUserToDriver);
        announcementDriverRepository.save(announcementDriver);
        announcementPassengerRepository.save(announcementPassenger);

        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.afterAgreeRequestForPassenger(fromUserToDriver.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);
        return new ApiResponse(YOU_ACCEPTED_REQUEST, true);
    }


    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAcceptedNotificationForDriver() {
        User receiver = userService.checkUserExistByContext();

        List<Notification> passengerAccepted = notificationRepository.findAllBySenderIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.PASSENGER);
        List<Notification> driverAccepted = notificationRepository.findAllByReceiverIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.DRIVER);
        List<AllowedAnnouncementResponseForDriver> allowedAnnouncementResponseForDrivers = new ArrayList<>();

        passengerAccepted.forEach(obj ->
                allowedAnnouncementResponseForDrivers.add(
                        AllowedAnnouncementResponseForDriver.fromForDriver(
                                userService.checkUserExistById(obj.getReceiverId()), obj,
                                announcementPassengerRepository.findById(obj.getAnnouncementPassengerId())
                                        .orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND)))));

        driverAccepted.forEach(obj ->
                allowedAnnouncementResponseForDrivers.add(
                        AllowedAnnouncementResponseForDriver.fromForDriver(
                                userService.checkUserExistById(obj.getSenderId()), obj,
                                announcementPassengerRepository.findById(obj.getAnnouncementPassengerId())
                                        .orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND)))));
        return new ApiResponse(allowedAnnouncementResponseForDrivers, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAcceptedNotificationForPassenger() {
        User receiver = userService.checkUserExistByContext();
        List<AllowedAnnouncementResponsePassenger> allowedAnnouncementResponsePassengers = new ArrayList<>();
        List<Notification> driverAccepted = notificationRepository.findAllBySenderIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.DRIVER);
        List<Notification> passengerAccepted = notificationRepository.findAllByReceiverIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.PASSENGER);

        driverAccepted.forEach(obj ->
                allowedAnnouncementResponsePassengers.add(
                        AllowedAnnouncementResponsePassenger.fromForPassenger(userService.checkUserExistById(obj.getReceiverId()), obj)));

        passengerAccepted.forEach(obj ->
                allowedAnnouncementResponsePassengers.add(
                        AllowedAnnouncementResponsePassenger.fromForPassenger(userService.checkUserExistById(obj.getSenderId()), obj)));
        return new ApiResponse(allowedAnnouncementResponsePassengers, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse changeToRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));
        notification.setRead(true);
        notificationRepository.save(notification);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    private Notification getNotification(User user1, User user2) {
        return notificationRepository.findFirstBySenderIdAndReceiverIdAndActiveAndReceivedOrderByCreatedTimeDesc(user2.getId(), user1.getId(), true, false)
                .orElseThrow(() -> new RecordNotFoundException(NOTIFICATION_NOT_FOUND));
    }

    private AnnouncementDriver getAnnouncementDriver(Notification fromUserToDriver) {
        return announcementDriverRepository
                .findByIdAndActive(fromUserToDriver.getAnnouncementDriverId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }

    private AnnouncementDriver getAnnouncementDriver(User driver) {
        return announcementDriverRepository.findByUserIdAndActive(driver.getId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }

    private AnnouncementPassenger getAnnouncementPassenger(Notification fromDriverToUser) {
        return announcementPassengerRepository.findByIdAndActive(fromDriverToUser.getAnnouncementPassengerId(), true)
                .orElseThrow(() -> new AnnouncementNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }

    private AnnouncementPassenger getAnnouncementPassenger(User passenger) {
        return announcementPassengerRepository
                .findByUserIdAndActive(passenger.getId(), true)
                .orElseThrow(() -> new RecordNotFoundException(ANNOUNCEMENT_NOT_FOUND));
    }

    private Notification from(NotificationRequestDto notificationRequestDto, User user) {
        Notification notification = Notification.from(notificationRequestDto);
        notification.setSenderId(user.getId());
        notification.setUser(user);
        User receiver = userService.checkUserExistById(notificationRequestDto.getReceiverId());
        notification.setReceiverToken(receiver.getFireBaseToken());
        if (notificationRequestDto.getSeatIdList() != null) {
            List<Seat> selectedSeats = seatRepository.findAllByIdIn(notificationRequestDto.getSeatIdList());
            notification.setCarSeats(selectedSeats);
        }
        return notificationRepository.save(notification);
    }

    private Notification reCreateNotification(UUID receiverId, UUID announcementDriverId, UUID announcementPassengerId, UUID senderId, List<Seat> seatList, String receiverToken) {
        return Notification.builder()
                .receiverToken(receiverToken)
                .senderId(senderId)
                .receiverId(receiverId)
                .announcementDriverId(announcementDriverId)
                .announcementPassengerId(announcementPassengerId)
                .carSeats(seatList)
                .notificationType(NotificationType.PASSENGER)
                .build();
    }

}