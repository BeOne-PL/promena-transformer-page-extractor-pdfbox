package pl.beone.promena.transformer.pageextractor.pdfbox.transformer

import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.internal.model.data.memoryData
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings
import java.io.ByteArrayOutputStream
import java.io.OutputStream

internal class MemoryTransformer(
    settings: PDFBoxPageExtractorTransformerSettings,
    defaultParameters: PDFBoxPageExtractorTransformerDefaultParameters
) : AbstractTransformer(settings, defaultParameters) {

    private val outputStream = ByteArrayOutputStream()

    override fun getOutputStream(): OutputStream =
        outputStream

    override fun createData(): Data =
        memoryData(outputStream.toByteArray())
}