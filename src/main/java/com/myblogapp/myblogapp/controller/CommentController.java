package com.myblogapp.myblogapp.controller;

import com.myblogapp.myblogapp.payload.CommentDto;
import com.myblogapp.myblogapp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/post/1/comment
//    @PostMapping("/post/{postId}/comment")
//    public ResponseEntity<CommentDto> createOneComment(@PathVariable("postId") long postId, @RequestBody CommentDto commentDto){
//        CommentDto dto = commentService.createComment(postId,commentDto);
//        return new ResponseEntity<>(dto, HttpStatus.CREATED);
//    }

    //http://localhost:8080/api/post/1/comment
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<Object> createOneComment(@PathVariable("postId") long postId, @Valid @RequestBody CommentDto commentDto, BindingResult result){

        if(result.hasErrors()){
            return new ResponseEntity<Object>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommentDto dto = commentService.createComment(postId,commentDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/post/1/comments
    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable("postId") long postId){
        List<CommentDto> allComments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(allComments,HttpStatus.OK);
    }
    //http://localhost:8080/api/post/1/comment/1
    @GetMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") long postId,@PathVariable("commentId") long commentId){
        CommentDto dto = commentService.getCommentId(postId,commentId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/post/1/comment/1
//    @PutMapping("/post/{postId}/comment/{id}")
//    public ResponseEntity<CommentDto> updateComment
//    (@PathVariable("postId") long postId, @PathVariable("id") long id,
//     @RequestBody CommentDto commentDto
//     ){
//        CommentDto dto = commentService.updateOneComment(postId,id,commentDto);
//        return new ResponseEntity<>(dto,HttpStatus.OK);
//    }

    //http://localhost:8080/api/post/1/comment/1
    @PutMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<Object> updateComment
            (@PathVariable("postId") long postId, @PathVariable("id") long id,
             @Valid @RequestBody CommentDto commentDto, BindingResult result
            ){
        if(result.hasErrors()){
            return new ResponseEntity<Object>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDto dto = commentService.updateOneComment(postId,id,commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //http://localhost:8080/api/post/1/comment/1
    @DeleteMapping("/post/{postId}/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId,@PathVariable("id") long id){
        commentService.deleteOneComment(postId,id);
        return new ResponseEntity<>("Comment has been successfully deleted!",HttpStatus.OK);
    }

}
