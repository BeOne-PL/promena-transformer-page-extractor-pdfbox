package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

class PdfBoxPageExtractionParametersDslTest {

    @Test
    fun `pdfBoxPageExtractionParameters _ all parameters`() {
        val pages = listOf(1, 2, 3)
        pdfBoxPageExtractionParameters(pages = pages).getPages() shouldBe pages
    }
}