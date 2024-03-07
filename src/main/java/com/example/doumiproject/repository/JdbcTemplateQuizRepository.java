package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.entity.Tag;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateQuizRepository implements QuizRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateQuizRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<QuizDto> getQuizDetails(long post_id, long user_id) {

        String sql = "select " +
                "p.id as post_id, " +
                "p.user_id, " +
                "p.title, " +
                "p.contents , " +
                "p.created_at , " +
                "p.updated_at , " +
                "p.type as post_type, " +
                "u.user_id as author, " +
                "a.answer, " +
                "(select group_concat(t.id) from quiztag qt left join tag t on qt.tag_id = t.id where qt.post_id = p.id order by t.id) as tag_ids," +
                "(select group_concat(t.name) from quiztag qt left join tag t on qt.tag_id = t.id where qt.post_id = p.id order by t.id) as tag_names, " +
                "(select count(*) from likes where post_id = p.id and type = 'POST') as like_count, " +
                "case when exists (select 1 from likes where post_id = p.id and user_id = ? and type = 'POST') then 'Y' else 'N' end as is_liked " +
                "from " +
                "post p " +
                "left join user u on p.user_id = u.id " +
                "left join likes l on p.id = l.post_id " +
                "left join answer a on p.id = a.post_id " +
                "where " +
                "p.id = ? and p.type='QUIZ' " +
                "group by " +
                "p.id;";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, quizDtoRowMapper(), user_id, post_id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public QuizDto getByQuizId(long id) {
        //post의 user_id(squence값)과 user의 user_id(nickname용)이 아주 헷갈린다;
        String sql = "select p.id as post_id, p.user_id, p.title, p.contents, p.created_at, a.answer, " +
                "u.user_id as author " +
                "from post p " +
                "inner join answer a on p.id = a.post_id " +
                "inner join user u on p.user_id = u.id " +
                "where p.id = ?";
        //퀴즈 내용 가져오기
        QuizDto quizDto = jdbcTemplate.queryForObject(sql, quizDtoRowMapper(), id);
        //퀴즈와 연결된 태그들 가져오기
        List<Tag> tags = getTags(id);
        //quizDto.setTags(tags);
        return quizDto;
    }

    @Override
    public List<Tag> getTags(long id) {
        String sql = "select t.id, t.name " +
                "from tag t inner join quiztag qt on t.id = qt.tag_id " +
                "where qt.post_id = ?";
        return jdbcTemplate.query(sql, TagRowMapper(), id);
    }

    @Override
    public Long saveQuiz(QuizRequestDto quiz, long userId) {
        //게시글 저장
        String postSql = "insert into post (user_id, type, title, contents, created_at, updated_at) " +
                "values (?, ?, ?, ?, ?, ?)";

        //생성된 키 값 받아오기
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(postSql, new String[]{"id"}); // "id"는 자동 생성된 키의 컬럼명
            ps.setLong(1, userId);
            ps.setString(2, "QUIZ");
            ps.setString(3, quiz.getTitle());
            ps.setString(4, quiz.getQuizContent());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
            return ps;
        }, keyHolder);

        Long postId = keyHolder.getKey().longValue();

        return postId;
    }

    @Override
    public void saveAnswer(QuizRequestDto quiz, long postId, long userId) {
        //퀴즈 답변 저장
        String answerSql = "insert into answer(post_id, answer) " +
                "values (?,?)";
        String answer = quiz.getAnswerContent();

        jdbcTemplate.update(answerSql, postId, answer);
    }

    @Override
    public void updateQuiz(QuizRequestDto quiz, long postId) {
        //로그인 생기면 수정 권한 있는지 확인 로직 where에 추가
        String postSql = "update post " +
                "set title=?, contents=?, updated_at = ? " +
                "where id = ?";
        jdbcTemplate.update(postSql,
                quiz.getTitle(), quiz.getQuizContent(), LocalDateTime.now()
                , postId);
    }

    @Override
    public void updateAnswer(QuizRequestDto quiz, long postId) {
        String answerSql = "update answer " +
                "set answer=? " +
                "where post_id=?";
        jdbcTemplate.update(answerSql, quiz.getAnswerContent(), postId);
    }

    @Override
    public void deleteQuiz(long postId) {
        String sql = "delete from post where id=?";
        jdbcTemplate.update(sql, postId);
    }

}