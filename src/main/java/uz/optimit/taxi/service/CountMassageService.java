package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uz.optimit.taxi.entity.CountMassage;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.model.request.CountMassageRequest;
import uz.optimit.taxi.model.response.CountResponse;
import uz.optimit.taxi.repository.CountMassageRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CountMassageService {

    private final CountMassageRepository countMassageRepository;
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAllMassagesCount(){
      return new ApiResponse(countMassageRepository.countAllByCount(1),true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAllMassagesCountByDate(CountMassageRequest countMassageRequest){
        Integer counted = countMassageRepository.countAllBySandedTimeBetween(countMassageRequest.getTime1(), countMassageRequest.getTime2());
        return new ApiResponse(counted,true);
    }
//    public ApiResponse getAllMassagesCountByPhoneNumbers(){
//        List<CountResponse> countMassages = countMassageRepository.countCountMassage();
//        return new ApiResponse(countMassages,true);
//    }
}
