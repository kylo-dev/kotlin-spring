package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loadhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loadhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserService,
  private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {
  @Test
  fun saveTest() {

    // given
    val request = UserCreateRequest("김현겸", null)

    // when
    userService.saveUser(request)

    // then
    val results = userRepository.findAll()
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("김현겸")
    assertThat(results[0].age).isNull()
  }

  @Test
  fun getUserTest() {

    // given
    userRepository.saveAll(listOf(
      User("A", 20),
      User("B", null)
    ))

    // when
    val results = userService.getUsers()

    // then
    assertThat(results).hasSize(2)
    assertThat(results).extracting("name")
      .containsExactlyInAnyOrder("A", "B")
    assertThat(results).extracting("age")
      .containsExactlyInAnyOrder(20, null)
  }

  @Test
  fun updateTest() {

    // given
    val saveJavaUser = userRepository.save(
      User(
        "김현겸",
        25
      )
    )
    val request = UserUpdateRequest(saveJavaUser.id!!, "rlagusrua")

    // when
    userService.updateUserName(request)

    // then
    val result = userRepository.findAll()[0]
    assertThat(result.name).isEqualTo("rlagusrua")

  }

  @Test
  fun deleteTest() {

    // given
    val saveJavaUser = userRepository.save(
      User(
        "김현겸",
        25
      )
    )

    // when
    userService.deleteUser("김현겸")

    // then
    assertThat(userRepository.findAll()).isEmpty()

  }

  @Test
  fun 대출_기록이_없는_유저도_응답에_포함() {

    // given
    userRepository.save(User("A", null))

    // when
    val results = userService.getUserLoanHistories()

    // then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("A")
    assertThat(results[0].books).isEmpty()
  }

  @Test
  fun 대출_기록이_많은_유저의_응답() {

    // given
    val savedUser = userRepository.save(User("A", null))
    userLoanHistoryRepository.saveAll(listOf(
      UserLoanHistory.fixture(savedUser, "book1", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "book2", UserLoanStatus.LOANED),
      UserLoanHistory.fixture(savedUser, "book3", UserLoanStatus.RETURNED),
    ))

    // when
    val results = userService.getUserLoanHistories()

    // then
    assertThat(results).hasSize(1)
    assertThat(results[0].name).isEqualTo("A")
    assertThat(results[0].books).hasSize(3)
    assertThat(results[0].books).extracting("name")
      .containsExactlyInAnyOrder("book1", "book2", "book3")
    assertThat(results[0].books).extracting("isReturn")
      .containsExactlyInAnyOrder(false, false, true)
  }
}