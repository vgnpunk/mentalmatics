package de.vegnpunk.mentalmatics.core.generation

/**
 * Strategy interface (ADR-014): one implementation per exercise area
 * (e.g. `ArithmeticTaskGenerator`, `ScenarioTaskGenerator`,
 * `BaseConversionTaskGenerator`), so adding a new exercise area never
 * requires changing this contract.
 */
interface TaskGenerator<Task> {
    fun generate(): Task
}
