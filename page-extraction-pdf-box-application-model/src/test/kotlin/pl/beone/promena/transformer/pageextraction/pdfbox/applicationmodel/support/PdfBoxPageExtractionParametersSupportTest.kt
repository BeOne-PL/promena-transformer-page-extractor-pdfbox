package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.support

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionSupport.ParametersSupport.isSupported
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters

class PdfBoxPageExtractionParametersSupportTest {

    @Test
    fun `isSupported _ default parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractionParameters(pages = listOf(1, 2, 3)))
        }
    }

    @Test
    fun `isSupported _ all parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractionParameters(pages = listOf(1, 2, 3), relaxed = true))
        }
    }

    @Test
    fun `isSupported _ empty list of pages _ all parameters`() {
        shouldThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractionParameters(pages = emptyList()))
        }.message shouldBe "Parameter <pages> must contain at least <1> page"
    }
}