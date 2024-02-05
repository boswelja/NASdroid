package com.nasdroid.core.markdown

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownNodeGeneratorTest {

    companion object {
        private val ITALICS_PATTERNS = mapOf(
            "*asterisks*" to produceMarkdownTextNode(text = "asterisks", isItalics = true),
            "_underscores_" to produceMarkdownTextNode(text = "underscores", isItalics = true),
            "*multi word asterisks*" to produceMarkdownTextNode(text = "multi word asterisks", isItalics = true),
            "_multi word underscores_" to produceMarkdownTextNode(text = "multi word underscores", isItalics = true),
        )

        private val BOLD_PATTERNS = mapOf(
            "**asterisks**" to produceMarkdownTextNode(text = "asterisks", isBold = true),
            "__underscores__" to produceMarkdownTextNode(text = "underscores", isBold = true),
            "**multi word asterisks**" to produceMarkdownTextNode(text = "multi word asterisks", isBold = true),
            "__multi word underscores__" to produceMarkdownTextNode(text = "multi word underscores", isBold = true),
        )

        private val STRIKETHROUGH_PATTERNS = mapOf(
            "~~strikethrough~~" to produceMarkdownTextNode(text = "strikethrough", isStrikethrough = true),
            "~~multi word strikethrough~~" to
                    produceMarkdownTextNode(text = "multi word strikethrough", isStrikethrough = true)
        )

        private val MONOSPACE_PATTERNS = mapOf(
            "`monospace`" to produceMarkdownCodeSpan("monospace"),
            "` `" to produceMarkdownCodeSpan(" "),
            "`multi word monospace`" to produceMarkdownCodeSpan("multi word monospace")
        )

        private val RULE_PATTERNS = mapOf(
            "---" to MarkdownRule
        )

        private val CODEBLOCK_PATTERNS = mapOf(
            """
                ```kotlin
                val codeBlockTest = true
                ```
            """.trimIndent() to produceMarkdownCodeBlock(text = "val codeBlockTest = true", language = "kotlin"),
            """
                ```kotlin
                val codeBlockTest = true
                val secondLine = true
                ```
            """.trimIndent() to produceMarkdownCodeBlock(
                text = "val codeBlockTest = true\nval secondLine = true",
                language = "kotlin"
            ),
            """
                ```kotlin
                val codeBlockTest = true
                
                val secondLine = true
                ```
            """.trimIndent() to produceMarkdownCodeBlock(
                text = "val codeBlockTest = true\n\nval secondLine = true",
                language = "kotlin"
            ),
        )

        private val BLOCKQUOTE_PATTERNS = mapOf(
            "> this is a block quote" to produceMarkdownBlockQuote(
                produceMarkdownParagraphNode(
                    produceMarkdownTextNode("this is a block quote")
                )
            ),
            """
                > > What on earth
                >
                > > Is this
                >
                > # Heading
            """.trimIndent() to produceMarkdownBlockQuote(
                produceMarkdownBlockQuote(
                    produceMarkdownParagraphNode(produceMarkdownTextNode("What on earth"))
                ),
                MarkdownEol,
                MarkdownWhitespace,
                MarkdownEol,
                MarkdownWhitespace,
                produceMarkdownBlockQuote(
                    produceMarkdownParagraphNode(produceMarkdownTextNode("Is this"))
                ),
                MarkdownEol,
                MarkdownWhitespace,
                MarkdownEol,
                MarkdownWhitespace,
                produceMarkdownHeaderNode(MarkdownHeading.Size.Headline1, produceMarkdownTextNode("Heading")),
            )
        )

        private val PARAGRAPH_PATTERNS = mapOf(
            "**bold** _italics_ ~~strikethrough~~ [Google](https://google.com)" to produceMarkdownParagraphNode(
                produceMarkdownTextNode("bold", isBold = true),
                MarkdownWhitespace,
                produceMarkdownTextNode("italics", isItalics = true),
                MarkdownWhitespace,
                produceMarkdownTextNode("strikethrough", isStrikethrough = true),
                MarkdownWhitespace,
                produceMarkdownUrlNode("https://google.com", listOf(produceMarkdownTextNode("Google")))
            ),
            """
                **bold** _italics_
                [Google](https://google.com)
                This is a single newline test
            """.trimIndent() to produceMarkdownParagraphNode(
                produceMarkdownTextNode("bold", isBold = true),
                MarkdownWhitespace,
                produceMarkdownTextNode("italics", isItalics = true),
                MarkdownEol,
                produceMarkdownUrlNode("https://google.com", listOf(produceMarkdownTextNode("Google"))),
                MarkdownEol,
                produceMarkdownTextNode("This is a single newline test")
            ),
            "un*frigging*believable" to produceMarkdownParagraphNode(
                produceMarkdownTextNode("un"),
                produceMarkdownTextNode("frigging", isItalics = true),
                produceMarkdownTextNode("believable")
            )
        )

        private val LINK_PATTERNS = mapOf(
            "https://google.com" to produceMarkdownUrlNode("https://google.com"),
            "<https://google.com>" to produceMarkdownUrlNode("https://google.com"),
            "[Google](https://google.com)" to produceMarkdownUrlNode(
                url = "https://google.com",
                text = listOf(produceMarkdownTextNode("Google"))
            ),
            "[*Google*](https://google.com)" to produceMarkdownUrlNode(
                url = "https://google.com",
                text = listOf(produceMarkdownTextNode("Google", isItalics = true))
            ),
            "[**Google**](https://google.com)" to produceMarkdownUrlNode(
                url = "https://google.com",
                text = listOf(produceMarkdownTextNode("Google", isBold = true))
            ),
            "[Google](https://google.com \"Google\")" to produceMarkdownUrlNode(
                url = "https://google.com",
                text = listOf(produceMarkdownTextNode("Google")),
                altText = "Google"
            ),
        )

        private val PLAINTEXT_PATTERNS = mapOf(
            "text" to produceMarkdownTextNode(text = "text"),
            "one two three" to produceMarkdownTextNode("one two three")
        )

        private val HEADER_PATTERNS = mapOf(
            """
                H1
                ===
            """.trimIndent() to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline1, produceMarkdownTextNode("H1")),
            "# H1" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline1, produceMarkdownTextNode("H1")),
            """
                H2
                ---
            """.trimIndent() to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline2, produceMarkdownTextNode("H2")),
            "## H2" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline2, produceMarkdownTextNode("H2")),
            "### H3" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline3, produceMarkdownTextNode("H3")),
            "#### H4" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline4, produceMarkdownTextNode("H4")),
            "##### H5" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline5, produceMarkdownTextNode("H5")),
            "###### H6" to produceMarkdownHeaderNode(MarkdownHeading.Size.Headline6, produceMarkdownTextNode("H6")),
        )

        private val IMAGE_PATTERNS = mapOf(
            "![alt text](https://test.image)" to produceMarkdownImage("alt text", "https://test.image"),
            "![alt text](https://test.image \"Test image\")" to produceMarkdownImage("alt text", "https://test.image", "Test image")
        )

        private fun produceMarkdownAstNode(markdown: String): ASTNode {
            val flavor = GFMFlavourDescriptor()
            val tree = MarkdownParser(flavor).buildMarkdownTreeFromString(markdown)
            return tree
        }

        private fun produceMarkdownImage(
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

        private fun produceMarkdownCodeBlock(
            text: String,
            language: String? = null
        ): MarkdownCodeBlock {
            return MarkdownCodeBlock(
                code = text,
                language = language
            )
        }

        private fun produceMarkdownBlockQuote(
            vararg children: MarkdownNode
        ): MarkdownBlockQuote {
            return MarkdownBlockQuote(
                children = children.toList()
            )
        }
        private fun produceMarkdownParagraphNode(
            vararg children: MarkdownSpanNode
        ): MarkdownParagraph {
            return MarkdownParagraph(
                children = children.toList()
            )
        }

        private fun produceMarkdownCodeSpan(
            text: String
        ): MarkdownCodeSpan {
            return MarkdownCodeSpan(text)
        }

        private fun produceMarkdownTextNode(
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

        private fun produceMarkdownUrlNode(
            url: String,
            text: List<MarkdownText> = listOf(produceMarkdownTextNode(url)),
            altText: String? = null
        ): MarkdownLink {
            return MarkdownLink(
                displayText = text,
                url = url,
                titleText = altText
            )
        }

        private fun produceMarkdownHeaderNode(
            size: MarkdownHeading.Size,
            vararg children: MarkdownSpanNode
        ): MarkdownHeading {
            return MarkdownHeading(
                children = children.toList(),
                size = size
            )
        }
    }

    private fun testParagraphParsing(markdown: String, expectedParagraph: MarkdownParagraph) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedParagraph), actual,
            "Matching failed for input $markdown"
        )
    }

    private fun testBlockquoteParsing(markdown: String, expectedParagraph: MarkdownBlockQuote) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedParagraph), actual,
            "Matching failed for input $markdown"
        )
    }

    private fun testTextParsing(markdown: String, expectedText: MarkdownSpanNode) {
        testParagraphParsing(markdown, MarkdownParagraph(listOf(expectedText)))
    }

    private fun testHeaderParsing(markdown: String, expectedHeader: MarkdownHeading) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedHeader), actual,
            "Matching failed for input $markdown"
        )
    }

    private fun testRuleParsing(markdown: String, expectedRule: MarkdownRule) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedRule), actual,
            "Matching failed for input $markdown"
        )
    }

    private fun testCodeBlockParsing(markdown: String, expectedCodeBlock: MarkdownCodeBlock) {
        val generator = MarkdownNodeGenerator(markdown, produceMarkdownAstNode(markdown))
        val actual = generator.generateNodes()
        assertEquals(
            listOf(expectedCodeBlock), actual,
            "Matching failed for input $markdown"
        )
    }

    private fun testImageParsing(markdown: String, expectedImage: MarkdownImage) {
        testParagraphParsing(markdown, MarkdownParagraph(listOf(expectedImage)))
    }

    @Test
    fun `plaintext parsing`() {
        PLAINTEXT_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }
    @Test
    fun `strikethrough parsing`() {
        STRIKETHROUGH_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }

    @Test
    fun `bold parsing`() {
        BOLD_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }

    @Test
    fun `italics parsing`() {
        ITALICS_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }

    @Test
    fun `header parsing`() {
        HEADER_PATTERNS.forEach { (markdown, expected) ->
            testHeaderParsing(markdown, expected)
        }
    }

    @Test
    fun `link parsing`() {
        LINK_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }

    @Test
    fun `paragraph parsing`() {
        PARAGRAPH_PATTERNS.forEach { (markdown, expected) ->
            testParagraphParsing(markdown, expected)
        }
    }

    @Test
    fun `image parsing`() {
        IMAGE_PATTERNS.forEach { (markdown, expected) ->
            testImageParsing(markdown, expected)
        }
    }

    @Test
    fun `rule parsing`() {
        RULE_PATTERNS.forEach { (markdown, expected) ->
            testRuleParsing(markdown, expected)
        }
    }

    @Test
    fun `blockquote parsing`() {
        BLOCKQUOTE_PATTERNS.forEach { (markdown, expected) ->
            testBlockquoteParsing(markdown, expected)
        }
    }

    @Test
    fun `code span parsing`() {
        MONOSPACE_PATTERNS.forEach { (markdown, expected) ->
            testTextParsing(markdown, expected)
        }
    }

    @Test
    fun `code block parsing`() {
        CODEBLOCK_PATTERNS.forEach { (markdown, expected) ->
            testCodeBlockParsing(markdown, expected)
        }
    }
}