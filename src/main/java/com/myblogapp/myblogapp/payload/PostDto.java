package com.myblogapp.myblogapp.payload;

import com.myblogapp.myblogapp.entity.Comment;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostDto {

    private long id;

    @NotEmpty(message = "Title cannot be empty!")
    @Size(min = 2, message = "Title must have at least 2 characters")
    private String title;
    @NotEmpty(message = "Description cannot be empty!")
    @Size(min = 4, message = "Description must have at least 4 characters")
    private String description;
    @NotEmpty(message = "Content cannot be empty!")
    @Size(min = 4, message = "Content must have at least 4 characters")
    private String content;
    private List<CommentDto> commentDtos;

}
