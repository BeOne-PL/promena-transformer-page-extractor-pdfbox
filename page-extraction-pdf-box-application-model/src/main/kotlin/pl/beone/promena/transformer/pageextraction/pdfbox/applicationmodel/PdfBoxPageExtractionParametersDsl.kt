@file:JvmName("PdfBoxPageExtractionParametersDsl")

package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.internal.model.parameters.MapParameters
import pl.beone.promena.transformer.internal.model.parameters.emptyParameters
import pl.beone.promena.transformer.internal.model.parameters.plus
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionParametersConstants.Pages

fun pdfBoxPageExtractionParameters(pages: List<Int>): MapParameters =
    emptyParameters() +
            (Pages.NAME to pages)

fun Parameters.getPages(): List<Int> =
    get(Pages.NAME, Pages.CLASS)