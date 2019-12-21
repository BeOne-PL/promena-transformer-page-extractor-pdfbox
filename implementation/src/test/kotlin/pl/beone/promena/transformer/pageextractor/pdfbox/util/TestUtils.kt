package pl.beone.promena.transformer.pageextractor.pdfbox.util

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.withClue
import io.kotlintest.shouldBe
import io.mockk.mockk
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.barcodedetector.metadata.BarcodeDetectorMetadataGetter
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.contract.data.singleDataDescriptor
import pl.beone.promena.transformer.contract.model.Metadata
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.contract.model.data.WritableData
import pl.beone.promena.transformer.internal.model.data.memory.emptyMemoryWritableData
import pl.beone.promena.transformer.internal.model.data.memory.toMemoryData
import pl.beone.promena.transformer.internal.model.metadata.emptyMetadata
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.toPDDocument

internal data class TextWithMetadataAssert(
    val texts: List<String>,
    val metadata: Metadata = emptyMetadata()
)

private object MemoryCommunicationWritableDataCreator : CommunicationWritableDataCreator {
    override fun create(communicationParameters: CommunicationParameters): WritableData = emptyMemoryWritableData()
}

internal fun createPdfBoxPageExtractorTransformer(
    settings: PdfBoxPageExtractorTransformerSettings = PdfBoxPageExtractorTransformerSettings(
        MemoryUsageSetting.setupMainMemoryOnly()
    ),
    defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters = PdfBoxPageExtractorTransformerDefaultParameters(
        true
    ),
    communicationParameters: CommunicationParameters = mockk(),
    communicationWritableDataCreator: CommunicationWritableDataCreator = MemoryCommunicationWritableDataCreator
): PdfBoxPageExtractorTransformer =
    PdfBoxPageExtractorTransformer(
        settings,
        defaultParameters,
        communicationParameters,
        communicationWritableDataCreator
    )

private val data = getResourceAsBytes("/document/test.pdf").toMemoryData()

internal fun test(
    parameters: Parameters,
    textWithMetadataAsserts: List<TextWithMetadataAssert> = emptyList(),
    metadata: Metadata = emptyMetadata(),
    transformer: PdfBoxPageExtractorTransformer = createPdfBoxPageExtractorTransformer()
) {
    with(
        transformer.transform(singleDataDescriptor(data, APPLICATION_PDF, metadata), APPLICATION_PDF, parameters)
    ) {
        withClue("Transformed data should contain <${textWithMetadataAsserts.size}> element") { descriptors shouldHaveSize textWithMetadataAsserts.size }

        descriptors.zip(textWithMetadataAsserts)
            .forEach { (singleTransformedDataDescriptor, textWithMetadataAssert) ->
                PDDocument.load(singleTransformedDataDescriptor.data.getInputStream()).use { pdDocument ->
                    pdDocument.readPages() shouldBe textWithMetadataAssert.texts
                }

                try {
                    BarcodeDetectorMetadataGetter(singleTransformedDataDescriptor.metadata).getBarcodes() shouldBe
                            BarcodeDetectorMetadataGetter(textWithMetadataAssert.metadata).getBarcodes()
                } catch (e: NoSuchElementException) {
                    singleTransformedDataDescriptor.metadata shouldBe textWithMetadataAssert.metadata
                }
            }
    }
}

private fun PDDocument.readPages(): List<String> =
    (0 until numberOfPages)
        .map { page -> readText(page) }

private fun PDDocument.readText(page: Int): String =
    getPage(page).toPDDocument().use { PDFTextStripper().getText(it).trim() }