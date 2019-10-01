package pl.beone.promena.transformer.pageextraction.pdfbox.example

import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionTransformation

fun `promena _ relaxed`(): Transformation {
    // Data: example.pdf

    return pdfBoxPageExtractionTransformation(pdfBoxPageExtractionParameters(pages = listOf(1, 3, 99), relaxed = true))
}

fun `promena _ not relaxed`(): Transformation {
    // Data: example.pdf

    return pdfBoxPageExtractionTransformation(pdfBoxPageExtractionParameters(pages = listOf(1, 3), relaxed = false))
}
