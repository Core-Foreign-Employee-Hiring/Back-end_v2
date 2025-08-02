package com.forwork.backend.api.pass_archive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PassArchiveCreateRequestDTO {
    private String title;
    private String oneLineReview;
    private String description;
    private long price;
}