package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.support

import io.kotlintest.shouldNotThrow
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionSupport.ParametersSupport.isSupported
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters

class PdfBoxPageExtractionParametersSupportTest {

    @Test
    fun `isSupported _ all parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractionParameters(pages = listOf(1, 2, 3)))
        }
    }
}