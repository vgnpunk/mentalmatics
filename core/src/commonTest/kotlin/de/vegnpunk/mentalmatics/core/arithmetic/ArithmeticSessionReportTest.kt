package de.vegnpunk.mentalmatics.core.arithmetic

import kotlin.test.Test
import kotlin.test.assertEquals

class ArithmeticSessionReportTest {
    private val task = ArithmeticTask(listOf(7, 5), ArithmeticOperation.ADDITION, 12)

    @Test
    fun `counts correct and total results`() {
        val report =
            ArithmeticSessionReport(
                results =
                    listOf(
                        ArithmeticTaskResult(task, userAnswer = 12, isCorrect = true),
                        ArithmeticTaskResult(task, userAnswer = 11, isCorrect = false),
                        ArithmeticTaskResult(task, userAnswer = 12, isCorrect = true),
                    ),
            )

        assertEquals(2, report.correctCount)
        assertEquals(3, report.totalCount)
    }

    @Test
    fun `an empty report has zero correct and zero total`() {
        val report = ArithmeticSessionReport(results = emptyList())

        assertEquals(0, report.correctCount)
        assertEquals(0, report.totalCount)
    }
}
