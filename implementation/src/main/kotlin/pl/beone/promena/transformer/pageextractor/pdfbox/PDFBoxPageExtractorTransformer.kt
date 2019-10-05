package pl.beone.promena.transformer.pageextractor.pdfbox

import pl.beone.promena.communication.file.model.contract.FileCommunicationParameters
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.Transformer
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.toTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PDFBoxPageExtractorSupport
import pl.beone.promena.transformer.pageextractor.pdfbox.transformer.AbstractTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.transformer.FileTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.transformer.MemoryTransformer

class PDFBoxPageExtractorTransformer(
    private val settings: PDFBoxPageExtractorTransformerSettings,
    private val defaultParameters: PDFBoxPageExtractorTransformerDefaultParameters,
    private val internalCommunicationParameters: CommunicationParameters
) : Transformer {

    override fun transform(dataDescriptor: DataDescriptor, targetMediaType: MediaType, parameters: Parameters): TransformedDataDescriptor =
        dataDescriptor.descriptors
            .map { determineTransformer().transform(it, parameters) }
            .toTransformedDataDescriptor()

    private fun determineTransformer(): AbstractTransformer =
        when (internalCommunicationParameters.getId()) {
            FileCommunicationParameters.ID ->
                FileTransformer(settings, defaultParameters, (internalCommunicationParameters as FileCommunicationParameters).getDirectory())
            else ->
                MemoryTransformer(settings, defaultParameters)
        }

    override fun isSupported(dataDescriptor: DataDescriptor, targetMediaType: MediaType, parameters: Parameters) {
        PDFBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters)
    }
}