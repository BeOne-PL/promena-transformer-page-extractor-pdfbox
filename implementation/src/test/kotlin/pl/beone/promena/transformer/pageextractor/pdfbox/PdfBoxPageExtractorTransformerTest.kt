package pl.beone.promena.transformer.pageextractor.pdfbox

import io.kotlintest.shouldThrow
import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.beone.lib.junit.jupiter.external.DockerExtension
import pl.beone.promena.transformer.barcodedetector.metadata.*
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
        val metadataBarcode = barcode() addText "1" addPage 1
        val metadataBarcode2 = barcode() addText "2"
        val metadataBarcode3 = barcode() addText "3" addPage 4
        val metadataBarcode4 = barcode() addText "4" addPage 4
        val metadataBarcode5 = barcode() addText "5" addPage 5
        val metadataBarcode6 = barcode() addText "6" addPage 10
        val metadataBarcode7 = barcode() addText "7" addPage 10

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1", "Page 2", "Page 3"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode addBarcode metadataBarcode2
                ),
                TextWithMetadataAssert(
                    listOf("Page 4"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode3 addBarcode metadataBarcode4
                ),
                TextWithMetadataAssert(
                    listOf("Page 5", "Page 6", "Page 7", "Page 8", "Page 9"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode5
                ),
                TextWithMetadataAssert(
                    listOf("Page 10"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode6 addBarcode metadataBarcode7
                )
            ),
            barcodeDetectorMetadata() addBarcode
                    metadataBarcode addBarcode
                    metadataBarcode2 addBarcode
                    metadataBarcode3 addBarcode
                    metadataBarcode4 addBarcode
                    metadataBarcode5 addBarcode
                    metadataBarcode6 addBarcode
                    metadataBarcode7

        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_barcodeMetadataWithoutFirstPage() {
        val metadataBarcode = barcode() addText "1" addPage 2

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    barcodeDetectorMetadata()
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode
                )
            ),
            barcodeDetectorMetadata() addBarcode metadataBarcode
        )
    }

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_barcodeMetadataWithoutFirstPageAndWithElementWithoutPage() {
        val metadataBarcode = barcode() addText "1" addPage 2
        val metadataBarcode2 = barcode() addText "2"

        test(
            pdfBoxPageExtractorParameters(splitByBarcodeMetadata = true),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode2
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode
                )
            ),
            barcodeDetectorMetadata() addBarcode metadataBarcode addBarcode metadataBarcode2
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
        val metadataBarcode = barcode() addText "1" addPage 2

        test(
            pdfBoxPageExtractorParameters(),
            listOf(
                TextWithMetadataAssert(
                    listOf("Page 1"),
                    barcodeDetectorMetadata()
                ),
                TextWithMetadataAssert(
                    listOf("Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7", "Page 8", "Page 9", "Page 10"),
                    barcodeDetectorMetadata() addBarcode metadataBarcode
                )
            ),
            barcodeDetectorMetadata() addBarcode metadataBarcode
        )
    }

    // ***

    @Test
    fun transform_mainMemoryUsageSetting_splitByBarcodeMetadata_defaultParameterIsFalse() {
        val barcodeDetectorMetadata = barcodeDetectorMetadata() addBarcode (barcode() addText "1" addPage 2)

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