package com.example.doumiproject.service;

import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.dto.TagDto;
import com.example.doumiproject.entity.Tag;
import com.example.doumiproject.exception.post.NoContentException;
import com.example.doumiproject.repository.PostRepository;
import com.example.doumiproject.repository.QuizRepository;
import com.example.doumiproject.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class QuizServiceImplTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private QuizServiceImpl quizService;

    private PostDto createDummyPostDto(long id, String userId, String title) {
        return PostDto.builder()
                .id(id)
                .userId(userId)
                .type("QUIZ")
                .title(title)
                .contents("Quiz Content")
                .createdAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .updatedAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .likeCount(0L)
                .build();
    }

    public static QuizDto createDummyQuizDto(long id, long userId) {
        return QuizDto.builder()
                .id(id)
                .userId(userId)
                .author("퀴즈 작성자")
                .title("퀴즈 제목")
                .contents("퀴즈 내용")
                .answer("정답")
                .postType("QUIZ")
                .createdAt(java.sql.Timestamp.valueOf("2022-01-01 00:00:00"))
                .updatedAt(java.sql.Timestamp.valueOf("2022-01-01 00:00:00"))
                .tags(Arrays.asList(createDummyTag(), createDummyTag())) // 가짜 태그 리스트 추가
                .likeCount(5L)
                .isLiked(true)
                .build();
    }

    public static Tag createDummyTag() {
        return Tag.builder()
                .id(1L)
                .type("Java")
                .name("인터페이스")
                .build();
    }

    @Test
    @DisplayName("퀴즈 게시물들 가져오기")
    void getAllQuiz() {
        //given
        List<PostDto> mockPostList = Arrays.asList(
                createDummyPostDto(1L, "user1", "포스트제목1"),
                createDummyPostDto(2L, "user2", "포스트제목2"),
                createDummyPostDto(3L, "user3", "포스트제목3")
        );
        int page = 1;
        int pageSize = 10;

        when(postRepository.findAllPost(page, pageSize, "QUIZ")).thenReturn(mockPostList);

        //when
        List<PostDto> result = quizService.getAllQuiz(page, pageSize);

        //then
        assertThat(result).isEqualTo(mockPostList);
    }

    @Test
    @DisplayName("아무런 게시물이 없을 때")
    void getAllQuiz_withValidEmptyQuiz() {
        //given
        int page = 1;
        int pageSize = 10;
        when(postRepository.findAllPost(page, pageSize, "QUIZ")).thenReturn(null);

        //when
        List<PostDto> result = quizService.getAllQuiz(page, pageSize);

        //then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("퀴즈 게시물 가져오기")
    void getQuiz() {
        //given
        long postId = 1;
        long userId = 1;
        QuizDto mockQuizDto = createDummyQuizDto(postId, userId);

        when(quizRepository.getQuizDetails(postId, userId)).thenReturn(Optional.ofNullable(mockQuizDto));

        //when
        QuizDto quiz = quizService.getQuiz(postId, userId);

        //then
        assertThat(quiz).isEqualTo(mockQuizDto);
    }

    @Test
    @DisplayName("존재하지 않는 퀴즈 게시물 가져오기")
    void getQuiz_withInValidData() {
        //given
        long postId = 1;
        long userId = 1;

        when(quizRepository.getQuizDetails(postId, userId)).thenReturn(Optional.empty());

        //then
        //예외 발생 됐는지 확인
        assertThatThrownBy(() -> quizService.getQuiz(postId, userId))
                .isInstanceOf(NoContentException.class).hasMessage(null);;
    }

    @Test
    @DisplayName("태그 정렬 상태로 제대로 나오는지 테스트")
    void getAllTag() {
        //given
        List<TagDto> unsortedTag = Arrays.asList(
                TagDto.builder().type("FrontEnd").detailTags(List.of(new Tag(1, "FrontEnd", "Detail1"))).build(),
                TagDto.builder().type("Java").detailTags(List.of(new Tag(2, "Java", "Detail2"))).build(),
                TagDto.builder().type("DB").detailTags(Arrays.asList(new Tag(3, "DB", "Detail3"), new Tag(4, "DB", "Detail4"))).build(),
                TagDto.builder().type("AWS").detailTags(List.of(new Tag(5, "AWS", "Detail5"))).build(),
                TagDto.builder().type("Spring").detailTags(Arrays.asList(new Tag(6, "Spring", "Detail9"), new Tag(10, "Spring", "Detail10"))).build()
        );

        List<TagDto> sortedTag = Arrays.asList(
                TagDto.builder().type("Java").detailTags(List.of(new Tag(2, "Java", "Detail2"))).build(),
                TagDto.builder().type("Spring").detailTags(Arrays.asList(new Tag(6, "Spring", "Detail9"), new Tag(10, "Spring", "Detail10"))).build(),
                TagDto.builder().type("DB").detailTags(Arrays.asList(new Tag(3, "DB", "Detail3"), new Tag(4, "DB", "Detail4"))).build(),
                TagDto.builder().type("AWS").detailTags(List.of(new Tag(5, "AWS", "Detail5"))).build(),
                TagDto.builder().type("FrontEnd").detailTags(List.of(new Tag(1, "FrontEnd", "Detail1"))).build()
        );


        //정렬 안된 tag 리스트를 서비스단을 거쳤을 때 정렬되는 지 확인
        when(tagRepository.findAll()).thenReturn(unsortedTag);

        //when
        List<TagDto> result = quizService.getAllTags();

        //then
        assertThat(result).isEqualTo(sortedTag);
    }

    @Test
    @DisplayName("저장 테스트")
    void saveQuiz(){
        //given
        Long postId=1L;
        long userId=1;

        QuizRequestDto quiz = QuizRequestDto.builder()
                .userId(userId)
                .title("Sample Title")
                .tags("1,2,3")
                .quizContent("Sample Quiz Content")
                .answerContent("Sample Answer Content")
                .build();

        when(quizRepository.saveQuiz(quiz, userId)).thenReturn(postId);

        //when
        Long result = quizService.saveQuiz(quiz, userId);

        //then
        assertThat(result).isEqualTo(postId);
    }


}