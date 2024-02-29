package com.example.doumiproject.repository;


import com.example.doumiproject.dto.TagDetailDto;
import com.example.doumiproject.dto.TagDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public interface TagRepository {
    List<TagDto> findAll();


    default RowMapper<TagDto> TagRowMapper(){
        return (rs, rowNum)->{
            TagDto tagDto = new TagDto();
            tagDto.setType(rs.getString("type"));
            String[] nameStrings=rs.getString("names").split(",");
            String[] idStrings=rs.getString("ids").split(",");
            List<TagDetailDto> tagDetailDtos=new ArrayList<>();
            for(int i=0;i<nameStrings.length;i++){
                TagDetailDto tagDetailDto = new TagDetailDto();
                tagDetailDto.setName(nameStrings[i]);
                tagDetailDto.setId(Long.parseLong(idStrings[i]));
                tagDetailDtos.add(tagDetailDto);
            }
            tagDto.setDetailTags(tagDetailDtos);
            return tagDto;
        };
    }
}
