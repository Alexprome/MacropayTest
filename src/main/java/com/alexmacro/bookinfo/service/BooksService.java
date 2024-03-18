package com.alexmacro.bookinfo.service;

import com.alexmacro.bookinfo.models.Book;
import com.alexmacro.bookinfo.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BooksService {

    @Autowired
    private BooksRepository booksRepository;

    public Book getBookById(UUID bookId){
        return booksRepository.getBookById(bookId);
    }


    public List<Book> getBooks(BigDecimal comparePrice, String charactersToFind){

        //List of books filled with a stream, depending on price, allowing all books that fullfill the filter conditions
        List<Book> books = booksRepository.getAllBooks().stream()
                .filter(book -> comparePrice == null || book.getPrice().compareTo(comparePrice) > 0)
                .collect(Collectors.toList());


        if(charactersToFind != null){
            //Fill a map with the characters that were input to look for in the authors name
            Map<Character, Integer> characterToFindToCount = new HashMap<>();
            for(char c : charactersToFind.toCharArray()){
                characterToFindToCount.put(c, characterToFindToCount.getOrDefault(c, 0) + 1);
            }

            books = books.stream()
                    .filter(book -> {
                        //Deep copy of the Map that holds the characters and count of those characters,
                        //so it is a different reference
                        Map<Character, Integer> copyOfCharacterToFindToCount = characterToFindToCount.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        /*Loop that goes through the author name letter by letter, then checks if the
                        copy Map contains that letter, if it does, the Map value will be reduced by 1.
                        */
                        for (char authorChar : book.getAuthor().toLowerCase().toCharArray()){
                            if(copyOfCharacterToFindToCount.containsKey(authorChar)){

                                int reducedCount = copyOfCharacterToFindToCount.get(authorChar) - 1;

                                //Conditional statements to reduce the integer value of the Map
                                //if it reacehs 0, the key(letter) will be removed
                                if(reducedCount == 0){
                                    copyOfCharacterToFindToCount.remove(authorChar);
                                } else {
                                    copyOfCharacterToFindToCount.put(authorChar, reducedCount);
                                }

                                //Conditional statement that checks if the Map is empty, so there is no
                                // need to keep looking
                                if(copyOfCharacterToFindToCount.isEmpty()){
                                    return true;
                                }
                            }
                        }

                        return false;
                    })
                    .collect(Collectors.toList());

            if(books.isEmpty()){

            }
        }
        return books;
    }

    public Book addBook(Book book){
        return booksRepository.addBook(book);
    }

    public BigDecimal getAverage(){

        List<Book> allBooks = booksRepository.getAllBooks();
        BigDecimal sum = allBooks.stream()
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(allBooks.size()),2, RoundingMode.HALF_DOWN);
    }


}
