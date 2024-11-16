package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalculatorTest {

  @Test
  fun addTest() {

    // given
    val calculator = Calculator(5)
    calculator.add(3)

    // when
    val expected = Calculator(8);

    // then
    assertThat(calculator.number).isEqualTo(expected.number)
  }

  @Test
  fun minus() {

    // given
    val calculator = Calculator(10)
    calculator.minus(5)
    // when
    val number = calculator.number

    // then
    assertThat(number).isEqualTo(5)
  }


}