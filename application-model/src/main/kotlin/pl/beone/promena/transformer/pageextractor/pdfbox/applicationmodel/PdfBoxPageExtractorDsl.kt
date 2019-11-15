@file:JvmName("PdfBoxPageExtractorDsl")

package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.contract.transformation.Transformation
import pl.beone.promena.transformer.contract.transformation.singleTransformation
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorConstants.TRANSFORMER_ID
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorConstants.TRANSFORMER_NAME

fun pageExtractorTransformation(targetMediaType: MediaType, parameters: Parameters): Transformation.Single =
    singleTransformation(TRANSFORMER_NAME, targetMediaType, parameters)

fun pdfBoxPageExtractorTransformation(parameters: Parameters): Transformation.Single =
    singleTransformation(TRANSFORMER_ID, APPLICATION_PDF, parameters)