package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.AutoModel;
import uz.optimit.taxi.entity.Car;
import uz.optimit.taxi.entity.User;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.CarNotFound;
import uz.optimit.taxi.model.request.CarRegisterRequestDto;
import uz.optimit.taxi.model.response.CarResponseDto;
import uz.optimit.taxi.model.response.SeatResponse;
import uz.optimit.taxi.repository.AutoModelRepository;
import uz.optimit.taxi.repository.CarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final AttachmentService attachmentService;

    private final AutoModelRepository autoModelRepository;

    private final UserService userService;

    private final SeatService seatService;

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse addCar(CarRegisterRequestDto carRegisterRequestDto) {
        User user = userService.checkUserExistByContext();
        from(carRegisterRequestDto, user);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse disActiveCarList() {
        List<Car> allByActive = carRepository.findAllByActiveFalse();
        List<CarResponseDto> carResponseDtoList = new ArrayList<>();
        allByActive.forEach(car -> carResponseDtoList.add(CarResponseDto.from(car, attachmentService.attachUploadFolder)));
        return new ApiResponse(carResponseDtoList, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getCarById(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFound(CAR_NOT_FOUND));
        return new ApiResponse(CarResponseDto.from(car, attachmentService.attachUploadFolder), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getCar() {
        User user = userService.checkUserExistByContext();
        Car car = getCarByUserId(user.getId());
        return new ApiResponse(CarResponseDto.from(car, attachmentService.attachUploadFolder), true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse activateCar(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFound(CAR_NOT_FOUND));
        car.setActive(true);
        carRepository.save(car);
        userService.addRoleDriver(List.of(car));
        return new ApiResponse(CAR_ACTIVATED, true);
    }

    public ApiResponse getCarSeat() {
        User user = userService.checkUserExistByContext();
        Car car = getCarByUserId(user.getId());
        List<SeatResponse> seatResponses = new ArrayList<>();
        car.getSeatList().forEach(seat -> seatResponses.add(SeatResponse.from(seat)));
        return new ApiResponse(seatResponses, true);
    }

    private Car from(CarRegisterRequestDto carRegisterRequestDto, User user) {
        AutoModel autoModel1 = autoModelRepository.getByIdAndAutoCategoryId(carRegisterRequestDto.getAutoModelId(), carRegisterRequestDto.getAutoCategoryId());

        Car car = Car.from(carRegisterRequestDto);
        car.setAutoModel(autoModel1);
        car.setPhotoDriverLicense(attachmentService.saveToSystem(carRegisterRequestDto.getPhotoDriverLicense()));
        car.setTexPassportPhoto(attachmentService.saveToSystem(carRegisterRequestDto.getTexPassportPhoto()));
        car.setAutoPhotos(attachmentService.saveToSystemListFile(carRegisterRequestDto.getAutoPhotos()));
        car.setUser(user);
        Car save = carRepository.save(car);
        if (carRegisterRequestDto.getCountSeat() == 0) {
            seatService.createCarSeats(autoModel1.getCountSeat(), save);
        } else {
            seatService.createCarSeats(carRegisterRequestDto.getCountSeat(), save);
        }
        return save;
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteCarByID(UUID id) {
        Car byId = carRepository.findById(id).orElseThrow(()->new CarNotFound(CAR_NOT_FOUND));
        byId.setActive(false);
        carRepository.save(byId);
        return new ApiResponse(DELETED, true);
    }
    public Car getCarByUserId(UUID user_id){
        return carRepository.findByUserIdAndActiveTrue(user_id).orElseThrow(() ->
                new CarNotFound(CAR_NOT_FOUND));
    }

}
