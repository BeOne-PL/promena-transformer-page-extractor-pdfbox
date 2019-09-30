package pl.beone.promena.transformer.pageextraction.pdfbox.extension

import org.apache.pdfbox.pdmodel.PDDocument
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun PDDocument.getInputStream(): InputStream =
    ByteArrayInputStream(
        ByteArrayOutputStream()
            .also { outputStream -> outputStream.use(this::save) }
            .toByteArray()
    )