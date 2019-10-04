package pl.beone.promena.transformer.pageextractor.pdfbox.example

import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorTransformation

fun promena(): Transformation {
    // Data: example.pdf

    return pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(1, 3, 66)))
}

fun `promena _ relaxed`(): Transformation {
    // Data: example.pdf

    return pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(1, 3, 99), relaxed = true))
}

fun `promena _ not relaxed`(): Transformation {
    // Data: example.pdf

    return pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(1, 3), relaxed = false))
}
