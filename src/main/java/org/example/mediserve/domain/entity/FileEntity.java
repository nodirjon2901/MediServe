package org.example.mediserve.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileEntity extends BaseEntity {

    private String fileName;

    private String fileDownloadUri;

    private String fileType;

    private long size;

    @OneToOne
    @JsonIgnore
    private UserEntity user;

}
