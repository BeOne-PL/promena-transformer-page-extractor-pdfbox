package pl.beone.promena.transformer.pageextraction.pdfbox

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit5.extension.docker.external.DockerExtension
import pl.beone.promena.transformer.internal.model.parameters.addTimeout
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters
import java.time.Duration
import java.util.concurrent.TimeoutException

@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractionTransformerTest {

    @Test
    fun transform_relaxedAndMainMemoryUsageSetting_2and4Pages() {
        memoryTest(
            createSettings(MemoryUsageSetting.setupMainMemoryOnly()),
            createDefaultParameters(true),
            pdfBoxPageExtractionParameters(pages = listOf(2, 4)),
            2,
            listOf("Page 2", "Page 4")
        )
    }

    @Test
    fun transform_relaxedAndMainMemoryUsageSetting_1PageAndPageOutOfRange() {
        memoryTest(
            createSettings(MemoryUsageSetting.setupMainMemoryOnly()),
            createDefaultParameters(true),
            pdfBoxPageExtractionParameters(pages = listOf(1, 999)),
            1,
            listOf("Page 1")
        )
    }

    // ***

    @Test
    fun transform_notRelaxedAndMainMemoryUsageSetting_1PageAndPageOutOfRange_shouldThrowIllegalArgumentException() {
        shouldThrow<IllegalArgumentException> {
            memoryTest(
                createSettings(MemoryUsageSetting.setupMainMemoryOnly()),
                createDefaultParameters(false),
                pdfBoxPageExtractionParameters(pages = listOf(1, 999))
            )
        }.message shouldBe "Document hasn't <999> pages"
    }

    // ***

    @Test
    fun transform_relaxedAndTempFileMemoryUsageSetting_1Page() {
        memoryTest(
            createSettings(MemoryUsageSetting.setupTempFileOnly()),
            createDefaultParameters(true),
            pdfBoxPageExtractionParameters(pages = listOf(1)),
            1,
            listOf("Page 1")
        )
    }

    // ***

    @Test
    fun transform_zeroTimeout_shouldThrowTimeoutException() {
        shouldThrow<TimeoutException> {
            memoryTest(
                createSettings(MemoryUsageSetting.setupTempFileOnly()),
                createDefaultParameters(true, Duration.ZERO),
                pdfBoxPageExtractionParameters(pages = listOf(1))
            )
        }

        shouldThrow<TimeoutException> {
            memoryTest(
                createSettings(MemoryUsageSetting.setupTempFileOnly()),
                createDefaultParameters(true, null),
                pdfBoxPageExtractionParameters(pages = listOf(1)) addTimeout Duration.ZERO
            )
        }
    }
}