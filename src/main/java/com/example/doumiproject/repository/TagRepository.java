package com.example.doumiproject.repository;

import com.example.doumiproject.dto.TagDto;
import com.example.doumiproject.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public interface TagRepository {
    public List<TagDto> findAll();
    public void saveTags(String[] quiz, long postId);
    public void deleteTags(long postId);


    default RowMapper<TagDto> TagRowMapper(){
        return (rs, rowNum)->{
            String[] nameStrings=rs.getString("names").split(",");
            String[] idStrings=rs.getString("ids").split(",");
            List<Tag> tags=new ArrayList<>();
            for(int i=0;i<nameStrings.length;i++){
                Tag tag = Tag.builder()
                        .name(nameStrings[i])
                        .id(Long.parseLong(idStrings[i])).build();
                tags.add(tag);
            }


            TagDto tagDto = TagDto.builder()
                    .type(rs.getString("type"))
                    .detailTags(tags).build();

            return tagDto;
        };
    }
}
