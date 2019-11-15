package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.promena.transformer.contract.transformer.transformerId

object PdfBoxPageExtractorConstants {

    const val TRANSFORMER_NAME = "page extractor"

    const val TRANSFORMER_SUB_NAME = "PDFBox"

    @JvmField
    val TRANSFORMER_ID = transformerId(TRANSFORMER_NAME, TRANSFORMER_SUB_NAME)
}