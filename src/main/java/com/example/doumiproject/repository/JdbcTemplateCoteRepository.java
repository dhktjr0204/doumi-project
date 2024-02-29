package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.entity.Cote;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

@Repository
public class JdbcTemplateCoteRepository implements CoteRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCoteRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public CoteDto getByCoteId(long id) {
        //post의 user_id(squence값)과 user의 user_id(nickname용)이 아주 헷갈린다;
        String sql = "select p.id as post_id, p.user_id, p.title, p.contents, p.created_at, p.like, a.answer, " +
                "u.user_id as nickname " +
                "from post p " +
                "inner join answer a on p.id = a.post_id " +
                "inner join user u on p.user_id = u.id " +
                "where p.id = ?";
        //퀴즈 내용 가져오기
        CoteDto coteDto = jdbcTemplate.queryForObject(sql, coteDtoRowMapper(), id);
        //퀴즈와 연결된 태그들 가져오기
        return coteDto;
    }


    @Override
    public Long saveCote(Cote cote, long userId) {
        //게시글 저장
        String postSql = "insert into post (user_id, type, title, contents, created_at, updated_at, `like`) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

        //생성된 키 값 받아오기
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(postSql, new String[]{"id"}); // "id"는 자동 생성된 키의 컬럼명
            ps.setLong(1, userId);
            ps.setString(2, "COTE");
            ps.setString(3, cote.getTitle());
            ps.setString(4, cote.getCoteContent());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
            ps.setInt(7, 0);
            return ps;
        }, keyHolder);

        //게시글을 저장한 후 생성된 postId 가져오기
        Long postId = keyHolder.getKey().longValue();

        //퀴즈 답변 저장
        String answerSql = "insert into answer(post_id, answer) " +
                "values (?,?)";
        String answer = cote.getAnswerContent();

        jdbcTemplate.update(answerSql, postId, answer);

        return postId;
    }

    @Override
    public void updateCote(Cote cote, long postId, long userId) {
        //로그인 생기면 수정 권한 있는지 확인 로직 where에 추가
        String postSql="update post "+
                "set title=?, contents=?, updated_at = ? "+
                "where id = ?";
        jdbcTemplate.update(postSql,
                cote.getTitle(), cote.getCoteContent(),LocalDateTime.now()
                ,postId,userId);

        String answerSql="update answer "+
                "set answer=? "+
                "where post_id=?";
        jdbcTemplate.update(answerSql, cote.getAnswerContent(),postId);
    }

    @Override
    public void deleteCote(long postId) {
        String sql="delete from post where id=?";
        jdbcTemplate.update(sql, postId);
    }

}