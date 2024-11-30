package com.group.libraryapp.utils

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

// Optional 처리하는 중복 코드 처리
fun fail(): Nothing {
  throw IllegalArgumentException()
}

fun<T, ID> CrudRepository<T, ID>.findByIdOrThrow(id: ID): T {
  return this.findByIdOrNull(id) ?: fail()
}