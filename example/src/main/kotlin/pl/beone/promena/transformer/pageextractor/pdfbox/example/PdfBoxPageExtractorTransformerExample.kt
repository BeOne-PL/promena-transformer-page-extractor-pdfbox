package pl.beone.promena.transformer.pageextractor.pdfbox.example

import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.pdfBoxPageExtractorTransformation

fun promena(): Transformation {
    // Data: gre_research_validity_data.pdf

    return pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(listOf(1, 3))))
}

fun `promena _ two page sets`(): Transformation {
    // Data: gre_research_validity_data.pdf

    return pdfBoxPageExtractorTransformation(pdfBoxPageExtractorParameters(pages = listOf(listOf(1, 3), listOf(2, 4))))
}