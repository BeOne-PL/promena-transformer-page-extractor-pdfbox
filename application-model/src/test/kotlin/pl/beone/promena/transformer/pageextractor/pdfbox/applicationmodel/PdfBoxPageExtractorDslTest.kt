package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorConstants.TRANSFORMER_ID

class PdfBoxPageExtractorDslTest {

    @Test
    fun pdfBoxPageExtractorTransformation() {
        with(pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters())) {
            transformerId shouldBe TRANSFORMER_ID
            targetMediaType shouldBe APPLICATION_PDF
            parameters.getAll().size shouldBe 0
        }
    }
}