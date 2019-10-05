package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import org.apache.pdfbox.io.MemoryUsageSetting
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.getNotBlankProperty
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.getRequiredNotBlankProperty
import pl.beone.promena.transformer.pageextractor.pdfbox.configuration.extension.toDuration

@Configuration
class PDFBoxPageExtractorTransformerConfigurationContext {

    companion object {
        private const val PROPERTY_PREFIX = "transformer.pl.beone.promena.transformer.pageextractor.pdfbox"
    }

    @Bean
    fun pdfBoxPageExtractorTransformerSettings(environment: Environment): PDFBoxPageExtractorTransformerSettings =
        PDFBoxPageExtractorTransformerSettings(
            environment.getRequiredNotBlankProperty("$PROPERTY_PREFIX.settings.memoryUsageSetting").let(::createUsingStaticMethod),
            environment.getNotBlankProperty("$PROPERTY_PREFIX.settings.fallbackMemoryUsageSetting")?.let(::createUsingStaticMethod)
        )

    @Bean
    fun pdfBoxPageExtractorTransformerDefaultParameters(environment: Environment): PDFBoxPageExtractorTransformerDefaultParameters =
        PDFBoxPageExtractorTransformerDefaultParameters(
            environment.getRequiredNotBlankProperty("$PROPERTY_PREFIX.default.parameters.relaxed").toBoolean(),
            environment.getNotBlankProperty("$PROPERTY_PREFIX.default.parameters.timeout")?.toDuration()
        )

    private fun createUsingStaticMethod(property: String): MemoryUsageSetting {
        val (className, methodName) = property.split("::")

        return try {
            Class.forName(className)
                .methods
                .firstOrNull { it.name == methodName && it.parameterCount == 0 }
                ?.let { it.invoke(null) as MemoryUsageSetting }
                ?: throw IllegalStateException("Class <$className> doesn't contain <$methodName> method")
        } catch (e: Exception) {
            throw IllegalStateException("Couldn't create MemoryUsageSetting using <$property>. It must be static method without arguments!", e)
        }
    }
}