package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(Objects.isNull(userDto)) {
            throw new BadRequestException(String.format("[%s] : UserDto is null",UserServiceImpl.class));
        }

        return userMapper.userEntityToUserDto(
                userRepository.save(userMapper.userDtoToUserEntity(userDto))
        );
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        if(Objects.isNull(userDto) || Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : UserDto or id are null",UserServiceImpl.class));
        }

        userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User was not found with id - %d", id)));

        return userMapper.userEntityToUserDto(
                userRepository.update(id, userMapper.userDtoToUserEntity(userDto))
        );
    }

    @Override
    public UserDto getUserById(Long id) {
        if(Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : Id is null",UserServiceImpl.class));
        }
        return userMapper.userEntityToUserDto(
                userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User was not found with id - %d", id)))
        );
    }

    @Override
    public void deleteUserById(Long id) {
        if(Objects.isNull(id)) {
            throw new BadRequestException(String.format("[%s] : Id is null",UserServiceImpl.class));
        }
        userRepository.delete(id);
    }
}
