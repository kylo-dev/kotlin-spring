package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.`as`
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class UserServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val userService: UserService
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
    val saveUser = userRepository.save(User("김현겸", 25))
    val request = UserUpdateRequest(saveUser.id, "rlagusrua")

    // when
    userService.updateUserName(request)

    // then
    val result = userRepository.findAll()[0]
    assertThat(result.name).isEqualTo("rlagusrua")

  }

  @Test
  fun deleteTest() {

    // given
    val saveUser = userRepository.save(User("김현겸", 25))

    // when
    userService.deleteUser("김현겸")

    // then
    assertThat(userRepository.findAll()).isEmpty()

  }
}