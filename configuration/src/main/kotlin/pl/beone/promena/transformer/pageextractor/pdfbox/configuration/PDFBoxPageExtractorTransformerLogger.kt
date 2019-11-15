package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings
import javax.annotation.PostConstruct

@Configuration
class PDFBoxPageExtractorTransformerLogger(
    private val settings: PDFBoxPageExtractorTransformerSettings,
    private val defaultParameters: PDFBoxPageExtractorTransformerDefaultParameters
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @PostConstruct
    private fun log() {
        logger.info {
            "Run <${PDFBoxPageExtractorTransformer::class.java.canonicalName}> with <$settings> and <$defaultParameters>"
        }
    }
}