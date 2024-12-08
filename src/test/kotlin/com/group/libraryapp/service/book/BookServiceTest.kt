package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class BookServiceTest @Autowired constructor(
  private val bookService: BookService,
  private val bookRepository: BookRepository,
  private val userRepository: UserRepository,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

  @Test
  fun 책_등록이_정상_동작한다() {

    // given
    val request = BookRequest("인프런", BookType.COMPUTER)

    // when
    bookService.saveBook(request)

    // then
    val results = bookRepository.findAll()
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("인프런")
    assertThat(results[0].type).isEqualTo(BookType.COMPUTER)

  }

  @Test
  fun 정상적인_책_대출하기() {

    // given
    val savedJavaBook = bookRepository.save(Book.fixture())
    val savedJavaUser = userRepository.save(
      User(
        "김현겸",
        25
      )
    )
    val request = BookLoanRequest("김현겸", "인프런")

    // when
    bookService.loanBook(request)

    // then
    val results = userLoanHistoryRepository.findAll()
    assertThat(results).hasSize(1)
    assertThat(results[0].bookName).isEqualTo("인프런")
    assertThat(results[0].user.id).isEqualTo(savedJavaUser.id)
    assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)

  }

  @Test
  fun 실패한_책_대출하기() {

    // given
    val savedJavaBook = bookRepository.save(
      Book.fixture()
    )
    val savedJavaUser = userRepository.save(
      User(
        "김현겸",
        25
      )
    )
    userLoanHistoryRepository.save(
      UserLoanHistory.fixture(savedJavaUser, "인프런")
    )
    val request = BookLoanRequest("김현겸", "인프런")

    // when & then
    val message = assertThrows<IllegalArgumentException> {
      bookService.loanBook(request)
    }.message
    assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")

  }

  @Test
  fun 정상적인_책_반납하기() {

    // given
    val savedJavaBook = bookRepository.save(
      Book.fixture()
    )
    val savedJavaUser = userRepository.save(
      User(
        "김현겸",
        25
      )
    )
    savedJavaUser.loanBook(savedJavaBook)
//    userLoanHistoryRepository.save(UserLoanHistory.fixture(savedJavaUser, "인프런"))
    val request = BookReturnRequest("김현겸", "인프런")

    // when
    bookService.returnBook(request)

    // then
    val results = userLoanHistoryRepository.findAll()
    assertThat(results).hasSize(1)
    assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)

  }

  @Test
  fun 책_대여_권수_확인() {
    // given
    val savedUser = userRepository.save(User("김현겸", 10))
    userLoanHistoryRepository.saveAll(listOf(
      UserLoanHistory.fixture(savedUser, "A"),
      UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
      UserLoanHistory.fixture(savedUser, "C", UserLoanStatus.RETURNED),
    ))

    // when
    val results = bookService.countLoanedBook()

    // then
    assertThat(results).isEqualTo(1)
  }

  @Test
  fun 분야별_책_권수_확인() {

    // given
    bookRepository.saveAll(listOf(
      Book.fixture("A", BookType.COMPUTER),
      Book.fixture("B", BookType.COMPUTER),
      Book.fixture("C", BookType.SCIENCE)
    ))

    // when
    val results = bookService.getBookStatistics()

    // then
    assertThat(results).hasSize(2)
    val computerDto = results.first { result -> result.type == BookType.COMPUTER }
    assertThat(computerDto.count).isEqualTo(2)

    val scienceDto = results.first { result -> result.type == BookType.SCIENCE }
    assertThat(scienceDto.count).isEqualTo(1)
  }
}