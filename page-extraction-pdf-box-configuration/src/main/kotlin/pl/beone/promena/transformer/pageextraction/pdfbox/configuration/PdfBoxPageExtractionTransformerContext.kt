package pl.beone.promena.transformer.pageextraction.pdfbox.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformer
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings

@Configuration
class PdfBoxPageExtractionTransformerContext {

    @Bean
    fun pdfBoxPageExtractionTransformer(
        environment: Environment,
        internalCommunicationParameters: CommunicationParameters,
        settings: PdfBoxPageExtractionTransformerSettings,
        defaultParameters: PdfBoxPageExtractionTransformerDefaultParameters
    ) =
        PdfBoxPageExtractionTransformer(
            settings,
            defaultParameters,
            internalCommunicationParameters
        )
}