package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test

class PdfBoxPageExtractorParametersDslKtTest {

    @Test
    fun `pdfBoxPageExtractionParameters _ default parameters`() {
        val pages = listOf(1, 2, 3)
        pdfBoxPageExtractorParameters(pages = pages).let {
            it.getPages() shouldBe pages
            shouldThrow<NoSuchElementException> {
                it.getRelaxed()
            }
            it.getRelaxedOrDefault(true) shouldBe true
        }
    }

    @Test
    fun `pdfBoxPageExtractionParameters _ all parameters`() {
        val pages = listOf(1, 2, 3)
        pdfBoxPageExtractorParameters(pages = pages, relaxed = true).let {
            it.getPages() shouldBe pages
            it.getRelaxed() shouldBe true
            it.getRelaxedOrDefault(false) shouldBe true
        }
    }
}