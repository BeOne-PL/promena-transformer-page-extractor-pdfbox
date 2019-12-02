package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test

class PdfBoxPageExtractorParametersDslTest {

    companion object {
        private val pages = listOf(listOf(1, 2), listOf(3, 4))
        private const val splitByBarcodeMetadata = true
    }

    @Test
    fun `pdfBoxPageExtractionParameters _ default parameters`() {
        with(pdfBoxPageExtractorParameters()) {
            shouldThrow<NoSuchElementException> { getPages() }
            getPagesOrNull() shouldBe null
            getPagesOrDefault(pages) shouldBe pages

            shouldThrow<NoSuchElementException> { getSplitByBarcodeMetadata() }
            getSplitByBarcodeMetadataOrNull() shouldBe null
            getSplitByBarcodeMetadataOrDefault(splitByBarcodeMetadata) shouldBe splitByBarcodeMetadata
        }
    }

    @Test
    fun `pdfBoxPageExtractionParameters _ all parameters`() {
        with(pdfBoxPageExtractorParameters(pages = pages, splitByBarcodeMetadata = splitByBarcodeMetadata)) {
            getPages() shouldBe pages
            getPagesOrNull() shouldBe pages
            getPagesOrDefault(pages) shouldBe pages

            getSplitByBarcodeMetadata() shouldBe splitByBarcodeMetadata
            getSplitByBarcodeMetadataOrNull() shouldBe splitByBarcodeMetadata
            getSplitByBarcodeMetadataOrDefault(splitByBarcodeMetadata) shouldBe splitByBarcodeMetadata
        }
    }
}