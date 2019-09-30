package pl.beone.promena.transformer.pageextraction.pdfbox

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.instanceOf
import io.kotlintest.matchers.withClue
import io.kotlintest.shouldBe
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import pl.beone.promena.communication.file.model.internal.fileCommunicationParameters
import pl.beone.promena.communication.memory.model.internal.memoryCommunicationParameters
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.data.singleDataDescriptor
import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.internal.model.data.FileData
import pl.beone.promena.transformer.internal.model.data.MemoryData
import pl.beone.promena.transformer.internal.model.data.toFileData
import pl.beone.promena.transformer.internal.model.data.toMemoryData
import pl.beone.promena.transformer.internal.model.metadata.emptyMetadata
import pl.beone.promena.transformer.pageextraction.pdfbox.extension.toPDDocument
import pl.beone.promena.transformer.pageextraction.pdfbox.util.getResourceAsBytes
import kotlin.reflect.KClass

private val testBytes = getResourceAsBytes("/text/test.pdf")

internal fun memoryTest(parameters: Parameters, assertPagesNumber: Int, assertPagesText: List<String>) {
    test(
        testBytes.toMemoryData(),
        MemoryData::class,
        memoryCommunicationParameters(),
        parameters,
        assertPagesNumber,
        assertPagesText
    )
}

internal fun fileTest(parameters: Parameters, assertPagesNumber: Int, assertPagesText: List<String>) {
    val directory = createTempDir()

    test(
        testBytes.inputStream().toFileData(directory),
        FileData::class,
        fileCommunicationParameters(directory),
        parameters,
        assertPagesNumber,
        assertPagesText
    )
}

private fun test(
    data: Data,
    dataClass: KClass<*>,
    communicationParameters: CommunicationParameters,
    parameters: Parameters,
    assertPagesNumber: Int,
    assertPagesText: List<String>
) {
    PdfBoxPageExtractionTransformer(communicationParameters)
        .transform(singleDataDescriptor(data, APPLICATION_PDF, emptyMetadata()), APPLICATION_PDF, parameters).let { transformedDataDescriptor ->
            withClue("Transformed data should contain only <1> element") { transformedDataDescriptor.descriptors shouldHaveSize 1 }

            transformedDataDescriptor.descriptors[0].let {
                withClue("Transformed data should be instance of <$dataClass>") { it.data shouldBe instanceOf(dataClass) }

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