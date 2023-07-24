package com.example.bookshop.controllers;

import com.example.bookshop.exceptions.EmptySearchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", ex);
        return "redirect:/";
    }
}
