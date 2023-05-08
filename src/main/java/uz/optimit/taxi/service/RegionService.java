package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.Region;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.RecordAlreadyExistException;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.model.request.RegionRegisterRequestDto;
import uz.optimit.taxi.repository.RegionRepository;

import java.util.List;
import java.util.Optional;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse addRegion(RegionRegisterRequestDto regionRegisterRequestDto) {
        if (regionRepository.existsByName(regionRegisterRequestDto.getName())) {
            throw new RecordAlreadyExistException(REGION_ALREADY_EXIST);
        }
        regionRepository.save(Region.from(regionRegisterRequestDto));
        return new ApiResponse(SUCCESSFULLY , true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getRegionList(){
        return new ApiResponse(regionRepository.findAll(),true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getRegionById(Integer id){
        return new ApiResponse(regionRepository.findById(id).orElseThrow(()->new RecordNotFoundException(REGION_NOT_FOUND)),true);
    }

    public ApiResponse deleteRegionById(Integer id) {
        regionRepository.deleteById(id);
        return new ApiResponse(DELETED,true);
    }
}
