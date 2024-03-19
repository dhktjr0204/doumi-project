package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CodingTestDto;
import com.example.doumiproject.dto.CodingTestRequestDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class JdbcTemplateCodingTestRepository implements CodingTestRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCodingTestRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<CodingTestDto> findByCodingTestId(long post_id, long user_id) {
        //post의 user_id(squence값)과 user의 user_id(nickname용)이 아주 헷갈린다;
        String sql = "select " +
                "p.id as post_id, " +
                "p.user_id, " +
                "p.title, " +
                "p.contents , " +
                "p.created_at , " +
                "p.updated_at , " +
                "p.type as post_type, " +
                "u.user_id as author, " +
                "(select count(*) from likes where post_id = p.id and type = 'POST') as like_count, " +
                "case when exists (select 1 from likes where post_id = p.id and user_id = ? and type = 'POST') then 'Y' else 'N' end as is_liked " +
                "from " +
                "post p " +
                "left join user u on p.user_id = u.id " +
                "left join likes l on p.id = l.post_id " +
                "where " +
                "p.id = ? and p.type='COTE' " +
                "group by p.id ";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, codingTestDtoRowMapper(), user_id, post_id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public Long saveCodingTest(CodingTestRequestDto cote, long userId) {
        //게시글 저장
        String postSql = "insert into post (user_id, type, title, contents, created_at, updated_at) " +
                "values (?, ?, ?, ?, ?, ?)";

        //생성된 키 값 받아오기
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(postSql, new String[]{"id"}); // "id"는 자동 생성된 키의 컬럼명
            ps.setLong(1, userId);
            ps.setString(2, "COTE");
            ps.setString(3, cote.getTitle());
            ps.setString(4, cote.getCodingTestContent());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
            return ps;
        }, keyHolder);

        Long postId=keyHolder.getKey().longValue();

        return postId;
    }

    @Override
    public void updateCodingTest(CodingTestRequestDto cote, long postId) {
        //로그인 생기면 수정 권한 있는지 확인 로직 where에 추가
        String postSql="update post "+
                "set title=?, contents=?, updated_at = ? "+
                "where id = ?";
        jdbcTemplate.update(postSql,
                cote.getTitle(), cote.getCodingTestContent(),LocalDateTime.now()
                ,postId);
    }

    @Override
    public void deleteCodingTest(long postId) {
        String sql="delete from post where id=?";
        jdbcTemplate.update(sql, postId);
    }

}