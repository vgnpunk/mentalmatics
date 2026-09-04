package de.vegnpunk.mentalmatics.core.generation

import kotlinx.serialization.Serializable

/**
 * Whether a session reveals correctness per task (US-5.2 alternative) or
 * only via the end-of-session report (US-5.2 default).
 */
@Serializable
enum class SessionFeedbackMode { REPORT_AT_END, RETRY_UNTIL_CORRECT }
