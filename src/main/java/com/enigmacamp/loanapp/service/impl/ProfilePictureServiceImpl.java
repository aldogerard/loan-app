package com.enigmacamp.loanapp.service.impl;

import com.enigmacamp.loanapp.constant.strings.Message;
import com.enigmacamp.loanapp.entity.ProfilePicture;
import com.enigmacamp.loanapp.repository.ProfilePictureRepository;
import com.enigmacamp.loanapp.service.ProfilePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilePictureServiceImpl implements ProfilePictureService {

    private final ProfilePictureRepository profilePictureRepository;


    @Override
    public ProfilePicture createProfilePicture(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Message.NO_FILE_UPLOAD);

        try {
            String path = "/loan_app/assets/images";
            Path directoryPath = Paths.get(path);
            Files.createDirectories(directoryPath);
            String filename = String.format("%d_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
            Path filePath = directoryPath.resolve(filename);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            ProfilePicture profilePicture = ProfilePicture.builder()
                    .name(filename)
                    .url("/api/customer/" + LocalDateTime.now() + "/image")
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .path(filePath.toString())
                    .build();

            profilePictureRepository.saveAndFlush(profilePicture);
            return profilePicture;


        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Resource getImageByPath(String path) {
        try {
            Path directoryPath = Paths.get(path);
            return new UrlResource(directoryPath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void deleteProfilePictureByPath(String path) {
        try {
            Path directoryPath = Paths.get(path);
            Files.deleteIfExists(directoryPath);

            ProfilePicture menuImage = getProfilePictureByPath(path);
            profilePictureRepository.deleteById(menuImage.getId());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ProfilePicture getProfilePictureByPath(String path) {
        Optional<ProfilePicture> profilePicture = profilePictureRepository.findByPath(path);
        return profilePicture.orElse(null);
    }

}
