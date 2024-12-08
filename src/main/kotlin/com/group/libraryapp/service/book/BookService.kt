package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import com.group.libraryapp.utils.fail
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
    val book = Book(request.name, request.type)
    bookRepository.save(book)
  }

  fun loanBook(request : BookLoanRequest) {
    val book = bookRepository.findByName(request.bookName) ?: fail()
    if (userLoanHistoryRepository.findByBookNameAndStatus(request.bookName, UserLoanStatus.LOANED) != null) {
      throw IllegalArgumentException("진작 대출되어 있는 책입니다")
    }

    val user =
      userRepository.findByName(request.userName) ?: fail()
    user.loanBook(book)
  }

  fun returnBook(request : BookReturnRequest) {
    val user =
      userRepository.findByName(request.userName) ?: fail()
    user.returnBook(request.bookName)
  }

  @Transactional(readOnly = true)
  fun countLoanedBook(): Int {
    return userLoanHistoryRepository.findAllByStatus(UserLoanStatus.LOANED)
      .size
  }

  @Transactional(readOnly = true)
  fun getBookStatistics(): List<BookStatResponse> {
    val results = mutableListOf<BookStatResponse>()
    val books = bookRepository.findAll()

    for (book in books) {
      results.firstOrNull { dto -> book.type == dto.type }?.plus()
        ?: results.add(BookStatResponse(book.type, 1))
    }
    return results
  }
}