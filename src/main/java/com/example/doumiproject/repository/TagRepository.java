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
            TagDto tagDto = new TagDto();
            tagDto.setType(rs.getString("type"));
            String[] nameStrings=rs.getString("names").split(",");
            String[] idStrings=rs.getString("ids").split(",");
            List<Tag> tags=new ArrayList<>();
            for(int i=0;i<nameStrings.length;i++){
                Tag tag = new Tag();
                tag.setName(nameStrings[i]);
                tag.setId(Long.parseLong(idStrings[i]));
                tags.add(tag);
            }
            tagDto.setDetailTags(tags);
            return tagDto;
        };
    }
}
