package com.example.demo.controller;


import com.example.demo.Entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController{
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductsRepository productsRepository;

    public CommentController() {
    }

    @PostMapping("/products/{productId}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable(value = "productId") Long productId,
            @RequestBody Comment commentRequest
    )
    {
        // найти товар по идентификатору
        // установить этот товар для комента
        // спасти комент в базу данных
        // вернуть json комента с правильным идентификатором товара
        // выбросить исключение если такого товара нет
        Comment comment = productsRepository.findById(productId).map(
                product -> {
                    commentRequest.setProduct(product);
                    return commentRepository.save(commentRequest);
                }
        ).orElseThrow(
                () -> new RuntimeException("Product not found " + productId)

        );
        return new ResponseEntity<>(comment, HttpStatus.CREATED);

    }

    @GetMapping("/products/{productId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByProductId(
            @PathVariable(value = "productId") Long productId
    )
    {
        if(!productsRepository.existsById(productId))
            throw  new IllegalArgumentException("Product not found " + productId);
        // TODO РІРµСЂРЅСѓС‚СЊ РІСЃРµ РєРѕРјРјРµРЅС‚С‹ РїРѕ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂСѓ РїСЂРѕРґСѓРєС‚Р°
        return new ResponseEntity<>(
                commentRepository.findByProductId(productId),
                HttpStatus.OK
        );
    }


    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getCommentById(
            @PathVariable(value = "commentId") Long commentId
    )
    {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Comment not found " + commentId
                        )
                );
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


    // PUT 	/comments/:id 			update a Comment by :id
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateCommentById(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody Comment commentRequest
    )
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("CommentId " + commentId + "mot found"));
        comment.setContent(commentRequest.getContent());
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
    }

    // DELETE 	/comments/:id 			delete a Comment by :id
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentById(
            @PathVariable(name = "commentId") Long commentId
    )
    {
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE 	/products/:id/comments 	delete all Comments of a Product
    @DeleteMapping("/products/{productId}/comments")
    public ResponseEntity<HttpStatus> deleteCommentsByProductId(
            @PathVariable(name = "productId") Long productId
    )
    {
        if(!productsRepository.existsById(productId))
            throw new IllegalArgumentException("Product with " + productId + " not found");
        commentRepository.deleteByProductId(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
