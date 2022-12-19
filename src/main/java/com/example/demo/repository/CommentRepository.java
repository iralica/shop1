package com.example.demo.repository;

import com.example.demo.Entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(Long productId);


    @Transactional
    void deleteByProductId(long productId);


}
