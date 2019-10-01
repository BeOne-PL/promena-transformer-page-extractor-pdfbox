@file:JvmName("PdfBoxPageExtractionParametersDsl")

package pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel

import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.internal.model.parameters.MapParameters
import pl.beone.promena.transformer.internal.model.parameters.addIfNotNull
import pl.beone.promena.transformer.internal.model.parameters.emptyParameters
import pl.beone.promena.transformer.internal.model.parameters.plus
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionParametersConstants.Pages
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.PdfBoxPageExtractionParametersConstants.Relaxed

fun pdfBoxPageExtractionParameters(pages: List<Int>, relaxed: Boolean? = null): MapParameters =
    emptyParameters() +
            (Pages.NAME to pages) addIfNotNull
            (Relaxed.NAME to relaxed)

fun Parameters.getPages(): List<Int> =
    get(Pages.NAME, Pages.CLASS)

fun Parameters.getRelaxed(): Boolean =
    get(Relaxed.NAME, Relaxed.CLASS)

fun Parameters.getRelaxedOrDefault(default: Boolean): Boolean =
    getOrDefault(Relaxed.NAME, Relaxed.CLASS, default)