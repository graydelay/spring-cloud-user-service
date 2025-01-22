package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final Environment env;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder encoder,
                           Environment env,
                           RestTemplate restTemplate,
                           OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /* Using as RestTemplate */
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
//                                    new ParameterizedTypeReference<List<ResponseOrder>>() {
//                });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        /* Using a FeignClient */
        /* Feign Exception handling */
//        List<ResponseOrder> ordersList = null;
//        try {
//            ordersList = orderServiceClient.getOrders(userId);
//        } catch (FeignException e) {
//            log.error(e.getMessage());
//        }
        /* ErrorDecoder */
        List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);
        userDto.setOrders(ordersList);

        return userDto;
    }

    @Override
    public Page<UserEntity> getUserByAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userDto.getName() != null) {
            userEntity.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userEntity.setEmail(userDto.getEmail());
        }
        if (userDto.getPwd() != null) {
            userEntity.setEncryptedPwd(encoder.encode(userDto.getPwd()));
        }

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(userEntity);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return mapper.map(userEntity, UserDto.class);
    }
}
