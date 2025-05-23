package com.jamjam.bookjeok.domains.question.command.service;

import com.jamjam.bookjeok.domains.question.command.dto.request.QuestionRequest;
import com.jamjam.bookjeok.domains.question.command.dto.response.QuestionResponse;
import com.jamjam.bookjeok.exception.question.NotFoundQuestionException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class QuestionMemberCommandServiceTest {

    @Autowired
    QuestionMemberCommandService questionMemberCommandService;

    @DisplayName("회원 문의사항 등록 테스트")
    @Test
    void testRegistQuestion() {

        Long memberUid = 1L;
        Long categoryId = 1L;
        String title = "등록 성공";
        String contents = "등록 잘 되었는지 확인해주실 수 있나요?";

        MockMultipartFile imageFile = new MockMultipartFile(
                "questionImg", "cover.png", "image/png", "fake image data".getBytes());

        QuestionRequest request = QuestionRequest.builder()
                .writerUid(memberUid)
                .questionCategoryId(categoryId)
                .title(title)
                .contents(contents)
                .build();

        QuestionResponse response = questionMemberCommandService.registQuestion(request, imageFile);

        assertThat(response).isNotNull();
        assertThat(response.questionId()).isNotNull();
        assertThat(response.title()).isEqualTo(title);
        assertThat(response.contents()).isEqualTo(contents);

    }

    @DisplayName("회원 문의사항 수정 테스트")
    @Test
    void testModifyQuestion() throws Exception {
        Long memberUid = 7L;
        Long questionId = 6L;
        String title = "수정 성공";
        String contents = "수정 잘 되었는지 확인해주실 수 있나요?";

        MockMultipartFile imageFile = new MockMultipartFile(
                "questionImg", "cover.png", "image/png", "fake image data".getBytes());

        QuestionRequest request = QuestionRequest.builder()
                .writerUid(memberUid)
                .questionCategoryId(2L)
                .title(title)
                .contents(contents)
                .build();

        QuestionResponse response = questionMemberCommandService.modifyQuestion(questionId, request, imageFile);

        assertThat(response).isNotNull();
        assertThat(response.questionId()).isEqualTo(questionId);
        assertThat(response.questionCategoryId()).isEqualTo(2L);
        assertThat(response.title()).isEqualTo(title);
        assertThat(response.contents()).isEqualTo(contents);
        assertThat(response.modifiedAt()).isNotNull();

    }

    @DisplayName("회원 문의 사항 수정 시 예외 발생 테스트")
    @Test
    void testModifyQuestionException() {
        Long memberUid = 7L;
        Long questionId = 123456L;
        Long categoryId = 1L;
        String title = "수정 성공";
        String contents = "수정 잘 되었는지 확인해주실 수 있나요?";

        MockMultipartFile imageFile = new MockMultipartFile(
                "questionImg", "cover.png", "image/png", "fake image data".getBytes());

        QuestionRequest request = QuestionRequest.builder()
                .writerUid(memberUid)
                .questionCategoryId(categoryId)
                .title(title)
                .contents(contents)
                .build();

        assertThatThrownBy(() -> questionMemberCommandService.modifyQuestion(questionId, request, imageFile))
                .isInstanceOf(NotFoundQuestionException.class)
                .hasMessage("존재하지 않는 문의사항 입니다.");

    }

    @DisplayName("회원 문의사항 삭제 테스트")
    @Test
    void testDeleteQuestion() {

        Long questionId = 1L;

        QuestionResponse response = questionMemberCommandService.deleteQuestion(questionId);

        assertThat(response).isNotNull();
        assertThat(response.modifiedAt()).isNotNull();
        assertThat(response.isDeleted()).isEqualTo(true);

    }


}
