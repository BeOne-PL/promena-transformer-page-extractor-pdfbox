package pl.beone.promena.transformer.pageextraction.pdfbox.transformer

import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.internal.model.data.fileData
import java.io.File
import java.io.OutputStream

internal class FileTransformer(
    directory: File
) : AbstractTransformer() {

    private val file = createTempFile(directory = directory)

    override fun getOutputStream(): OutputStream =
        file.outputStream()

    override fun createData(): Data =
        fileData(file)
}