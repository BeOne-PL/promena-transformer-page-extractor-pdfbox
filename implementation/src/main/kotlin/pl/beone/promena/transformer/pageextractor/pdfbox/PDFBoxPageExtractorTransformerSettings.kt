package pl.beone.promena.transformer.pageextractor.pdfbox

import org.apache.pdfbox.io.MemoryUsageSetting

data class PDFBoxPageExtractorTransformerSettings(
    val memoryUsageSetting: MemoryUsageSetting = MemoryUsageSetting.setupMainMemoryOnly(),
    val fallbackMemoryUsageSetting: MemoryUsageSetting? = null
)