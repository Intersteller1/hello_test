package com.myblogapp.myblogapp.service;

import com.myblogapp.myblogapp.payload.PostDto;
import com.myblogapp.myblogapp.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageN, int pageS, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updateOnePostById(long id,PostDto postDto);

    void deleteOnePostById(long id);

    PostDto getAllCommentsWithPost(long postId);
}
