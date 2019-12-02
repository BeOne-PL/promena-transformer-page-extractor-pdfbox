package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.applicationmodel.exception.TypeConversionException
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.Pages
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.SplitByBarcodeMetadata

object PdfBoxPageExtractorSupport {

    @JvmStatic
    fun isSupported(dataDescriptor: DataDescriptor, targetMediaType: MediaType, parameters: Parameters) {
        dataDescriptor.descriptors.forEach { (_, mediaType) -> MediaTypeSupport.isSupported(mediaType, targetMediaType) }
        ParametersSupport.isSupported(parameters)
    }

    object MediaTypeSupport {
        private val supportedMediaType = setOf(
            APPLICATION_PDF to APPLICATION_PDF
        )

        @JvmStatic
        fun isSupported(mediaType: MediaType, targetMediaType: MediaType) {
            if (!supportedMediaType.contains(mediaType to targetMediaType)) {
                throw TransformationNotSupportedException.unsupportedMediaType(mediaType, targetMediaType)
            }
        }
    }

    object ParametersSupport {
        @JvmStatic
        fun isSupported(parameters: Parameters) {
            parameters.validate(Pages.NAME, Pages.CLASS, false)
            parameters.validate(SplitByBarcodeMetadata.NAME, SplitByBarcodeMetadata.CLASS, false)
        }

        private fun <T> Parameters.validate(
            name: String,
            clazz: Class<T>,
            mandatory: Boolean,
            valueVerifierMessage: String? = null,
            valueVerifier: (T) -> Boolean = { true }
        ) {
            try {
                val value = get(name, clazz)
                if (!valueVerifier(value)) {
                    throw TransformationNotSupportedException.unsupportedParameterValue(name, value, valueVerifierMessage)
                }
            } catch (e: NoSuchElementException) {
                if (mandatory) {
                    throw TransformationNotSupportedException.mandatoryParameter(name)
                }
            } catch (e: TypeConversionException) {
                throw TransformationNotSupportedException.unsupportedParameterType(name, clazz)
            }
        }
    }
}