package com.group.libraryapp.domain.book

import jakarta.persistence.*

@Entity
class Book(
  val name: String,
  @Enumerated(value = EnumType.STRING)
  val type: BookType,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null,
) {

  init {
    if (name.isBlank()) {
      throw IllegalArgumentException("이름은 비어 있을 수 없습니다.")
    }
  }

  // 정적 메서드 선언
  // 제일 아래에 있는게 컨벤션
  companion object {
    fun fixture(
      name: String = "인프런",
      type: BookType = BookType.COMPUTER,
      id: Long? = null,
    ): Book {
      return Book(
        name, type, id
      )
    }
  }
}