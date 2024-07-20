package com.nasdroid.apps.logic.discover

/**
 * Strips all header text, and other HTML tags from a String. See [invoke] for details.
 */
class StripHtmlTags {

    /**
     * Processes [text] such that all HTML headers (h1-h6) are removed, and general HTML tags are
     * stripped.
     */
    operator fun invoke(text: String): String {
        return text
            .replace(Regex("<h[1-6]>.*</h[16]>"), "") // Remove headers
            .replace(Regex("<[^>]*>"), "") // Remove HTML tags
            .trim() // Remove any leading/trailing whitespace
    }
}
