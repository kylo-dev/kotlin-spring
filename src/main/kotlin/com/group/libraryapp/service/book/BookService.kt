package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
  private val bookRepository: BookRepository,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

  fun saveBook(request: BookRequest) {
    val book = Book(request.name)
    bookRepository.save(book)
  }

  fun loanBook(request : BookLoanRequest) {
    val book = bookRepository.findByName(request.bookName).orElseThrow(::IllegalArgumentException)
    if (userLoanHistoryRepository.findByBookNameAndIsReturn(request.bookName, false) != null) {
      throw IllegalArgumentException("진작 대출되어 있는 책입니다")
    }

    val user =
      userRepository.findByName(request.userName).orElseThrow(::IllegalArgumentException)
    user.loanBook(book)
  }

  fun returnBook(request : BookReturnRequest) {
    val user =
      userRepository.findByName(request.userName).orElseThrow(::IllegalArgumentException)
    user.returnBook(request.bookName)
  }
}