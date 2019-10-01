package pl.beone.promena.transformer.pageextraction.pdfbox.transformer

import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.internal.model.data.fileData
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerDefaultParameters
import pl.beone.promena.transformer.pageextraction.pdfbox.PdfBoxPageExtractionTransformerSettings
import java.io.File
import java.io.OutputStream

internal class FileTransformer(
    settings: PdfBoxPageExtractionTransformerSettings,
    defaultParameters: PdfBoxPageExtractionTransformerDefaultParameters,
    directory: File
) : AbstractTransformer(settings, defaultParameters) {

    private val file = createTempFile(directory = directory)

    override fun getOutputStream(): OutputStream =
        file.outputStream()

    override fun createData(): Data =
        fileData(file)
}