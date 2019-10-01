package pl.beone.promena.transformer.pageextraction.pdfbox.transformer

import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.internal.model.data.memoryData
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings
import java.io.ByteArrayOutputStream
import java.io.OutputStream

internal class MemoryTransformer(
    settings: PdfBoxPageExtractionTransformerSettings,
    defaultParameters: PdfBoxPageExtractionTransformerDefaultParameters
) : AbstractTransformer(settings, defaultParameters) {

    private val outputStream = ByteArrayOutputStream()

    override fun getOutputStream(): OutputStream =
        outputStream

    override fun createData(): Data =
        memoryData(outputStream.toByteArray())
}