package pl.beone.promena.transformer.pageextractor.pdfbox

import java.time.Duration

data class PDFBoxPageExtractorTransformerDefaultParameters(
    val relaxed: Boolean,
    val timeout: Duration? = null
)