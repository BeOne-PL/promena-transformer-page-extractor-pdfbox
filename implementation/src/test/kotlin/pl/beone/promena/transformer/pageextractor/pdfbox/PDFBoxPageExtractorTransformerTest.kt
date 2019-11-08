package pl.beone.promena.transformer.pageextractor.pdfbox

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.internal.model.parameters.addTimeout
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters
import java.time.Duration
import java.util.concurrent.TimeoutException

@ExtendWith(DockerExtension::class)
class PDFBoxPageExtractorTransformerTest {

    @Test
    fun transform_relaxedAndMainMemoryUsageSetting_2and4Pages() {
        test(
            PDFBoxPageExtractorTransformerSettings(),
            PDFBoxPageExtractorTransformerDefaultParameters(),
            pdfBoxPageExtractorParameters(pages = listOf(2, 4)),
            2,
            listOf("Page 2", "Page 4")
        )
    }

    @Test
    fun transform_relaxedAndMainMemoryUsageSetting_1PageAndPageOutOfRange() {
        test(
            PDFBoxPageExtractorTransformerSettings(),
            PDFBoxPageExtractorTransformerDefaultParameters(),
            pdfBoxPageExtractorParameters(pages = listOf(1, 999)),
            1,
            listOf("Page 1")
        )
    }

    // ***

    @Test
    fun transform_notRelaxedAndMainMemoryUsageSetting_1PageAndPageOutOfRange_shouldThrowIllegalArgumentException() {
        shouldThrow<IllegalArgumentException> {
            test(
                PDFBoxPageExtractorTransformerSettings(),
                PDFBoxPageExtractorTransformerDefaultParameters(relaxed = false),
                pdfBoxPageExtractorParameters(pages = listOf(1, 999))
            )
        }.message shouldBe "Document hasn't <999> pages"
    }

    // ***

    @Test
    fun transform_relaxedAndTempFileMemoryUsageSetting_1Page() {
        test(
            PDFBoxPageExtractorTransformerSettings(memoryUsageSetting = MemoryUsageSetting.setupTempFileOnly()),
            PDFBoxPageExtractorTransformerDefaultParameters(),
            pdfBoxPageExtractorParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }

    // ***

    @Test
    fun transform_zeroTimeout_shouldThrowTimeoutException() {
        shouldThrow<TimeoutException> {
            test(
                PDFBoxPageExtractorTransformerSettings(memoryUsageSetting = MemoryUsageSetting.setupTempFileOnly()),
                PDFBoxPageExtractorTransformerDefaultParameters(timeout = Duration.ZERO),
                pdfBoxPageExtractorParameters(pages = listOf(1))
            )
        }

        shouldThrow<TimeoutException> {
            test(
                PDFBoxPageExtractorTransformerSettings(),
                PDFBoxPageExtractorTransformerDefaultParameters(),
                pdfBoxPageExtractorParameters(pages = listOf(1)) addTimeout Duration.ZERO
            )
        }
    }
}