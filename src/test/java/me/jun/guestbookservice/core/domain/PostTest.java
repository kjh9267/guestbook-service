package me.jun.guestbookservice.core.domain;

import me.jun.guestbookservice.core.domain.exception.WriterMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static me.jun.guestbookservice.support.PostFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("deprecation")
public class PostTest {

    private Post post;

    @Mock
    private PostInfo mockPostInfo;

    @Mock
    private Writer mockWriter;

    @BeforeEach
    void setUp() {
        post = post().toBuilder()
                .postInfo(mockPostInfo)
                .writer(mockWriter)
                .build();
    }

    @Test
    void constructorTest() {
        new Post();
    }

    @Test
    void constructorTest2() {
        Post expected = Post.builder()
                .id(POST_ID)
                .postInfo(postInfo())
                .writer(writer())
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();

        assertThat(post())
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void validateWriterTest() {
        given(mockWriter.validate(any()))
                .willReturn(mockWriter);

        assertDoesNotThrow(
                () -> post.validateWriter(WRITER_ID)
        );
    }

    @Test
    void validateWriterFailTest() {
        given(mockWriter.validate(any()))
                .willThrow(WriterMismatchException.of(WRITER_ID.toString()));

        assertThrows(
                WriterMismatchException.class,
                () -> post.validateWriter(WRITER_ID)
        );
    }

    @Test
    void updateTitleTest() {
        Post expected = post().toBuilder()
                .postInfo(
                        postInfo().toBuilder()
                                .title(NEW_TITLE)
                                .build()
                )
                .build();

        given(mockPostInfo.updateTitle(any()))
                .willReturn(
                        postInfo().toBuilder()
                                .title(NEW_TITLE)
                                .build()
                );

        assertThat(post.updateTitle("new title string"))
                .isEqualToIgnoringGivenFields(expected, "writer");
    }

    @Test
    void updateContentTest() {
        Post expected = post().toBuilder()
                .postInfo(
                        postInfo().toBuilder()
                                .content(NEW_CONTENT)
                                .build()
                )
                .build();

        given(mockPostInfo.updateContent(NEW_CONTENT))
                .willReturn(
                        postInfo().toBuilder()
                                .content(NEW_CONTENT)
                                .build()
                );

        assertThat(post.updateContent("new content string"))
                .isEqualToIgnoringGivenFields(expected, "writer");
    }
}
