package minesweeper.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class BoardFieldsTest : DescribeSpec({
    describe("mineCount") {
        it("좌표 들을 입력받아 지뢰 개수를 확인할 수 있다") {
            val boardFields = BoardFields(
                listOf(
                    BoardField.mine(Coordinate(0, 0)),
                    BoardField.nonMine(Coordinate(0, 1)),
                    BoardField.mine(Coordinate(0, 2)),
                    BoardField.nonMine(Coordinate(1, 0)),
                    BoardField.nonMine(Coordinate(1, 2)),
                    BoardField.mine(Coordinate(2, 0)),
                    BoardField.nonMine(Coordinate(2, 1)),
                    BoardField.nonMine(Coordinate(2, 2)),
                )
            )

            val coordinates = listOf(
                Coordinate(0, 0),
                Coordinate(0, 1),
                Coordinate(0, 2),
                Coordinate(3, 3),
            )
            boardFields.mineCount(coordinates) shouldBe 2
        }
    }

    describe("open") {
        context("필드들에 해당하는 좌표를 입력받아") {
            it("해당하는 필드를 열 수 있다.") {
                val boardFields = boardFields()
                boardFields.open(Coordinate(0, 0))

                val boardField = boardFields.boardFields.first() { it.coordinate == Coordinate(0, 0) }
                boardField.isOpen shouldBe true
            }
        }

        context("필드들에 해당하지 않는 좌표를 입력받으면") {
            it("IllegalArgumentException 이 발생한다.") {
                val boardFields = boardFields()

                shouldThrow<IllegalArgumentException> { boardFields.open(Coordinate(3, 3)) }
            }
        }
    }

    describe("openNumberFields") {
        /**
         * * C *
         * C C C
         * * C C
         */
        context("필드들에 해당하는 좌표들 입력받아") {
            it("숫자필드 인 경우 필드를 열 수 있다") {
                val boardFields = boardFields()
                boardFields.openNumberFields(listOf(Coordinate(1, 0)))

                val openedFields = boardFields.boardFields.filter { it.isOpen }.map { it.coordinate }
                openedFields shouldContainExactly listOf(Coordinate(1, 0))
            }

            it("지뢰필드 인 경우 필드를 열지 않는다") {
                val boardFields = boardFields()
                boardFields.openNumberFields(listOf(Coordinate(0, 0)))

                val openedFields = boardFields.boardFields.filter { it.isOpen }.map { it.coordinate }
                openedFields shouldContainExactly emptyList()
            }

            it("필드들에 해당하지 않는 필드의 경우 아무 필드도 열지 않는다") {
                val boardFields = boardFields()
                boardFields.openNumberFields(listOf(Coordinate(3, 3)))

                val openedFields = boardFields.boardFields.filter { it.isOpen }.map { it.coordinate }
                openedFields shouldContainExactly emptyList()
            }
        }
    }

    describe("isAllOpenedNumberFields") {
        it("모든 숫자필드가 오픈되었는 지 확인할 수 있다") {
            val boardFields = BoardFields(
                listOf(
                    MineField(Coordinate(0, 0)),
                    NumberField(Coordinate(2, 2), true),
                    NumberField(Coordinate(2, 3), true),
                )
            )

            boardFields.isAllOpenedNumberFields() shouldBe true
        }
    }

    describe("hasOpenedMineField") {
        it("오픈된 지뢰필드가 있는지 확인할 수 있다") {
            val boardFields = BoardFields(
                listOf(
                    MineField(Coordinate(0, 0), true),
                    NumberField(Coordinate(2, 2)),
                )
            )

            boardFields.hasOpenedMineField() shouldBe true
        }
    }
})

private fun boardFields() = BoardFields(
    listOf(
        BoardField.mine(Coordinate(0, 0)),
        BoardField.nonMine(Coordinate(0, 1)),
        BoardField.mine(Coordinate(0, 2)),
        BoardField.nonMine(Coordinate(1, 0)),
        BoardField.nonMine(Coordinate(1, 1)),
        BoardField.nonMine(Coordinate(1, 2)),
        BoardField.mine(Coordinate(2, 0)),
        BoardField.nonMine(Coordinate(2, 1)),
        BoardField.nonMine(Coordinate(2, 2)),
    )
)
