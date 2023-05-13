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
import uz.optimit.taxi.model.request.NotificationRequestDto;
import uz.optimit.taxi.model.response.*;
import uz.optimit.taxi.repository.AnnouncementDriverRepository;
import uz.optimit.taxi.repository.CarRepository;
import uz.optimit.taxi.repository.NotificationRepository;
import uz.optimit.taxi.repository.SeatRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;


@Service
@RequiredArgsConstructor
public class ParcelNotificationService {

    private final NotificationRepository notificationRepository;
    private final AnnouncementDriverRepository announcementDriverRepository;
    private final AnnouncementDriverService announcementDriverService;
    private final FireBaseMessagingService fireBaseMessagingService;
    private final UserService userService;
    private final CarRepository carRepository;
    private final ParcelService parcelService;
    private final SeatRepository seatRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createNotificationForDriver(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        announcementDriverService.getByIdAndActiveAndDeletedFalse(notificationRequestDto.getAnnouncementDriverId(), true);
        PassengerParcel passengerParcel = parcelService.getById(notificationRequestDto.getParcelId(), true);

        HashMap<String, String> data = new HashMap<>();
        data.put("announcementId", passengerParcel.getId().toString());
        notificationRequestDto.setDate(data);
        notificationRequestDto.setAnnouncementPassengerId(passengerParcel.getId());
        notificationRequestDto.setTitle(YOU_COME_TO_MESSAGE_FROM_PASSENGER_FOR_PARCEL);
        notificationRequestDto.setNotificationType(NotificationType.DRIVER);
        Notification notification = from(notificationRequestDto, user,passengerParcel);
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForDriver(notificationRequestDto, notification.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

        return new ApiResponse(SUCCESSFULLY, true);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createNotificationForPassenger(NotificationRequestDto notificationRequestDto) {
        User user = userService.checkUserExistByContext();
        PassengerParcel passengerParcel = parcelService.getById(notificationRequestDto.getParcelId(), true);
        List<AnnouncementDriver> byUserIdAndActive = announcementDriverService.getByUserIdAndActiveAndParcelAndDeletedFalse(user.getId(), true);
        if (byUserIdAndActive.isEmpty()) {
            throw new AnnouncementNotFoundException(YOU_COME_TO_MESSAGE_FROM_DRIVER_FOR_PARCEL);
        }
        AnnouncementDriver announcementDriver = byUserIdAndActive.get(0);
        notificationRequestDto.setAnnouncementDriverId(announcementDriver.getId());

        HashMap<String, String> data = new HashMap<>();
        data.put("announcementId", announcementDriver.getId().toString());
        notificationRequestDto.setDate(data);

        notificationRequestDto.setTitle(YOU_COME_TO_MESSAGE_FROM_DRIVER);
        notificationRequestDto.setNotificationType(NotificationType.PASSENGER);
        Notification notification = from(notificationRequestDto, user,passengerParcel);
        NotificationMessageResponse notificationMessageResponse = NotificationMessageResponse.fromForPassenger(notificationRequestDto, notification.getReceiverToken());
        fireBaseMessagingService.sendNotificationByToken(notificationMessageResponse);

        return new ApiResponse(SUCCESSFULLY, true);
    }



//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse getAcceptedNotificationForDriver() {
//        User receiver = userService.checkUserExistByContext();
//
//        List<Notification> passengerAccepted = notificationRepository.findAllBySenderIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.PASSENGER);
//        List<Notification> driverAccepted = notificationRepository.findAllByReceiverIdAndReceivedTrueAndNotificationTypeOrderByCreatedTimeDesc(receiver.getId(), NotificationType.DRIVER);
//        List<AllowedAnnouncementResponseForDriver> allowedAnnouncementResponseForDrivers = new ArrayList<>();
//
//        passengerAccepted.forEach(obj ->
//                allowedAnnouncementResponseForDrivers.add(
//                        AllowedAnnouncementResponseForDriver.fromForDriver(
//                                userService.checkUserExistById(obj.getReceiverId()), obj,
//                                announcementPassengerRepository.findById(obj.getAnnouncementPassengerId())
//                                        .orElseThrow(() -> new AnnouncementNotFoundException(PASSENGER_ANNOUNCEMENT_NOT_FOUND)))));
//
//        driverAccepted.forEach(obj ->
//                allowedAnnouncementResponseForDrivers.add(
//                        AllowedAnnouncementResponseForDriver.fromForDriver(
//                                userService.checkUserExistById(obj.getSenderId()), obj,
//                                announcementPassengerRepository.findById(obj.getAnnouncementPassengerId())
//                                        .orElseThrow(() -> new AnnouncementNotFoundException(DRIVER_ANNOUNCEMENT_NOT_FOUND)))));
//        return new ApiResponse(allowedAnnouncementResponseForDrivers, true);
//    }



    private Notification from(NotificationRequestDto notificationRequestDto, User user, PassengerParcel passengerParcel) {
        Notification notification = Notification.from(notificationRequestDto);
        notification.setSenderId(user.getId());
        notification.setUser(user);
        notification.setPassengerParcels(passengerParcel);
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