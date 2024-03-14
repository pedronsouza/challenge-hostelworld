package com.pedronsouza.shared.parsers

import android.text.Html
import com.pedronsouza.domain.ContentParser
import com.pedronsouza.domain.values.HtmlContent

internal class HtmlToTexContentParser : ContentParser<HtmlContent, String> {
    override fun parse(value: HtmlContent): String =
        Html.fromHtml(value.toString(), Html.FROM_HTML_MODE_COMPACT).toString()
}