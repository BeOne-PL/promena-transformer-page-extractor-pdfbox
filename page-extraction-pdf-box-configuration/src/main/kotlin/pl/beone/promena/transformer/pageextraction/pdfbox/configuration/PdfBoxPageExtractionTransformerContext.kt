package pl.beone.promena.transformer.pageextraction.pdfbox.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformer

@Configuration
class PdfBoxPageExtractionTransformerContext {

    @Bean
    fun pdfBoxPageExtractionTransformer(
        internalCommunicationParameters: CommunicationParameters
    ) =
        PdfBoxPageExtractionTransformer(
            internalCommunicationParameters
        )
}