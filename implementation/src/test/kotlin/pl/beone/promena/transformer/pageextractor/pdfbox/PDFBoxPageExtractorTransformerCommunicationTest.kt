package pl.beone.promena.transformer.pageextractor.pdfbox

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters

@ExtendWith(DockerExtension::class)
class PDFBoxPageExtractorTransformerCommunicationTest {

    @Test
    fun transform_memoryData() {
        memoryTest(
            PDFBoxPageExtractorTransformerSettings(),
            PDFBoxPageExtractorTransformerDefaultParameters(),
            pdfBoxPageExtractorParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }

    @Test
    fun file_memoryData() {
        fileTest(
            PDFBoxPageExtractorTransformerSettings(),
            PDFBoxPageExtractorTransformerDefaultParameters(true),
            pdfBoxPageExtractorParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }
}