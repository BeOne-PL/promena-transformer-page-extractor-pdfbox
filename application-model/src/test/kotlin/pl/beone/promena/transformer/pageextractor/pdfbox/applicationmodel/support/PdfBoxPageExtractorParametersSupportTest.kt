package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.support

import io.kotlintest.shouldNotThrow
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorSupport.ParametersSupport.isSupported
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters

class PdfBoxPageExtractorParametersSupportTest {

    @Test
    fun `isSupported _ default parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractorParameters())
        }
    }

    @Test
    fun `isSupported _ all parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractorParameters(pages = listOf(listOf(1, 2, 3)), splitByBarcodeMetadata = true))
        }
    }
}