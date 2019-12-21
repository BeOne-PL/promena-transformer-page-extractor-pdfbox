package pl.beone.promena.transformer.pageextractor.pdfbox.processor

import mu.KotlinLogging
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDPage
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.contract.model.data.WritableData
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.getInputStream
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.toPDDocument
import java.io.OutputStream

internal class PdfCreator(
    private val settings: PdfBoxPageExtractorTransformerSettings,
    private val communicationParameters: CommunicationParameters,
    private val communicationWritableDataCreator: CommunicationWritableDataCreator
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun create(pages: List<PDPage>): WritableData {
        val writableData = communicationWritableDataCreator.create(communicationParameters)

        try {
            mergePages(pages, settings.memoryUsageSetting, writableData.getOutputStream())
        } catch (e: Exception) {
            val fallbackMemoryUsageSetting = settings.fallbackMemoryUsageSetting
            if (fallbackMemoryUsageSetting != null) {
                logger.warn(e) { "Couldn't merge pages. Using <${fallbackMemoryUsageSetting::class.java.canonicalName}> as fallback..." }
                mergePages(pages, fallbackMemoryUsageSetting, writableData.getOutputStream())
            } else {
                throw e
            }
        }

        return writableData
    }

    private fun mergePages(pages: List<PDPage>, memoryUsageSetting: MemoryUsageSetting, outputStream: OutputStream) {
        PDFMergerUtility()
            .apply { destinationStream = outputStream }
            .also { merger -> merger.addSources(pages.map { pdPage -> pdPage.toPDDocument().use { it.getInputStream() } }) }
            .also { merger -> merger.mergeDocuments(memoryUsageSetting) }
    }

}