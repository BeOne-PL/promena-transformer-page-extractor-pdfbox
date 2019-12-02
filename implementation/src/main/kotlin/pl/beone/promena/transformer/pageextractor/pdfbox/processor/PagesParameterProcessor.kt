package pl.beone.promena.transformer.pageextractor.pdfbox.processor

import org.apache.pdfbox.pdmodel.PDDocument
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Metadata
import pl.beone.promena.transformer.contract.model.data.Data
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.extractPages

internal class PagesParameterProcessor(
    private val pdfCreator: PdfCreator
) {

    fun process(data: Data, metadata: Metadata, pages: List<List<Int>>): List<TransformedDataDescriptor.Single> =
        PDDocument.load(data.getInputStream())
            .use { pdDocument ->
                pages
                    .map(::createIndexedPageSet)
                    .map(pdDocument::extractPages)
                    .map(pdfCreator::create)
                    .map { writableData -> singleTransformedDataDescriptor(writableData, metadata) }
            }

    private fun createIndexedPageSet(pageSet: List<Int>): List<Int> =
        pageSet.map { it - 1 } // because pages are indexed from 0
}