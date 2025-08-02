package com.forwork.backend.api.pass_archive.dto;

import com.forwork.backend.api.file.entity.UploadFile;
import com.forwork.backend.api.pass_archive.entity.PassArchive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PassArchiveDetailResponseDTO {
    private String title;
    private String oneLineReview;
    private double star;
    private long starCount;
    private String thumbnailUrl;
    private long price;
    private String description;
    private List<String> imageUrls;
    private String authorNickname;
    private String authorProfileImage;

    public static PassArchiveDetailResponseDTO from(PassArchive pa) {
        return new PassArchiveDetailResponseDTO(
                pa.getTitle(),
                pa.getOneLineReview(),
                pa.getStar(),
                pa.getStarCount(),
                pa.getThumbnail().getFileUrl(),
                pa.getPrice(),
                pa.getDescription(),
                pa.getImages().stream()
                        .map(UploadFile::getFileUrl)
                        .collect(Collectors.toList()),
                pa.getMember().getName(),            // name을 닉네임으로 사용
                pa.getMember().getProfileImage()     // Member.profileImage
        );
    }
}