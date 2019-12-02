@file:JvmName("PdfBoxPageExtractorParametersDsl")

package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.internal.model.parameters.MapParameters
import pl.beone.promena.transformer.internal.model.parameters.addIfNotNull
import pl.beone.promena.transformer.internal.model.parameters.emptyParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.Pages
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.SplitByBarcodeMetadata

fun pdfBoxPageExtractorParameters(pages: List<List<Int>>? = null, splitByBarcodeMetadata: Boolean? = null): MapParameters =
    emptyParameters() addIfNotNull
            (Pages.NAME to pages) addIfNotNull
            (SplitByBarcodeMetadata.NAME to splitByBarcodeMetadata)

fun Parameters.getPages(): List<List<Int>> =
    get(Pages.NAME, Pages.CLASS)

fun Parameters.getPagesOrNull(): List<List<Int>>? =
    getOrNull(Pages.NAME, Pages.CLASS)

fun Parameters.getPagesOrDefault(default: List<List<Int>>): List<List<Int>> =
    getOrDefault(Pages.NAME, Pages.CLASS, default)

fun Parameters.getSplitByBarcodeMetadata(): Boolean =
    get(SplitByBarcodeMetadata.NAME, SplitByBarcodeMetadata.CLASS)

fun Parameters.getSplitByBarcodeMetadataOrNull(): Boolean? =
    getOrNull(SplitByBarcodeMetadata.NAME, SplitByBarcodeMetadata.CLASS)

fun Parameters.getSplitByBarcodeMetadataOrDefault(default: Boolean): Boolean =
    getOrDefault(SplitByBarcodeMetadata.NAME, SplitByBarcodeMetadata.CLASS, default)