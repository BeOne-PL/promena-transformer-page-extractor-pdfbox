package pl.beone.promena.transformer.pageextractor.pdfbox.util

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.withClue
import io.kotlintest.shouldBe
import io.mockk.mockk
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.contract.data.singleDataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.contract.model.data.WritableData
import pl.beone.promena.transformer.internal.model.data.memory.emptyMemoryWritableData
import pl.beone.promena.transformer.internal.model.data.memory.toMemoryData
import pl.beone.promena.transformer.internal.model.metadata.emptyMetadata
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.toPDDocument

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

private val data = getResourceAsBytes("/text/test.pdf").toMemoryData()

internal fun test(
    parameters: Parameters,
    assertPagesNumber: Int = -1,
    assertPagesText: List<String> = emptyList(),
    transformer: PdfBoxPageExtractorTransformer = createPdfBoxPageExtractorTransformer()
) {
    transformer
        .transform(singleDataDescriptor(data, APPLICATION_PDF, emptyMetadata()), APPLICATION_PDF, parameters).let { transformedDataDescriptor ->
            withClue("Transformed data should contain only <1> element") { transformedDataDescriptor.descriptors shouldHaveSize 1 }

            transformedDataDescriptor.descriptors[0].let {
                val document = PDDocument.load(it.data.getInputStream())
                withClue("Data should contain <$assertPagesNumber> number of pages") { document.numberOfPages shouldBe assertPagesNumber }
                withClue("Data should contain <$assertPagesText> text on pages") { document.readPages() shouldBe assertPagesText }

                it.metadata shouldBe emptyMetadata()
            }
        }
}

private fun PDDocument.readPages(): List<String> =
    use { document ->
        (0 until numberOfPages)
            .map { page -> document.readText(page) }
    }

private fun PDDocument.readText(page: Int): String =
    PDFTextStripper().getText(getPage(page).toPDDocument()).trim()