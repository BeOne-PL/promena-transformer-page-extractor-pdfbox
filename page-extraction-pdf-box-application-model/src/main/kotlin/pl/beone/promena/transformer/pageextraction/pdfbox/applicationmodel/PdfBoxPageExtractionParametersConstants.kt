package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.internal.getClazz

object PdfBoxPageExtractionParametersConstants {

    object Pages {
        const val NAME = "pages"
        val CLASS = getClazz<List<Int>>()
    }
}