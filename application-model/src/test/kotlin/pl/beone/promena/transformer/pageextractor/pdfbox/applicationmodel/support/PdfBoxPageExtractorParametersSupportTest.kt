package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.support

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotThrow
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorSupport.ParametersSupport.isSupported
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters

class PdfBoxPageExtractorParametersSupportTest {

    @Test
    fun `isSupported _ default parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractorParameters(pages = listOf(1, 2, 3)))
        }
    }

    @Test
    fun `isSupported _ all parameters`() {
        shouldNotThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractorParameters(pages = listOf(1, 2, 3), relaxed = true))
        }
    }

    @Test
    fun `isSupported _ empty list of pages _ all parameters`() {
        shouldThrow<TransformationNotSupportedException> {
            isSupported(pdfBoxPageExtractorParameters(pages = emptyList()))
        }.message shouldBe "Parameter <pages> must contain at least <1> page"
    }
}