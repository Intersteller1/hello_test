package com.myblogapp.myblogapp.controller;


import com.myblogapp.myblogapp.payload.PostDto;
import com.myblogapp.myblogapp.payload.PostResponse;

import com.myblogapp.myblogapp.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //http://localhost:8080/api/posts
//    @PostMapping
//    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
//        PostDto dto = postService.createPost(postDto);
//        return new ResponseEntity<>(dto, HttpStatus.CREATED);
//    }

    //http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<Object>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=title&sortDir=asc
    @GetMapping
    public PostResponse findAllPosts
    (@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageN,
     @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageS,
     @RequestParam(value = "sortBy",defaultValue = "id", required = false) String sortBy,
     @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
       return postService.getAllPosts(pageN,pageS,sortBy,sortDir);
    }
    //http://localhost:8080/api/posts/4
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable("id") long id){
        PostDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }
    //http://localhost:8080/api/posts/4
//    @PutMapping("/{id}")
//    public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id,@RequestBody PostDto postDto){
//        PostDto dto = postService.updateOnePostById(id,postDto);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }
    //http://localhost:8080/api/posts/1
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") long id,@Valid @RequestBody PostDto postDto, BindingResult result){

        if ((result.hasErrors())){
            return new ResponseEntity<Object>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.updateOnePostById(id,postDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/4
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        postService.deleteOnePostById(id);
        return new ResponseEntity<>("Post is deleted successfully!",HttpStatus.OK);
    }
    //http://localhost:8080/api/posts/4/comments
    @GetMapping("/{postId}/comments")
    public PostDto getAllCommentsWithPost(@PathVariable("postId") long postId){
        return postService.getAllCommentsWithPost(postId);
    }

}
