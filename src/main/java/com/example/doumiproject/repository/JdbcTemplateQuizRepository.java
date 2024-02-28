package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.TagDetailDto;
import com.example.doumiproject.entity.Quiz;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTemplateQuizRepository implements QuizRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateQuizRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public QuizDto getByQuizId(long id) {
        //post의 user_id(squence값)과 user의 user_id(nickname용)이 아주 헷갈린다;
        String sql = "select p.id as post_id, p.user_id, p.title, p.contents, p.created_at, p.like, a.answer, " +
                "u.user_id as author " +
                "from post p " +
                "inner join answer a on p.id = a.post_id " +
                "inner join user u on p.user_id = u.id " +
                "where p.id = ?";
        //퀴즈 내용 가져오기
        QuizDto quizDto = jdbcTemplate.queryForObject(sql, quizDtoRowMapper(), id);
        //퀴즈와 연결된 태그들 가져오기
        List<TagDetailDto> tags = getTags(id);
        quizDto.setTags(tags);
        return quizDto;
    }

    @Override
    public List<TagDetailDto> getTags(long id) {
        String sql = "select t.id, t.name "+
                "from tag t inner join quiztag qt on t.id = qt.tag_id "+
                "where qt.post_id = ?";
        return jdbcTemplate.query(sql, TagRowMapper(), id);
    }

    @Override
    public Long saveQuiz(Quiz quiz, long userId) {
        //게시글 저장
        String postSql = "insert into post (user_id, type, title, contents, created_at, updated_at, `like`) " +
                "values (?, ?, ?, ?, ?, ?, ?)";

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
            ps.setInt(7, 0);
            return ps;
        }, keyHolder);

        //게시글을 저장한 후 생성된 postId 가져오기
        Long postId = keyHolder.getKey().longValue();

        //퀴즈 답변 저장
        String answerSql = "insert into answer(post_id, answer) " +
                "values (?,?)";
        String answer = quiz.getAnswerContent();

        jdbcTemplate.update(answerSql, postId, answer);

        //태그 저장
        saveTags(quiz,postId);
        return postId;
    }

    @Override
    public void updateQuiz(Quiz quiz, long postId) {
        //로그인 생기면 수정 권한 있는지 확인 로직 where에 추가
        String postSql="update post "+
                "set title=?, contents=?, updated_at = ? "+
                "where id = ?";
        jdbcTemplate.update(postSql,
                quiz.getTitle(),quiz.getQuizContent(),LocalDateTime.now()
                ,postId);

        String answerSql="update answer "+
                "set answer=? "+
                "where post_id=?";
        jdbcTemplate.update(answerSql,quiz.getAnswerContent(),postId);

        // 기존 태그 삭제 후 새로운 태그 추가
        String deleteTagsSql = "delete from quiztag where post_id = ?";
        jdbcTemplate.update(deleteTagsSql, postId);

        saveTags(quiz,postId);
    }

    @Override
    public void deleteQuiz(long postId) {
        String sql="delete from post where id=?";
        jdbcTemplate.update(sql, postId);
    }

    //태그 저장
    public void saveTags(Quiz quiz, long postId){
        String tagSql = "insert into quiztag (post_id, tag_id) " +
                "values (?,?)";
        String tags = quiz.getTags();
        if (!tags.isEmpty()) {
            String[] tagStrings = quiz.getTags().split(",");
            for (String tag : tagStrings) {
                jdbcTemplate.update(tagSql, postId, Integer.parseInt(tag));
            }
        }
    }

}