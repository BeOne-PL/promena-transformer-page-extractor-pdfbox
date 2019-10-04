package pl.beone.promena.transformer.pageextractor.pdfbox.transformer

import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.internal.model.data.fileData
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerDefaultParameters
import pl.beone.promena.transformer.pageextractor.pdfbox.PDFBoxPageExtractorTransformerSettings
import java.io.File
import java.io.OutputStream

internal class FileTransformer(
    settings: PDFBoxPageExtractorTransformerSettings,
    defaultParameters: PDFBoxPageExtractorTransformerDefaultParameters,
    directory: File
) : AbstractTransformer(settings, defaultParameters) {

    private val file = createTempFile(directory = directory)

    override fun getOutputStream(): OutputStream =
        file.outputStream()

    override fun createData(): Data =
        fileData(file)
}