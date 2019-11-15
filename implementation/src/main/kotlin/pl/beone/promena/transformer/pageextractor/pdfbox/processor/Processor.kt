package pl.beone.promena.transformer.pageextractor.pdfbox.processor

import kotlinx.coroutines.asCoroutineDispatcher
import mu.KotlinLogging
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.contract.model.data.Data
import pl.beone.promena.transformer.contract.model.data.WritableData
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.getPages
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.getRelaxedOrDefault
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.getInputStream
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.toPDDocument
import pl.beone.promena.transformer.util.execute
import java.io.OutputStream
import java.util.concurrent.Executors

internal class Processor(
    private val settings: PdfBoxPageExtractorTransformerSettings,
    private val defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val executionDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    fun process(
        singleDataDescriptor: DataDescriptor.Single,
        parameters: Parameters,
        transformedWritableData: WritableData
    ): TransformedDataDescriptor.Single {
        val (data, _, metadata) = singleDataDescriptor

        execute(parameters.getTimeoutOrNull() ?: defaultParameters.timeout, executionDispatcher) {
            process(data, parameters, transformedWritableData.getOutputStream())
        }

        return singleTransformedDataDescriptor(transformedWritableData, metadata)
    }

    private fun process(data: Data, parameters: Parameters, outputStream: OutputStream) {
        val indexPages = parameters.getPages()
            .map { it - 1 } // because pages are indexed from 0

        val relaxed = parameters.getRelaxedOrDefault(defaultParameters.relaxed)

        PDDocument.load(data.getInputStream())
            .use { pdDocument -> mergePages(getPagesRespectingRelaxedParameter(pdDocument, indexPages, relaxed), outputStream) }
    }

    private fun getPagesRespectingRelaxedParameter(pdDocument: PDDocument, indexPages: List<Int>, relaxed: Boolean): List<PDPage> =
        indexPages.mapNotNull { indexPage -> getPageRespectingRelaxedParameter(pdDocument, indexPage, relaxed) }

    private fun getPageRespectingRelaxedParameter(document: PDDocument, indexPage: Int, relaxed: Boolean): PDPage? =
        try {
            document.getPage(indexPage)
        } catch (e: IndexOutOfBoundsException) {
            if (!relaxed) {
                throw IllegalArgumentException("Document hasn't <${indexPage + 1}> pages")
            } else {
                null
            }
        }

    private fun mergePages(pages: List<PDPage>, outputStream: OutputStream) {
        try {
            mergePages(pages, settings.memoryUsageSetting, outputStream)
        } catch (e: Exception) {
            val fallbackMemoryUsageSetting = settings.fallbackMemoryUsageSetting
            if (fallbackMemoryUsageSetting != null) {
                logger.warn(e) { "Couldn't merge pages. Using <${fallbackMemoryUsageSetting::class.java.canonicalName}> as fallback..." }
                mergePages(pages, fallbackMemoryUsageSetting, outputStream)
            } else {
                throw e
            }
        }
    }

    private fun mergePages(pages: List<PDPage>, memoryUsageSetting: MemoryUsageSetting, outputStream: OutputStream) {
        PDFMergerUtility()
            .apply { destinationStream = outputStream }
            .also { merger -> merger.addSources(pages.map { pdPage -> pdPage.toPDDocument().getInputStream() }) }
            .also { merger -> merger.mergeDocuments(memoryUsageSetting) }
    }
}