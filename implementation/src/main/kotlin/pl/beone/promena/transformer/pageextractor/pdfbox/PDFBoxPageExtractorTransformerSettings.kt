package pl.beone.promena.transformer.pageextractor.pdfbox

import org.apache.pdfbox.io.MemoryUsageSetting

data class PDFBoxPageExtractorTransformerSettings(
    val memoryUsageSetting: MemoryUsageSetting,
    val fallbackMemoryUsageSetting: MemoryUsageSetting? = null
)