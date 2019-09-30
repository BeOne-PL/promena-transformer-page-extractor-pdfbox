package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import pl.beone.promena.transformer.contract.transformer.transformerId

object PdfBoxPageExtractionConstants {

    const val TRANSFORMER_NAME = "page-extraction"

    const val TRANSFORMER_SUB_NAME = "pdf-box"

    @JvmField
    val TRANSFORMER_ID = transformerId(TRANSFORMER_NAME, TRANSFORMER_SUB_NAME)
}