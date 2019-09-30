package pl.beone.promena.transformer.pageextraction.pdfbox.transformer

import org.apache.pdfbox.io.MemoryUsageSetting
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.data.TransformedDataDescriptor
import pl.beone.promena.transformer.contract.data.singleTransformedDataDescriptor
import pl.beone.promena.transformer.contract.model.Data
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextraction.pdfbox.applicationmodel.getPages
import java.io.OutputStream

internal abstract class AbstractTransformer {

    protected abstract fun getOutputStream(): OutputStream

    protected abstract fun createData(): Data

    fun transform(singleDataDescriptor: DataDescriptor.Single, targetMediaType: MediaType, parameters: Parameters): TransformedDataDescriptor.Single {
        val (data, mediaType, metadata) = singleDataDescriptor

        process(data, parameters)

        return singleTransformedDataDescriptor(createData(), metadata)
    }

    private fun process(data: Data, parameters: Parameters) {
        val pages = parameters.getPages()

        PDDocument.load(data.getInputStream()).use { document ->
            mergePages(pages.map(document::getPage))
        }
    }

    private fun mergePages(pages: List<PDPage>) {
        PDFMergerUtility()
            .apply { destinationStream = getOutputStream() }
            .also { it.addSources(pages.map(PDPage::getContents)) }
            .also { it.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly()) }
    }

}