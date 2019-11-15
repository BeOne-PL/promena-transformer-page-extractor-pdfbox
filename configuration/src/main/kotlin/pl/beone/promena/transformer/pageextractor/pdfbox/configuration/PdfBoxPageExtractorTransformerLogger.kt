package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import javax.annotation.PostConstruct

@Configuration
class PdfBoxPageExtractorTransformerLogger(
    private val settings: PdfBoxPageExtractorTransformerSettings,
    private val defaultParameters: PdfBoxPageExtractorTransformerDefaultParameters
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @PostConstruct
    private fun log() {
        logger.info {
            "Run <${PdfBoxPageExtractorTransformer::class.java.canonicalName}> with <$settings> and <$defaultParameters>"
        }
    }
}