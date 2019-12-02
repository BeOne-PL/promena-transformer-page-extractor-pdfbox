package pl.beone.promena.transformer.pageextractor.pdfbox.processor

import mu.KotlinLogging
import org.apache.pdfbox.pdmodel.PDDocument
import pl.beone.promena.transformer.barcodedetector.metadata.BarcodeDetectorMetadata
import pl.beone.promena.transformer.barcodedetector.metadata.BarcodeDetectorMetadata.Barcode
import pl.beone.promena.transformer.barcodedetector.metadata.barcodeDetectorMetadata
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Metadata
import pl.beone.promena.transformer.contract.model.data.Data
import pl.beone.promena.transformer.contract.model.data.WritableData
import pl.beone.promena.transformer.pageextractor.pdfbox.extension.extractPages

internal class SplitByBarcodeMetadataParameterProcessor(
    private val pdfCreator: PdfCreator
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private data class BarcodesWithPage(
        val barcodes: List<Barcode>,
        val pageIndex: Int
    )

    fun process(data: Data, metadata: Metadata): List<TransformedDataDescriptor.Single> =
        try {
            val barcodes = BarcodeDetectorMetadata(metadata).getBarcodes()
            val barcodesWithPage = barcodes.getWithPage()
            val barcodesWithoutPage = barcodes - barcodesWithPage

            PDDocument.load(data.getInputStream())
                .use { pdDocument ->
                    barcodesWithPage
                        .groupBy(Barcode::getPage)
                        .convertToBarcodesWithPageList()
                        .sortedBy(BarcodesWithPage::pageIndex)
                        .let(::addFirstElementWithoutBarcodesIfThereIsNotElementWithPageIndex0)
                        .let { listOfBarcodesWithPage -> addBarcodesToFirstElement(listOfBarcodesWithPage, barcodesWithoutPage) }
                        .let { listOfBarcodesWithPage -> addElementWithoutBarcodes(listOfBarcodesWithPage, pdDocument.numberOfPages) }
                        .zipWithNext()
                        .map { (pageWithBarcodes, pageWithBarcodes2) ->
                            singleTransformedDataDescriptor(
                                createDataFromPageRange(pdDocument, pageWithBarcodes.pageIndex, pageWithBarcodes2.pageIndex),
                                barcodeDetectorMetadata(pageWithBarcodes.barcodes)
                            )
                        }.toList()
                }
        } catch (e: Exception) {
            logger.debug { "There is no barcode metadata. Skipped processing" }
            listOf(singleTransformedDataDescriptor(data, metadata))
        }

    private fun List<Barcode>.getWithPage(): List<Barcode> =
        filter {
            try {
                it.getPage()
                true
            } catch (e: NoSuchElementException) {
                false
            }
        }

    private fun Map<Int, List<Barcode>>.convertToBarcodesWithPageList(): List<BarcodesWithPage> =
        toList().map { (page, barcodes) -> BarcodesWithPage(barcodes, page - 1) }

    private fun addFirstElementWithoutBarcodesIfThereIsNotElementWithPageIndex0(listOfBarcodesWithPage: List<BarcodesWithPage>): List<BarcodesWithPage> =
        if (listOfBarcodesWithPage.first().pageIndex != 0) {
            listOf(BarcodesWithPage(emptyList(), 0)) + listOfBarcodesWithPage
        } else {
            listOfBarcodesWithPage
        }

    private fun addBarcodesToFirstElement(listOfBarcodesWithPage: List<BarcodesWithPage>, barcodesWithoutPage: List<Barcode>): List<BarcodesWithPage> =
        with(listOfBarcodesWithPage) {
            listOf(BarcodesWithPage(first().barcodes + barcodesWithoutPage, first().pageIndex)) +
                    subList(1, size)
        }

    private fun addElementWithoutBarcodes(listOfBarcodesWithPage: List<BarcodesWithPage>, numberOfPages: Int): List<BarcodesWithPage> =
        listOfBarcodesWithPage + BarcodesWithPage(emptyList(), numberOfPages)

    private fun createDataFromPageRange(pdDocument: PDDocument, pageFrom: Int, pageTo: Int): WritableData =
        pdfCreator.create(
            pdDocument.extractPages((pageFrom until pageTo).toList())
        )
}