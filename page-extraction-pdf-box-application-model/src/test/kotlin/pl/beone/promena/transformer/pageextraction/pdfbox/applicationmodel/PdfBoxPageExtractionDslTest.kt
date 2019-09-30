package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionConstants.TRANSFORMER_ID

class PdfBoxPageExtractionDslTest {

    @Test
    fun pdfBoxPageExtractionTransformation() {
        pdfBoxPageExtractionTransformation(pdfBoxPageExtractionParameters(pages = listOf(1, 2, 3))).let {
            it.transformerId shouldBe TRANSFORMER_ID
            it.targetMediaType shouldBe APPLICATION_PDF
            it.parameters.getAll().size shouldBe 1
        }
    }
}