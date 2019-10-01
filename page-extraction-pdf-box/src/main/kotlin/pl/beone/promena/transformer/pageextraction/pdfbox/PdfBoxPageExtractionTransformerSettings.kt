package pl.beone.promena.transformer.pageextraction.pdfbox

import org.apache.pdfbox.io.MemoryUsageSetting

data class PdfBoxPageExtractionTransformerSettings(
    val memoryUsageSetting: MemoryUsageSetting,
    val fallbackMemoryUsageSetting: MemoryUsageSetting?
)