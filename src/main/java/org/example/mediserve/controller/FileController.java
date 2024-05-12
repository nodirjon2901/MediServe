package org.example.mediserve.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.dto.response.FileResponseDTO;
import org.example.mediserve.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class FileController {

    private final FileService fileService;

    @Operation(
            description = "This API is used to upload a file",
            method = "POST method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN", "USER"})
    )
    @PostMapping("/upload/{ownerId}")
    public ResponseEntity<FileResponseDTO> uploadFile(
            @RequestParam MultipartFile file,
            @PathVariable UUID ownerId
    ) {
        return ResponseEntity.ok(fileService.saveFile(file, ownerId));
    }

    @Operation(
            description = "This API is used to download a file",
            method = "POST method is supported",
            security = @SecurityRequirement(name = "open", scopes = {"ADMIN", "USER"})
    )
    @PostMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Path file = fileService.downloadFile(fileName);
        try {
            String mediaType = Files.probeContentType(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .body(new UrlResource(file.toUri()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
