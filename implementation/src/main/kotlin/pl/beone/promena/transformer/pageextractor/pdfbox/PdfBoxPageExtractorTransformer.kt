package pl.beone.promena.transformer.pageextractor.pdfbox

import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.Transformer
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.toTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorSupport
import pl.beone.promena.transformer.pageextractor.pdfbox.processor.Processor

class PdfBoxPageExtractorTransformer(
    settings: PdfBoxPageExtractorTransformerSettings,
    defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters,
    communicationParameters: CommunicationParameters,
    communicationWritableDataCreator: CommunicationWritableDataCreator
) : Transformer {

    private val processor = Processor(settings, defaultParameters, communicationParameters, communicationWritableDataCreator)

    override fun transform(dataDescriptor: DataDescriptor, targetMediaType: MediaType, parameters: Parameters): TransformedDataDescriptor =
        dataDescriptor.descriptors
            .flatMap { processor.process(it, parameters) }
            .toTransformedDataDescriptor()

    override fun isSupported(dataDescriptor: DataDescriptor, targetMediaType: MediaType, parameters: Parameters) {
        PdfBoxPageExtractorSupport.isSupported(dataDescriptor, targetMediaType, parameters)
    }
}