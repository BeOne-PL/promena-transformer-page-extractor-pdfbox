package pl.beone.promena.transformer.pageextractor.pdfbox

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PDFBoxPageExtractorSupport

@ExtendWith(DockerExtension::class)
class PDFBoxPageExtractorTransformerSupportTest {

    @BeforeEach
    fun setUp() {
        mockkObject(PDFBoxPageExtractorSupport)
    }

    @Test
    fun isSupported() {
        val dataDescriptor = mockk<DataDescriptor>()
        val targetMediaType = mockk<MediaType>()
        val parameters = mockk<Parameters>()

        every { PDFBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters) } just Runs

        PDFBoxPageExtractorTransformer(mockk(), mockk(), mockk())
            .isSupported(dataDescriptor, targetMediaType, parameters)

        verify(exactly = 1) { PDFBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters) }
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(PDFBoxPageExtractorSupport)
    }
}