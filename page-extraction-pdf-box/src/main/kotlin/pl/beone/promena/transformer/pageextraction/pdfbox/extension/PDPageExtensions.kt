package pl.beone.promena.transformer.pageextraction.pdfbox.extension

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage

fun PDPage.toPDDocument(): PDDocument =
    PDDocument()
        .apply { addPage(this@toPDDocument) }
