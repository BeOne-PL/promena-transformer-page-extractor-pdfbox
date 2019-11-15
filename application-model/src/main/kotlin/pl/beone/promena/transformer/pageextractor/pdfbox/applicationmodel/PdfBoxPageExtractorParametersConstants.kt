package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.internal.getClazz

object PdfBoxPageExtractorParametersConstants {

    object Pages {
        const val NAME = "pages"
        @JvmField
        val CLASS = getClazz<List<Int>>()
    }

    object Relaxed {
        const val NAME = "relaxed"
        @JvmField
        val CLASS = Boolean::class.java
    }
}