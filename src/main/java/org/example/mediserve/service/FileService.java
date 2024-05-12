package org.example.mediserve.service;

import org.example.mediserve.domain.dto.response.FileResponseDTO;
import org.example.mediserve.domain.entity.FileEntity;
import org.example.mediserve.exception.DataNotFoundException;
import org.example.mediserve.repository.FileRepository;
import org.example.mediserve.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public FileService(@Value("${file.upload-dir}") String fileUploadDir) {
        this.fileLocation = Paths.get(fileUploadDir)
                .toAbsolutePath().normalize();
    }

    public FileResponseDTO saveFile(MultipartFile file, UUID ownerId) {
        String fullFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Path targetLocation = fileLocation.resolve(fullFileName);
            Files.copy(file.getInputStream(), targetLocation);
        } catch (FileAlreadyExistsException e) {
            String[] fileNameAndType = fullFileName.split("\\.");
            fullFileName = fileNameAndType[0] + System.currentTimeMillis() + "." + fileNameAndType[1];
            Path targetLocation = fileLocation.resolve(fullFileName);
            try {
                Files.copy(file.getInputStream(), targetLocation);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileEntity fileEntity = FileEntity.builder()
                .fileName(fullFileName)
                .fileType(file.getContentType())
                .fileDownloadUri(fileLocation + file.getOriginalFilename())
                .size(file.getSize())
                .user(userRepository.findById(ownerId).orElseThrow(()->new DataNotFoundException(
                        "User is not found with this id"
                )))
                .build();
        return modelMapper.map(fileRepository.save(fileEntity), FileResponseDTO.class);
    }

    public Path downloadFile(String fileName) {
        FileEntity fileEntity = fileRepository.findByFileName(fileName).orElseThrow(() -> new DataNotFoundException("File is not found"));
        return fileLocation.resolve(fileEntity.getFileName());
    }

}
