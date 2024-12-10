package com.group.libraryapp.repository.book

import com.group.libraryapp.domain.book.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {

  fun findByName(bookName: String): Book?

//  @Query("select new com.group.libraryapp.dto.book.response.BookStatResponse(b.type, count(b.id))" +
//      " from Book b group by b.type")
//  fun getStat(): List<BookStatResponse>
}