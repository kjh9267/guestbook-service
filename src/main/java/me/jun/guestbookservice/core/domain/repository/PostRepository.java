package me.jun.guestbookservice.core.domain.repository;

import me.jun.guestbookservice.core.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBy(Pageable pageable);
}
