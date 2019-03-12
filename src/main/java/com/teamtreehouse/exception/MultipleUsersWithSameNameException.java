package com.teamtreehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleUsersWithSameNameException extends RuntimeException
{
    public MultipleUsersWithSameNameException(String message)
    {
        super(message);
    }
}
