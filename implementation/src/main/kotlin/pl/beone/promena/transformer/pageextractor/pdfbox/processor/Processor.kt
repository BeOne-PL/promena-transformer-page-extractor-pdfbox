package pl.beone.promena.transformer.pageextractor.pdfbox.processor

import kotlinx.coroutines.asCoroutineDispatcher
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.getPages
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.getSplitByBarcodeMetadataOrNull
import pl.beone.promena.transformer.util.execute
import java.util.concurrent.Executors

internal class Processor(
    settings: PdfBoxPageExtractorTransformerSettings,
    private val defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters,
    communicationParameters: CommunicationParameters,
    communicationWritableDataCreator: CommunicationWritableDataCreator
) {

    private val executionDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val pdfCreator = PdfCreator(settings, communicationParameters, communicationWritableDataCreator)
    private val pagesParameterProcessor = PagesParameterProcessor(pdfCreator)
    private val splitByBarcodeMetadataParameterProcessor = SplitByBarcodeMetadataParameterProcessor(pdfCreator)

    fun process(singleDataDescriptor: DataDescriptor.Single, parameters: Parameters): List<TransformedDataDescriptor.Single> {
        val (data, _, metadata) = singleDataDescriptor

        return execute(parameters.getTimeoutOrNull() ?: defaultParameters.timeout, executionDispatcher) {
            try {
                pagesParameterProcessor.process(data, metadata, parameters.getPages())
            } catch (e: NoSuchElementException) {
                if (parameters.getSplitByBarcodeMetadataOrNull() ?: defaultParameters.splitByBarcodeMetadata) {
                    splitByBarcodeMetadataParameterProcessor.process(data, metadata)
                } else {
                    listOf(singleTransformedDataDescriptor(data, metadata))
                }
            }
        }
    }
}