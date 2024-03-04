package com.example.doumiproject.dto;

import com.example.doumiproject.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TagDto {
    String type;
    List<Tag> detailTags;
}
