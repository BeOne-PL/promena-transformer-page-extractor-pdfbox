package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PDFBoxPageExtractorConstants.TRANSFORMER_ID

class PDFBoxPageExtractorDslTest {

    @Test
    fun pdfBoxPageExtractorTransformation() {
        pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(1, 2, 3))).let {
            it.transformerId shouldBe TRANSFORMER_ID
            it.targetMediaType shouldBe APPLICATION_PDF
            it.parameters.getAll().size shouldBe 1
        }
    }
}