package me.jun.guestbookservice.core.domain.repository;

import me.jun.guestbookservice.core.domain.Post;
import me.jun.guestbookservice.core.domain.Writer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBy(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("delete from Post p where p.writer = :writer")
    void deleteAllByWriter(Writer writer);
}
