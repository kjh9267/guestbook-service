package me.jun.guestbookservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.PostInfo;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class PostFixture {

    public static final Long POST_ID = 1L;

    public static final String TITLE = "title string";

    public static final String CONTENT = "content string";

    public static final Long WRITER_ID = 1L;

    public static final Instant CREATED_AT = Instant.now();

    public static final Instant UPDATED_AT = Instant.now();

    public static final String NEW_TITLE = "new title string";

    public static final String NEW_CONTENT = "new content string";

    public static PostInfo postInfo() {
        return PostInfo.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
    }

    public static Post post() {
        return Post.builder()
                .id(POST_ID)
                .postInfo(postInfo())
                .writerId(WRITER_ID)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }
}
