package pl.beone.promena.transformer.pageextraction.pdfbox.transformer

import mu.KotlinLogging
import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.getPages
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.getRelaxedOrDefault
import pl.beone.promena.transformer.pageextraction.pdfbox.extension.getInputStream
import pl.beone.promena.transformer.pageextraction.pdfbox.extension.toPDDocument
import java.io.OutputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal abstract class AbstractTransformer(
    private val settings: PdfBoxPageExtractionTransformerSettings,
    private val defaultParameters: PdfBoxPageExtractionTransformerDefaultParameters
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    protected abstract fun getOutputStream(): OutputStream

    protected abstract fun createData(): Data

    fun transform(singleDataDescriptor: DataDescriptor.Single, targetMediaType: MediaType, parameters: Parameters): TransformedDataDescriptor.Single {
        val (data, _, metadata) = singleDataDescriptor

        val timeout = parameters.getTimeoutOrNull() ?: defaultParameters.timeout
        if (timeout != null) {
            Executors.newSingleThreadExecutor()
                .submit { process(data, parameters) }
                .get(timeout.toMillis(), TimeUnit.MILLISECONDS)
        } else {
            process(data, parameters)
        }

        return singleTransformedDataDescriptor(createData(), metadata)
    }

    private fun process(data: Data, parameters: Parameters) {
        val indexPages = parameters.getPages()
            .map { it - 1 } // because pages are indexed from 0

        val relaxed = parameters.getRelaxedOrDefault(defaultParameters.relaxed)

        PDDocument.load(data.getInputStream())
            .use { pdDocument -> mergePages(getPagesRespectingRelaxedParameter(pdDocument, indexPages, relaxed)) }
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

    private fun mergePages(pages: List<PDPage>) {
        try {
            mergePages(pages, settings.memoryUsageSetting)
        } catch (e: Exception) {
            val fallbackMemoryUsageSetting = settings.fallbackMemoryUsageSetting
            if (fallbackMemoryUsageSetting != null) {
                logger.warn(e) { "Couldn't merge pages. Using <${fallbackMemoryUsageSetting::class.java.canonicalName}> as fallback..." }
                mergePages(pages, fallbackMemoryUsageSetting)
            } else {
                throw e
            }
        }
    }

    private fun mergePages(pages: List<PDPage>, memoryUsageSetting: MemoryUsageSetting) {
        PDFMergerUtility()
            .apply { destinationStream = getOutputStream() }
            .also { merger -> merger.addSources(pages.map { pdPage -> pdPage.toPDDocument().getInputStream() }) }
            .also { merger -> merger.mergeDocuments(memoryUsageSetting) }
    }
}