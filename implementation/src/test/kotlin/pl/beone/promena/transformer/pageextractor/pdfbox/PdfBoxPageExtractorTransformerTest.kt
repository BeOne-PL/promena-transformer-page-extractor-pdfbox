package pl.beone.promena.transformer.pageextractor.pdfbox

import io.kotlintest.shouldThrow
import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.barcodedetector.metadata.BarcodeDetectorMetadataBuilder
import pl.beone.promena.transformer.barcodedetector.metadata.BarcodeDetectorMetadataBuilder.BarcodeBuilder
import pl.beone.promena.transformer.internal.model.metadata.emptyMetadata
import pl.beone.promena.transformer.internal.model.parameters.addTimeout
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.util.TextWithMetadataAssert
import pl.beone.promena.transformer.pageextractor.pdfbox.util.createPdfBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.util.test
import java.time.Duration
import java.util.concurrent.TimeoutException

@ExtendWith(DockerExtension::class)
class PdfBoxPageExtractorTransformerTest {

    @Test
    fun transform_mainMemoryUsageSetting_2and4Pages_shouldExtractPagesUsingPagesParameterBecauseItHasHigherPriority() {
        test(
            pdfBoxPageExtractorParameters(pages = listOf(listOf(2, 4)), splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(listOf("Page 2", "Page 4"))
            )
        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_2and4Pages_3and4Pages() {
        test(
            pdfBoxPageExtractorParameters(pages = listOf(listOf(2, 4), listOf(3, 4))),
            listOf(
                TextWithMetadataAssert(listOf("Page 2", "Page 4")),
                TextWithMetadataAssert(listOf("Page 3", "Page 4"))
            )
        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_1PageAndPageOutOfRange() {
        test(
            pdfBoxPageExtractorParameters(pages = listOf(listOf(1, 999))),
            listOf(
                TextWithMetadataAssert(listOf("Page 1"))
            )
        )
    }

    // ***


    @Test
    fun transform_tempFileMemoryUsageSetting_1Page() {
        test(
            pdfBoxPageExtractorParameters(pages = listOf(listOf(1))),
            listOf(
                TextWithMetadataAssert(listOf("Page 1"))
            ),
            transformer = createPdfBoxPageExtractorTransformer(
                settings = PdfBoxPageExtractorTransformerSettings(memoryUsageSetting = MemoryUsageSetting.setupTempFileOnly())
            )
        )
    }

    // ***

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata() {
        val barcodeMetadata = BarcodeBuilder().text("1").page(1).build()
        val barcodeMetadata2 = BarcodeBuilder().text("2").build()
        val barcodeMetadata3 = BarcodeBuilder().text("3").page(4).build()
        val barcodeMetadata4 = BarcodeBuilder().text("4").page(4).build()
        val barcodeMetadata5 = BarcodeBuilder().text("5").page(5).build()
        val barcodeMetadata6 = BarcodeBuilder().text("6").page(10).build()
        val barcodeMetadata7 = BarcodeBuilder().text("7").page(10).build()

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1", "Page 2", "Page 3"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).barcode(barcodeMetadata2).build()
                ),
                TextWithMetadataAssert(
                    listOf("Page 4"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata3).barcode(barcodeMetadata4).build()
                ),
                TextWithMetadataAssert(
                    listOf("Page 5", "Page 6", "Page 7", "Page 8", "Page 9"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata5).build()
                ),
                TextWithMetadataAssert(
                    listOf("Page 10"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata6).barcode(barcodeMetadata7).build()
                )
            ),
            BarcodeDetectorMetadataBuilder()
                .barcode(barcodeMetadata)
                .barcode(barcodeMetadata2)
                .barcode(barcodeMetadata3)
                .barcode(barcodeMetadata4)
                .barcode(barcodeMetadata5)
                .barcode(barcodeMetadata6)
                .barcode(barcodeMetadata7)
                .build()

        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_barcodeMetadataWithoutFirstPage() {
        val barcodeMetadata = BarcodeBuilder().text("1").page(2).build()

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    BarcodeDetectorMetadataBuilder().build()
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).build()
                )
            ),
            BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).build()
        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_barcodeMetadataWithoutFirstPageAndWithElementWithoutPage() {
        val barcodeMetadata = BarcodeBuilder().text("1").page(2).build()
        val barcodeMetadata2 = BarcodeBuilder().text("2").build()

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata2).build()
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).build()
                )
            ),
            BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).barcode(barcodeMetadata2).build()
        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_noBarcodeMetadata() {
        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"))
            ),
            emptyMetadata()
        )
    }

    // ***

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_defaultParameterIsTrue() {
        val barcodeMetadata = BarcodeBuilder().text("1").page(2).build()

        test(
            pdfBoxPageExtractorParameters(),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    emptyMetadata()
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).build()
                )
            ),
            BarcodeDetectorMetadataBuilder().barcode(barcodeMetadata).build()
        )
    }

    // ***

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_defaultParameterIsFalse() {
        val barcodeDetectorMetadata = BarcodeDetectorMetadataBuilder()
            .barcode(BarcodeBuilder().text("1").page(2).build())
            .build()

        test(
            pdfBoxPageExtractorParameters(),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    barcodeDetectorMetadata
                )
            ),
            barcodeDetectorMetadata,
            createPdfBoxPageExtractorTransformer(defaultParameters = PdfBoxPageExtractorTransformerDefaultParameters(splitByBarcodeMetadata = false))
        )
    }

    // ***

    @Test
    fun transform_zeroTimeout_shouldThrowTimeoutException() {
        shouldThrow<TimeoutException> {
            test(pdfBoxPageExtractorParameters(pages = listOf(listOf(1))) addTimeout Duration.ZERO)
        }
    }
}