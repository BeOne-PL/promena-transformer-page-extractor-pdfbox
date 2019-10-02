package pl.beone.promena.transformer.pageextraction.pdfbox.configuration

import io.kotlintest.matchers.instanceOf
import io.kotlintest.shouldBe
import org.apache.pdfbox.io.MemoryUsageSetting
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.mock.env.MockEnvironment
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings
import java.time.Duration

class PdfBoxPageExtractionTransformerContextTest {

    @Test
    fun `default timeout and default fallbackMemoryUsageSetting`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.parameters.default.relaxed" to "true"
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PdfBoxPageExtractionTransformerConfigurationContext::class.java)
        applicationContext.getBean(PdfBoxPageExtractionTransformerSettings::class.java).let {
            it.memoryUsageSetting shouldBe instanceOf(MemoryUsageSetting::class)
            it.fallbackMemoryUsageSetting shouldBe null
        }
        applicationContext.getBean(PdfBoxPageExtractionTransformerDefaultParameters::class.java).let {
            it.timeout shouldBe null
            it.relaxed shouldBe true
        }
    }

    @Test
    fun `given timeout and fallbackMemoryUsageSetting`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.settings.fallbackMemoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupTempFileOnly",
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.parameters.default.timeout" to "5s",
                "transformer.pl.beone.promena.transformer.pageextraction.pdfbox.parameters.default.relaxed" to "true"
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PdfBoxPageExtractionTransformerConfigurationContext::class.java)
        applicationContext.getBean(PdfBoxPageExtractionTransformerSettings::class.java).let {
            it.memoryUsageSetting shouldBe instanceOf(MemoryUsageSetting::class)
            it.fallbackMemoryUsageSetting shouldBe instanceOf(MemoryUsageSetting::class)
        }
        applicationContext.getBean(PdfBoxPageExtractionTransformerDefaultParameters::class.java).let {
            it.timeout shouldBe Duration.ofSeconds(5)
            it.relaxed shouldBe true
        }
    }

    private fun createEnvironment(properties: Map<String, String>): MockEnvironment =
        MockEnvironment()
            .apply { properties.forEach { (key, value) -> setProperty(key, value) } }

    private fun createConfigApplicationContext(environment: ConfigurableEnvironment, clazz: Class<*>): AnnotationConfigApplicationContext =
        AnnotationConfigApplicationContext().apply {
            this.environment = environment
            register(clazz)
            refresh()
        }
}