package org.example.mediserve.service;

import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.dto.request.UserCreateDTO;
import org.example.mediserve.domain.dto.request.UserLoginDTO;
import org.example.mediserve.domain.dto.response.UserResponseDTO;
import org.example.mediserve.domain.entity.UserEntity;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.domain.enums.UserState;
import org.example.mediserve.exception.AlreadyExistsException;
import org.example.mediserve.exception.DataNotFoundException;
import org.example.mediserve.exception.WrongPasswordException;
import org.example.mediserve.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserResponseDTO, UserCreateDTO> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    public void signUp(UserCreateDTO userCreateDTO) {
        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new AlreadyExistsException("User is already exist with this username");
        }
        UserEntity userEntity = modelMapper.map(userCreateDTO, UserEntity.class);
        userEntity.setRole(UserRole.GUEST);
        userEntity.setIsPaid(false);
        userEntity.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userRepository.save(userEntity);
    }

    public UserResponseDTO signIn(UserLoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findByUsername(loginDTO.username()).orElseThrow(
                () -> new DataNotFoundException("User is not found with this username")
        );
        if (passwordEncoder.matches(loginDTO.password(), userEntity.getPassword())) {
            return modelMapper.map(userEntity, UserResponseDTO.class);
        }
        throw new WrongPasswordException("Password is wrong! Please try again");
    }

    @Override
    public void save(UserCreateDTO userCreateDTO) {
        userRepository.save(modelMapper.map(userCreateDTO, UserEntity.class));
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public void deleteByChatId(Long chatId) {
        userRepository.deleteUserByChatId(chatId);
    }

    @Override
    public UserResponseDTO update(UserCreateDTO userCreateDTO, UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User is not found with this id")
        );
        userEntity.setFirstName(userCreateDTO.getFirstName());
        userEntity.setLastName(userCreateDTO.getLastName());
        userEntity.setUsername(userCreateDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userEntity.setExperience(userCreateDTO.getExperience());
        userEntity.setPhoneNumber(userCreateDTO.getPhoneNumber());
        return modelMapper.map(userRepository.save(userEntity), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO findById(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User is not found with this id"));
        return modelMapper.map(userEntity, UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userEntity -> {
                    return modelMapper.map(userEntity, UserResponseDTO.class);
                }).toList();
    }

    public Optional<UserEntity> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public void updateState(Long chatId, UserState userState) {
        UserEntity userEntity = userRepository.findByChatId(chatId).orElseThrow(() -> new DataNotFoundException("User is not found with this chatId"));
        userEntity.setState(userState);
        userRepository.save(userEntity);
    }

    public void updateFirstname(Long chatId, String firstName) {
        userRepository.updateFirstName(firstName, chatId);
    }

    public int updateUsername(Long chatId, String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent()) {
            return 500;
        }
        userRepository.updateUserName(username, chatId);
        return 200;
    }

    public void updatePassword(Long chatId, String password) {
        String encodePassword = passwordEncoder.encode(password);
        userRepository.updatePassword(encodePassword, chatId);
    }

    public void updateLastname(Long chatId, String lastName) {
        userRepository.updateLastName(lastName, chatId);
    }

    public void updateExperience(Long chatId, String experience) {
        userRepository.updateExperience(experience, chatId);
    }

    public UserResponseDTO changeRole(UUID id, UserRole role) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("User is not found with this id")
        );
        userEntity.setRole(role);
        return modelMapper.map(userRepository.save(userEntity), UserResponseDTO.class);
    }

}
