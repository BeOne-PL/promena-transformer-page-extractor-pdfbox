package pl.beone.promena.transformer.pageextraction.pdfbox.configuration

import org.apache.pdfbox.io.MemoryUsageSetting
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
        internalCommunicationParameters: CommunicationParameters
    ) =
        PdfBoxPageExtractionTransformer(
            createSettings(environment),
            createDefaultParameters(environment),
            internalCommunicationParameters
        )

    private fun createSettings(environment: Environment): PdfBoxPageExtractionTransformerSettings =
        PdfBoxPageExtractionTransformerSettings(
            createUsingStaticMethod(environment.getRequiredProperty("transformer.pl.beone.promena.transformer.pageextraction.pdfbox.settings.memoryUsageSetting")),
            environment.getProperty("transformer.pl.beone.promena.transformer.pageextraction.pdfbox.settings.fallbackMemoryUsageSetting")
                ?.let(this::createUsingStaticMethod)
        )

    private fun createDefaultParameters(environment: Environment): PdfBoxPageExtractionTransformerDefaultParameters =
        PdfBoxPageExtractionTransformerDefaultParameters(
            environment.getRequiredProperty(
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.parameters.default.relaxed",
                Boolean::class.java
            )
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