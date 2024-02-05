package com.nasdroid.core.markdown

object MarkdownNodeBuilders {
    fun markdownUnorderedList(vararg listItems: MarkdownParagraph): MarkdownUnorderedList {
        return MarkdownUnorderedList(listItems.toList())
    }

    fun markdownOrderedList(vararg listItems: MarkdownParagraph): MarkdownOrderedList {
        return MarkdownOrderedList(listItems.toList())
    }

    fun markdownTable(
        vararg columns: MarkdownTable.Column
    ): MarkdownTable {
        return MarkdownTable(columns.toList())
    }

    fun markdownTableColumn(
        header: MarkdownParagraph,
        alignment: MarkdownTable.Alignment = MarkdownTable.Alignment.LEFT,
        vararg cells: MarkdownParagraph
    ): MarkdownTable.Column {
        return MarkdownTable.Column(
            header = header,
            alignment = alignment,
            cells = cells.toList()
        )
    }

    fun markdownImage(
        contentDescription: String,
        imageUrl: String,
        titleText: String? = null
    ): MarkdownImage {
        return MarkdownImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            titleText = titleText
        )
    }

    fun markdownCodeBlock(
        text: String,
        language: String? = null
    ): MarkdownCodeBlock {
        return MarkdownCodeBlock(
            code = text,
            language = language
        )
    }

    fun markdownBlockQuote(
        vararg children: MarkdownNode
    ): MarkdownBlockQuote {
        return MarkdownBlockQuote(
            children = children.toList()
        )
    }

    fun markdownParagraph(
        vararg children: MarkdownSpanNode
    ): MarkdownParagraph {
        return MarkdownParagraph(
            children = children.toList()
        )
    }

    fun markdownCodeSpan(
        text: String
    ): MarkdownCodeSpan {
        return MarkdownCodeSpan(text)
    }

    fun markdownText(
        text: String,
        isBold: Boolean = false,
        isItalics: Boolean = false,
        isStrikethrough: Boolean = false,
    ): MarkdownText {
        return MarkdownText(
            text = text,
            isBold = isBold,
            isItalics = isItalics,
            isStrikethrough = isStrikethrough
        )
    }

    fun markdownLink(
        link: String,
        text: List<MarkdownText> = listOf(markdownText(link)),
        altText: String? = null
    ): MarkdownLink {
        return MarkdownLink(
            displayText = text,
            url = link,
            titleText = altText
        )
    }

    fun markdownHeader(
        size: MarkdownHeading.Size,
        vararg children: MarkdownSpanNode
    ): MarkdownHeading {
        return MarkdownHeading(
            children = children.toList(),
            size = size
        )
    }
}
