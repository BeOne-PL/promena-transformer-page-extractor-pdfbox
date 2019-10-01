package pl.beone.promena.transformer.pageextraction.pdfbox

import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit5.extension.docker.external.DockerExtension
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters

@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractionTransformerCommunicationTest {

    @Test
    fun transform_memoryData() {
        memoryTest(
            createSettings(MemoryUsageSetting.setupMainMemoryOnly()),
            createDefaultParameters(true),
            pdfBoxPageExtractionParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }

    @Test
    fun file_memoryData() {
        fileTest(
            createSettings(MemoryUsageSetting.setupMainMemoryOnly()),
            createDefaultParameters(true),
            pdfBoxPageExtractionParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }
}