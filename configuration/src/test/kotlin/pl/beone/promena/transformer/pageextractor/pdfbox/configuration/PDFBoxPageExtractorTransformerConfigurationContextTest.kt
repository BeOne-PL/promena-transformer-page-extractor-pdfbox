package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.mock.env.MockEnvironment
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings
import java.time.Duration

class PDFBoxPageExtractorTransformerConfigurationContextTest {

    @Test
    fun `setting context`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.fallbackMemoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupTempFileOnly",

                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.relaxed" to "true",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.timeout" to "5m"
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PDFBoxPageExtractorTransformerConfigurationContext::class.java)
        applicationContext.getBean(PDFBoxPageExtractorTransformerSettings::class.java).let {
            it.memoryUsageSetting.useMainMemory() shouldBe true
            it.fallbackMemoryUsageSetting shouldNotBe null
            it.fallbackMemoryUsageSetting!!.useTempFile() shouldBe true
        }
        applicationContext.getBean(PDFBoxPageExtractorTransformerDefaultParameters::class.java).let {
            it.relaxed shouldBe true
            it.timeout shouldBe Duration.ofMinutes(5)
        }
    }

    @Test
    fun `setting context _ empty timeout and fallbackMemoryUsageSetting`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.fallbackMemoryUsageSetting" to "",

                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.relaxed" to "true",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.timeout" to ""
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PDFBoxPageExtractorTransformerConfigurationContext::class.java)
        applicationContext.getBean(PDFBoxPageExtractorTransformerSettings::class.java).let {
            it.memoryUsageSetting.useMainMemory() shouldBe true
            it.fallbackMemoryUsageSetting shouldBe null
        }
        applicationContext.getBean(PDFBoxPageExtractorTransformerDefaultParameters::class.java).let {
            it.relaxed shouldBe true
            it.timeout shouldBe null
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