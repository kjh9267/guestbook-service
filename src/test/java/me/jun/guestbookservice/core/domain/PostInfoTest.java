package me.jun.guestbookservice.core.domain;

import org.junit.jupiter.api.Test;

import static me.jun.guestbookservice.support.PostFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("deprecation")
class PostInfoTest {

    @Test
    void constructorTest() {
        PostInfo expected = new PostInfo();

        assertThat(new PostInfo())
                .isEqualTo(expected);
    }

    @Test
    void constructorTest2() {
        PostInfo expected = PostInfo.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();

        assertThat(postInfo())
                .isEqualToComparingFieldByField(expected);
    }

    @Test
    void updateTitleTest() {
        PostInfo expected = postInfo().toBuilder()
                .title(NEW_TITLE)
                .build();

        assertThat(postInfo().updateTitle("new title string"))
                .isEqualToIgnoringGivenFields(expected);
    }

    @Test
    void updateContentTest() {
        PostInfo expected = postInfo().toBuilder()
                .content(NEW_CONTENT)
                .build();

        assertThat(postInfo().updateContent("new content string"))
                .isEqualToComparingFieldByField(expected);
    }
}