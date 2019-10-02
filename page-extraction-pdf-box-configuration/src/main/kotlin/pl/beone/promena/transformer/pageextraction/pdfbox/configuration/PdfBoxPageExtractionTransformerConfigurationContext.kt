package pl.beone.promena.transformer.pageextraction.pdfbox.configuration

import org.apache.pdfbox.io.MemoryUsageSetting
import org.joda.time.format.PeriodFormatterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings
import java.time.Duration

@Configuration
class PdfBoxPageExtractionTransformerConfigurationContext {

    companion object {
        private const val PROPERTY_PREFIX = "transformer.pl.beone.promena.transformer.pageextraction.pdfbox"
    }

    @Bean
    fun pdfBoxPageExtractionTransformerSettings(environment: Environment): PdfBoxPageExtractionTransformerSettings =
        PdfBoxPageExtractionTransformerSettings(
            createUsingStaticMethod(environment.getRequiredProperty("$PROPERTY_PREFIX.settings.memoryUsageSetting")),
            environment.getProperty("$PROPERTY_PREFIX.settings.fallbackMemoryUsageSetting")?.let(this::createUsingStaticMethod)
        )

    @Bean
    fun pdfBoxPageExtractionTransformerDefaultParameters(environment: Environment): PdfBoxPageExtractionTransformerDefaultParameters =
        PdfBoxPageExtractionTransformerDefaultParameters(
            environment.getProperty("$PROPERTY_PREFIX.parameters.default.timeout")?.toDuration(),
            environment.getRequiredProperty("$PROPERTY_PREFIX.parameters.default.relaxed", Boolean::class.java)
        )

    private fun String.toDuration(): Duration {
        val formatter = PeriodFormatterBuilder()
            .appendDays().appendSuffix("d ")
            .appendHours().appendSuffix("h ")
            .appendMinutes().appendSuffix("m")
            .appendSeconds().appendSuffix("s")
            .appendMillis().appendSuffix("ms")
            .toFormatter()

        return Duration.ofMillis(formatter.parsePeriod(this).toStandardDuration().millis)
    }

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