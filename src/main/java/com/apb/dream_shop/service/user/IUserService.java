package com.apb.dream_shop.service.user;

import com.apb.dream_shop.dto.UserDTO;
import com.apb.dream_shop.modal.User;
import com.apb.dream_shop.request.CreateUserRequest;
import com.apb.dream_shop.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

    UserDTO convertUserToDto(User user);
}
