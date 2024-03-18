package com.alexmacro.bookinfo.repository;

import com.alexmacro.bookinfo.models.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Repository
public class BooksRepository {

    private List<Book> bookList = new ArrayList<>();

    private Map<UUID, Book> bookIdToBook;
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            bookIdToBook = objectMapper.readValue(
                    new ClassPathResource("mock/MOCK_DATA.json").getFile(),
                    new TypeReference<List<Book>>(){}).stream().collect(toMap(Book::getId, Function.identity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks(){
        return bookIdToBook.values().stream().toList();
    }

    public Book getBookById(UUID bookId){

        return bookIdToBook.get(bookId);
    }

    public Book addBook(Book book){
        UUID newId = UUID.randomUUID();
        book.setId(newId);
        bookIdToBook.put(book.getId(), book);
        return bookIdToBook.get(newId);
    }



}
