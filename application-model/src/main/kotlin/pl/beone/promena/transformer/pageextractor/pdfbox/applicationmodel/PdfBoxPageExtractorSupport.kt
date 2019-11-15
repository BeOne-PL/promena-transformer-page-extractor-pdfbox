package pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel

import pl.beone.lib.typeconverter.applicationmodel.exception.TypeConversionException
import pl.beone.promena.transformer.applicationmodel.exception.transformer.TransformationNotSupportedException
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaType
import pl.beone.promena.transformer.applicationmodel.mediatype.MediaTypeConstants.APPLICATION_PDF
import pl.beone.promena.transformer.contract.data.DataDescriptor
import pl.beone.promena.transformer.contract.model.Parameters
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.Pages
import pl.beone.promena.transformer.pageextractor.pdfbox.applicationmodel.PdfBoxPageExtractorParametersConstants.Relaxed

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
            parameters.validate(Pages.NAME, Pages.CLASS, true)
            parameters.validatePagesParameter()
            parameters.validate(Relaxed.NAME, Relaxed.CLASS, false)
        }

        private fun Parameters.validate(name: String, clazz: Class<*>, mandatory: Boolean) {
            try {
                get(name, clazz)
            } catch (e: NoSuchElementException) {
                if (mandatory) {
                    throw TransformationNotSupportedException.mandatoryParameter(name)
                }
            } catch (e: TypeConversionException) {
                throw TransformationNotSupportedException.unsupportedParameterType(name, clazz)
            }
        }

        private fun Parameters.validatePagesParameter() {
            if (getPages().isEmpty()) {
                throw TransformationNotSupportedException.custom("Parameter <${Pages.NAME}> must contain at least <1> page")
            }
        }
    }
}