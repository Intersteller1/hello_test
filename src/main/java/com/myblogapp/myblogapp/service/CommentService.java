package com.myblogapp.myblogapp.service;

import com.myblogapp.myblogapp.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentId(long postId, long commentId);

    CommentDto updateOneComment(long postId, long id, CommentDto commentDto);

    void deleteOneComment(long postId, long id);
}
