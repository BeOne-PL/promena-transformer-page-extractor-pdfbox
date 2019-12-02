package pl.beone.promena.transformer.pageextractor.pdfbox.configuration

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.mock.env.MockEnvironment
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformerSettings
import java.time.Duration

class PdfBoxPageExtractorTransformerConfigurationContextTest {

    @Test
    fun `setting context _ default values`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.fallbackMemoryUsageSetting" to "",

                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.split-by-barcode-metadata" to "true",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.timeout" to ""
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PdfBoxPageExtractorTransformerConfigurationContext::class.java)
        with(applicationContext.getBean(PdfBoxPageExtractorTransformerSettings::class.java)) {
            memoryUsageSetting.useMainMemory() shouldBe true
            fallbackMemoryUsageSetting shouldBe null
        }
        with(applicationContext.getBean(PdfBoxPageExtractorTransformerDefaultParameters::class.java)) {
            splitByBarcodeMetadata shouldBe true
            timeout shouldBe null
        }
    }

    @Test
    fun `setting context`() {
        val environment = createEnvironment(
            mapOf(
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.memoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.fallbackMemoryUsageSetting" to "org.apache.pdfbox.io.MemoryUsageSetting::setupTempFileOnly",

                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.split-by-barcode-metadata" to "true",
                "transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.timeout" to "5m"
            )
        )

        val applicationContext = createConfigApplicationContext(environment, PdfBoxPageExtractorTransformerConfigurationContext::class.java)
        with(applicationContext.getBean(PdfBoxPageExtractorTransformerSettings::class.java)) {
            memoryUsageSetting.useMainMemory() shouldBe true
            fallbackMemoryUsageSetting shouldNotBe null
            fallbackMemoryUsageSetting!!.useTempFile() shouldBe true
        }
        with(applicationContext.getBean(PdfBoxPageExtractorTransformerDefaultParameters::class.java)) {
            splitByBarcodeMetadata shouldBe true
            timeout shouldBe Duration.ofMinutes(5)
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