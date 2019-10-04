package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.beone.promena.transformer.contract.communication.CommunicationParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings

@Configuration
class PDFBoxPageExtractorTransformerContext {

    @Bean
    fun pdfBoxPageExtractorTransformer(
        settings: PDFBoxPageExtractorTransformerSettings,
        defaultParameters: PDFBoxPageExtractorTransformerDefaultParameters,
        internalCommunicationParameters: CommunicationParameters
    ) =
        PDFBoxPageExtractorTransformer(
            settings,
            defaultParameters,
            internalCommunicationParameters
        )
}