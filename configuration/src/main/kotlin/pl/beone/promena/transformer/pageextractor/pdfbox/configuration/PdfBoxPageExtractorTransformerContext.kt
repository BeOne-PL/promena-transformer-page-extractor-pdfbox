package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.contract.communication.CommunicationWritableDataCreator
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings

@Configuration
class PdfBoxPageExtractorTransformerContext {

    @Bean
    fun pdfBoxPageExtractorTransformer(
        settings: PdfBoxPageExtractorTransformerSettings,
        defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters,
        @Qualifier("internalCommunicationParameters") communicationParameters: CommunicationParameters,
        @Qualifier("internalCommunicationWritableDataCreator") communicationWritableDataCreator: CommunicationWritableDataCreator

    ) =
        PdfBoxPageExtractorTransformer(
            settings,
            defaultParameters,
            communicationParameters,
            communicationWritableDataCreator
        )
}