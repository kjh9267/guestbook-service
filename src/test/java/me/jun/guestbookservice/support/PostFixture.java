package me.jun.guestbookservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.jun.guestbookservice.core.application.dto.*;
import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.PostInfo;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static me.jun.guestbookservice.support.WriterFixture.WRITER_ID;
import static me.jun.guestbookservice.support.WriterFixture.writer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class PostFixture {

    public static final Long POST_ID = 1L;

    public static final String TITLE = "title string";

    public static final String CONTENT = "content string";

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
                .writer(writer())
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static PostInfo updatedPostInfo() {
        return PostInfo.builder()
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();
    }

    public static Post updatedPost() {
        return Post.builder()
                .id(POST_ID)
                .postInfo(updatedPostInfo())
                .writer(writer())
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    public static PostResponse postResponse() {
        return PostResponse.of(post());
    }

    public static CreatePostRequest createPostRequest() {
        return CreatePostRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .writerId(WRITER_ID)
                .build();
    }

    public static RetrievePostRequest retrievePostRequest() {
        return RetrievePostRequest.builder()
                .id(POST_ID)
                .build();
    }

    public static UpdatePostRequest updatePostRequest() {
        return UpdatePostRequest.builder()
                .id(POST_ID)
                .title(NEW_TITLE)
                .content(NEW_CONTENT)
                .build();
    }

    public static PostResponse updatedPostResponse() {
        return PostResponse.of(updatedPost());
    }

    public static DeletePostRequest deletePostRequest() {
        return DeletePostRequest.builder()
                .id(POST_ID)
                .build();
    }

    public static List<Post> postList() {
        return LongStream.rangeClosed(1, 10)
                .mapToObj(
                        id -> post().toBuilder()
                                .id(id)
                                .build()
                )
                .collect(Collectors.toList());
    }

    public static PostListResponse postListResponse() {
        return PostListResponse.of(postList());
    }
}
