package com.myblogapp.myblogapp.service.impl;

import com.myblogapp.myblogapp.entity.Comment;
import com.myblogapp.myblogapp.entity.Post;
import com.myblogapp.myblogapp.exception.BlogAPIException;
import com.myblogapp.myblogapp.exception.ResourceNotFoundException;
import com.myblogapp.myblogapp.payload.CommentDto;
import com.myblogapp.myblogapp.repository.CommentRepository;
import com.myblogapp.myblogapp.repository.PostRepository;
import com.myblogapp.myblogapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", postId));

        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        CommentDto dto = mapToDto(savedComment);
        return dto;
    }
    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(x->mapToDto(x)).collect(Collectors.toList());
    }
    @Override
    public CommentDto getCommentId(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Post with postId "+postId+" has not any comment by comment id "+commentId +"!");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateOneComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Post with postId "+postId+" has not any comment to update by comment id "+id+"!");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteOneComment(long postId, long id) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "postId", postId));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Post with postId "+postId+" has not any comment to delete by comment id "+id+"!");
        }

        commentRepository.deleteById(comment.getId());
    }

    private CommentDto mapToDto(Comment savedComment) {
        CommentDto commentDto = mapper.map(savedComment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(savedComment.getId());
//        commentDto.setName(savedComment.getName());
//        commentDto.setEmail(savedComment.getEmail());
//        commentDto.setBody(savedComment.getBody());

        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());

        return comment;
    }
}
