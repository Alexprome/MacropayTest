package com.alexmacro.bookinfo.controllers;

import com.alexmacro.bookinfo.models.Book;
import com.alexmacro.bookinfo.service.BooksService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
public class BooksController {

    @Autowired
    private BooksService booksService;


    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) BigDecimal price,
                                  @RequestParam(required = false) String phrase){
        //Here validate the input phrase so it is NOT null and is only letters
        if(phrase != null) {
            for (Character c : phrase.toCharArray()) {
                if (!Character.isLetter(c)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
            }
            //Lowercase the phrase so it will match the comparison in the BooksService
            phrase = phrase.toLowerCase();
        }
        List<Book> books = booksService.getBooks(price, phrase);
        if(books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/books/{UUID}")
    public Book getBookById(@PathVariable("UUID")UUID bookId){
        return booksService.getBookById(bookId);
    }

    @PostMapping("/books")
    public Book addBook(@Valid @RequestBody Book book){
        return booksService.addBook(book);
    }

    @GetMapping("books/average")
    public BigDecimal getAverage(){
        return booksService.getAverage();
    }



}
