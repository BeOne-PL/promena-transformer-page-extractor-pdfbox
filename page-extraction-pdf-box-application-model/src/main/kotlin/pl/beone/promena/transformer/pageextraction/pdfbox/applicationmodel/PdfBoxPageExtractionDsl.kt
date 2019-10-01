@file:JvmName("PdfBoxPageExtractionDsl")

package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.contract.transformation.singleTransformation
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionConstants.TRANSFORMER_ID
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionConstants.TRANSFORMER_NAME

fun pageExtractionTransformation(targetMediaType: MediaType, parameters: Parameters): Transformation.Single =
    singleTransformation(TRANSFORMER_NAME, targetMediaType, parameters)

fun pdfBoxPageExtractionTransformation(parameters: Parameters): Transformation.Single =
    singleTransformation(TRANSFORMER_ID, APPLICATION_PDF, parameters)