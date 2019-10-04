package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.internal.getClazz

object PDFBoxPageExtractorParametersConstants {

    object Pages {
        const val NAME = "pages"
        val CLASS = getClazz<List<Int>>()
    }

    object Relaxed {
        const val NAME = "relaxed"
        val CLASS = Boolean::class.java
    }
}