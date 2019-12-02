package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.internal.getClazz

object PdfBoxPageExtractorParametersConstants {

    object Pages {
        const val NAME = "pages"
        @JvmField
        val CLASS = getClazz<List<List<Int>>>()
    }

    object SplitByBarcodeMetadata {
        const val NAME = "splitByBarcodeMetadata"
        @JvmField
        val CLASS = Boolean::class.java
    }
}