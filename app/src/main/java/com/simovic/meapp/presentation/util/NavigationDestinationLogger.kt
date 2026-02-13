package com.simovic.meapp.presentation.util

import android.os.Bundle
import androidx.navigation.NavDestination
import com.simovic.meapp.feature.base.util.TimberLogTags
import timber.log.Timber

object NavigationDestinationLogger {
    fun logDestinationChange(
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        val logMessage =
            buildString {
                appendLine("Navigation destination changed:")
                appendLine("\tRoute: ${destination.route ?: "Unknown"}")
                appendLine("\tID: ${destination.id}")
                appendLine("\tLabel: ${destination.label ?: "No Label"}")

                arguments?.let { bundle ->
                    if (!bundle.isEmpty) {
                        appendLine("   Arguments:")
                        bundle.keySet().forEach { key ->
                            appendLine("\t\t$key: ${getValueFromBundle(bundle, key)}")
                        }
                    }
                }
            }

        Timber.tag(TimberLogTags.NAVIGATION).d(logMessage)
    }

    private fun getValueFromBundle(
        bundle: Bundle,
        key: String,
    ): String {
        val value = bundle.get(key) ?: return "null"
        return when (value) {
            is String -> "\"$value\""
            is IntArray -> value.contentToString()
            is LongArray -> value.contentToString()
            is FloatArray -> value.contentToString()
            is BooleanArray -> value.contentToString()
            is Array<*> -> value.contentToString()
            is android.content.Intent -> "Intent(action=${value.action})"
            else -> value.toString()
        }
    }
}
