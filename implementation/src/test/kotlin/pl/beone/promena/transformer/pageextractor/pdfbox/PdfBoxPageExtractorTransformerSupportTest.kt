package pl.beone.promena.transformer.pageextractor.pdfbox

import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorSupport
import pl.beone.promena.transformer.pageextractor.pdfbox.util.createPdfBoxPageExtractorTransformer

@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractorTransformerSupportTest {

    @Test
    fun isSupported() {
        val dataDescriptor = mockk<DataDescriptor>()
        val targetMediaType = mockk<MediaType>()
        val parameters = mockk<Parameters>()

        mockkStatic(PdfBoxPageExtractorSupport::class)
        every { PdfBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters) } just Runs

        createPdfBoxPageExtractorTransformer()
            .isSupported(dataDescriptor, targetMediaType, parameters)

        verify(exactly = 1) { PdfBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters) }
    }
}