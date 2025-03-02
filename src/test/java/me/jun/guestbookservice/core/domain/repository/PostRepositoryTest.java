package me.jun.guestbookservice.core.domain.repository;

import me.jun.guestbookservice.core.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static me.jun.guestbookservice.support.PostFixture.post;
import static me.jun.guestbookservice.support.WriterFixture.writer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest(properties = "spring.cloud.config.enabled=false")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAllByTest() {
        int expected = 10;

        for (long id = 1; id <= 10; id++) {
            Post post = post().toBuilder()
                    .id(id)
                    .build();

            postRepository.save(post);
        }

        assertThat(postRepository.findAllBy(PageRequest.of(0, 10)).size())
                .isEqualTo(expected);
    }

    @Test
    void deleteAllByWriterTest() {
        for (long id = 1; id <= 10; id++) {
            Post post = post().toBuilder()
                    .id(id)
                    .build();

            postRepository.save(post);
        }

        postRepository.deleteAllByWriter(writer());

        for (long id = 1; id <= 10; id++) {
            assertThat(postRepository.findById(id))
                    .isEmpty();
        }
    }
}