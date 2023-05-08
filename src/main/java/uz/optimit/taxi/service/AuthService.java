package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.optimit.taxi.exception.UserNotFoundException;
import uz.optimit.taxi.repository.UserRepository;

import static uz.optimit.taxi.entity.Enum.Constants.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhone(phoneNumber).orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
    }
}
