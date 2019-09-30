package pl.beone.promena.transformer.pageextraction.pdfbox

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit5.extension.docker.external.DockerExtension
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters

//@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractionTransformerTest {

    @Test
    fun transform() {
        memoryTest(pdfBoxPageExtractionParameters(pages = listOf(2, 4)), 2, listOf("Page 2", "Page 4"))
    }
}