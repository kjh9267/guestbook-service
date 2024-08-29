package me.jun.guestbookservice.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

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

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    public Post updateTitle(String newTitle) {
        this.postInfo = postInfo.updateTitle(newTitle);
        return this;
    }

    public Post updateContent(String newContent) {
        this.postInfo = postInfo.updateContent(newContent);
        return this;
    }
}
