package ru.fit.fitlyfe.services;

import ru.fit.fitlyfe.models.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    Optional<UserProfile> getOneUser(Long id);

    List<UserProfile> getAllUsers();

    UserProfile createUser(UserProfile userProfile);

    void deleteUser(Long id);

    Optional<UserProfile> patchUser(Long id, UserProfile userProfile);
}
