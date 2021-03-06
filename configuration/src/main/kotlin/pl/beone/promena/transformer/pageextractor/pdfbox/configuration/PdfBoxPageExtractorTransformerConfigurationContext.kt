package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import org.apache.pdfbox.io.MemoryUsageSetting
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.getNotBlankProperty
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.getRequiredNotBlankProperty
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.toDuration

@Configuration
class PdfBoxPageExtractorTransformerConfigurationContext {

    companion object {
        private const val PROPERTY_PREFIX = "transformer.pl.beone.promena.transformer.pageextractor.pdfbox"
    }

    @Bean
    fun pdfBoxPageExtractorTransformerSettings(environment: Environment): PdfBoxPageExtractorTransformerSettings =
        PdfBoxPageExtractorTransformerSettings(
            environment.getRequiredNotBlankProperty("$PROPERTY_PREFIX.settings.memoryUsageSetting").let(::createUsingStaticMethod),
            environment.getNotBlankProperty("$PROPERTY_PREFIX.settings.fallbackMemoryUsageSetting")?.let(::createUsingStaticMethod)
        )

    @Bean
    fun pdfBoxPageExtractorTransformerDefaultParameters(environment: Environment): PdfBoxPageExtractorTransformerDefaultParameters =
        PdfBoxPageExtractorTransformerDefaultParameters(
            environment.getRequiredNotBlankProperty("$PROPERTY_PREFIX.default.parameters.split-by-barcode-metadata").toBoolean(),
            environment.getNotBlankProperty("$PROPERTY_PREFIX.default.parameters.timeout")?.toDuration()
        )

    private fun createUsingStaticMethod(property: String): MemoryUsageSetting {
        val (className, methodName) = property.split("::")

        return try {
            Class.forName(className)
                .methods
                .firstOrNull { it.name == methodName && it.parameterCount == 0 }
                ?.let { it.invoke(null) as MemoryUsageSetting }
                ?: error("Class <$className> doesn't contain <$methodName> method")
        } catch (e: Exception) {
            throw IllegalStateException("Couldn't create MemoryUsageSetting using <$property>. It must be static method without arguments!", e)
        }
    }
}