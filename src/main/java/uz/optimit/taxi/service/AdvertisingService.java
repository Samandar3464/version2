package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Advertising;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.model.request.AdvertisingRequestDto;
import uz.optimit.taxi.repository.AdvertisingRepository;

import java.util.UUID;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AdvertisingService {

    private final AdvertisingRepository advertisingRepository;

    private final AttachmentService attachmentService;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse add(AdvertisingRequestDto advertisingRequestDto) {
        Attachment attachment = attachmentService.saveToSystem(advertisingRequestDto.getContent());
        Advertising advertising = Advertising.from(advertisingRequestDto);
        advertising.setContent(attachment);
        advertisingRepository.save(advertising);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deActivate(UUID id) {
        Advertising advertising = advertisingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(ADVERTISING_NOT_FOUND));
        advertising.setActive(false);
        advertisingRepository.save(advertising);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleted(UUID id) {
        advertisingRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(ADVERTISING_NOT_FOUND));
        advertisingRepository.deleteById(id);
        return new ApiResponse(DELETED, true);
    }
}
