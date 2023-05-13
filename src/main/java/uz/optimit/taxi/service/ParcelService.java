package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.PassengerParcel;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.model.request.GetByFilter;
import uz.optimit.taxi.model.request.ParcelRegisterRequestDto;
import uz.optimit.taxi.model.response.ParcelResponse;
import uz.optimit.taxi.model.response.ParcelResponseForList;
import uz.optimit.taxi.model.response.UserResponseDto;
import uz.optimit.taxi.repository.CityRepository;
import uz.optimit.taxi.repository.ParcelRepository;
import uz.optimit.taxi.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(ParcelRegisterRequestDto parcelRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        PassengerParcel passengerParcel = fromRequest(parcelRegisterRequestDto, user);
        parcelRepository.save(passengerParcel);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getParcelList() {
        List<ParcelResponseForList> parcelResponses = new ArrayList<>();
        parcelRepository.findAllByActiveTrueAndDeletedFalse().forEach(obj ->
                parcelResponses.add(ParcelResponseForList.from(obj)));
        return new ApiResponse(parcelResponses, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getParcelByIdAndActive(UUID id) {
        PassengerParcel active = parcelRepository.getByIdAndActiveTrueAndDeletedFalse(id).orElseThrow(() -> new RecordNotFoundException(PARCEL_NOT_FOUND));
        User user = userService.checkUserExistById(active.getUser().getId());
        UserResponseDto userResponseDto = userService.fromUserToResponse(user);
        ParcelResponse parcelResponse = ParcelResponse.from(active, userResponseDto);
        return new ApiResponse(parcelResponse, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getParcelById(UUID id) {
        PassengerParcel active = parcelRepository.getByIdAndDeletedFalse(id).orElseThrow(() -> new RecordNotFoundException(PARCEL_NOT_FOUND));
        User user = userService.checkUserExistById(active.getUser().getId());
        UserResponseDto userResponseDto = userService.fromUserToResponse(user);
        ParcelResponse parcelResponse = ParcelResponse.from(active, userResponseDto);
        return new ApiResponse(parcelResponse, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteParcel(UUID id) {
        PassengerParcel passengerParcel = parcelRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(PARCEL_NOT_FOUND));
        passengerParcel.setDeleted(true);
        parcelRepository.save(passengerParcel);
        return new ApiResponse(DELETED, true);
    }

    public ApiResponse getHistory() {
        User user = userService.checkUserExistByContext();
        List<PassengerParcel> allByActive = getByUserId(user, false);
        List<ParcelResponse> response = new ArrayList<>();
        UserResponseDto userResponseDto = userService.fromUserToResponse(userService.checkUserExistById(user.getId()));
        allByActive.forEach(announcementPassenger -> response.add(ParcelResponse.from(announcementPassenger, userResponseDto)));
        return new ApiResponse(response, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getByFilter(GetByFilter getByFilter) {
        List<PassengerParcel> byFilter = parcelRepository
                .findAllByActiveTrueAndFromRegionIdAndToRegionIdAndDeletedFalseAndTimeToSendBetweenOrderByCreatedTimeDesc(
                        getByFilter.getFromRegionId(),
                        getByFilter.getToRegionId(),
                        getByFilter.getTime1(),
                        getByFilter.getTime2());
        List<ParcelResponseForList> parcelResponses = new ArrayList<>();
        byFilter.forEach(obj -> parcelResponses.add(ParcelResponseForList.from(obj)));
        return new ApiResponse(parcelResponses, true);
    }

    public List<PassengerParcel> getByUserId(User user, boolean active) {
        return parcelRepository.findAllByUserIdAndActiveAndDeletedFalse(user.getId(), active);
    }

    public PassengerParcel getById(UUID parcelId, boolean active) {
        return parcelRepository.findByIdAndActiveAndDeletedFalse(parcelId, active).orElseThrow(() -> new RecordNotFoundException(PARCEL_NOT_FOUND));
    }

    private PassengerParcel fromRequest(ParcelRegisterRequestDto parcelRegisterRequestDto, User user) {
        PassengerParcel passengerParcel = PassengerParcel.from(parcelRegisterRequestDto);
        passengerParcel.setUser(user);
        passengerParcel.setFromRegion(regionRepository.getById(parcelRegisterRequestDto.getFromRegionId()));
        passengerParcel.setToRegion(regionRepository.getById(parcelRegisterRequestDto.getToRegionId()));
        passengerParcel.setFromCity(cityRepository.getById(parcelRegisterRequestDto.getFromCityId()));
        passengerParcel.setToCity(cityRepository.getById(parcelRegisterRequestDto.getToCityId()));
        return passengerParcel;
    }

    public ApiResponse getPassengerParcels() {
        User user = userService.checkUserExistByContext();
        List<ParcelResponseForList> parcelResponses = new ArrayList<>();
        parcelRepository.findAllByUserIdAndActiveAndDeletedFalse(user.getId(), true).forEach(obj ->
                parcelResponses.add(ParcelResponseForList.from(obj)));
        return new ApiResponse(parcelResponses, true);
    }
}
