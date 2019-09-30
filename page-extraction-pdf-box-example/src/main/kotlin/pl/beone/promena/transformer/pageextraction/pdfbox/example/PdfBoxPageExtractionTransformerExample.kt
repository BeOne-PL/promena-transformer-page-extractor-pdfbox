package pl.beone.promena.transformer.pageextraction.pdfbox.example

import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.pdfBoxPageExtractionTransformation

fun promena(): Transformation {
    // Data: example.txt

    return pdfBoxPageExtractionTransformation(pdfBoxPageExtractionParameters(pages = listOf(1, 3)))
}