package me.jun.guestbookservice.core.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Getter
@Entity
public class Post {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private PostInfo postInfo;

    @Embedded
    private Writer writer;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    public Post validateWriter(Long writerId) {
        writer.validate(writerId);
        return this;
    }

    public Post updateTitle(String newTitle) {
        this.postInfo = postInfo.updateTitle(newTitle);
        return this;
    }

    public Post updateContent(String newContent) {
        this.postInfo = postInfo.updateContent(newContent);
        return this;
    }
}
