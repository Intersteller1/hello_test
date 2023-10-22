package com.myblogapp.myblogapp.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDto {

    private Long id;
    @NotEmpty(message = "Name cannot be empty!")
    @Size(min = 3, message = "Name must have at least 3 characters")
    private String name;
    @NotEmpty(message = "Email cannot be empty!")
    @Email
    private String email;
    @NotEmpty(message = "Comment/Body cannot be empty!")
    @Size(min = 4, message = "Comment must have at least 4 characters")
    private String body;
}
