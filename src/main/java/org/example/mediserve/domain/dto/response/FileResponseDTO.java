package org.example.mediserve.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileResponseDTO {

    protected UUID id;

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

    protected LocalDateTime createdDate;

    protected LocalDateTime updatedDate;

}
