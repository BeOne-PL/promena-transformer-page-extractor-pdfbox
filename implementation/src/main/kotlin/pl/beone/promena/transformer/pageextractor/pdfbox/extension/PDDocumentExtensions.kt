package pl.beone.promena.transformer.pageextractor.pdfbox.extension

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

fun PDDocument.getInputStream(): InputStream =
    ByteArrayInputStream(
        ByteArrayOutputStream()
            .also { outputStream -> outputStream.use(::save) }
            .toByteArray()
    )

fun PDDocument.extractPages(pageIndexes: List<Int>): List<PDPage> =
    pageIndexes.mapNotNull {
        try {
            getPage(it)
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }