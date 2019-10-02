package pl.beone.promena.transformer.pageextraction.pdfbox

import java.time.Duration

data class PdfBoxPageExtractionTransformerDefaultParameters(
    val timeout: Duration?,
    val relaxed: Boolean
)