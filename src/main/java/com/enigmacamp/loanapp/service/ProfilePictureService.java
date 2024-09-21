package com.enigmacamp.loanapp.service;

import com.enigmacamp.loanapp.entity.ProfilePicture;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProfilePictureService {
    ProfilePicture createProfilePicture(MultipartFile multipartFile);
    Resource getImageByPath(String path);
    void deleteProfilePictureByPath(String path);
}
