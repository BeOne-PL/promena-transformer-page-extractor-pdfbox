package pl.beone.promena.transformer.pageextraction.pdfbox

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit5.extension.docker.external.DockerExtension
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionSupport

@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractionTransformerSupportTest {

    @BeforeEach
    fun setUp() {
        mockkObject(PdfBoxPageExtractionSupport)
    }

    @Test
    fun isSupported() {
        val dataDescriptor = mockk<DataDescriptor>()
        val targetMediaType = mockk<MediaType>()
        val parameters = mockk<Parameters>()

        every { PdfBoxPageExtractionSupport.isSupported(dataDescriptor, targetMediaType, parameters) } just Runs

        PdfBoxPageExtractionTransformer(mockk())
            .isSupported(dataDescriptor, targetMediaType, parameters)

        verify(exactly = 1) { PdfBoxPageExtractionSupport.isSupported(dataDescriptor, targetMediaType, parameters) }
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(PdfBoxPageExtractionSupport)
    }
}