package me.jun.guestbookservice.core.application;

import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static me.jun.guestbookservice.support.PostFixture.post;
import static me.jun.guestbookservice.support.WriterFixture.WRITER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@EmbeddedKafka(
        topics = "member.delete",
        ports = 9092,
        brokerProperties = "listeners=PLAINTEXT://localhost:9092"
)
class PostServiceKafkaTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("#{${delete-member-kafka-topic}}")
    private String topic;

    @Test
    void deleteAllPostByWriterTest() throws InterruptedException {
        for (long id = 1; id <= 10; id++) {
            Post post = post().toBuilder()
                    .id(id)
                    .build();

            postRepository.save(post);
        }

        kafkaTemplate.send(topic, WRITER_ID.toString());

        Thread.sleep(2_000);

        for (long id = 1; id <= 10; id++) {
            assertThat(postRepository.findById(id))
                    .isEmpty();
        }
    }
}