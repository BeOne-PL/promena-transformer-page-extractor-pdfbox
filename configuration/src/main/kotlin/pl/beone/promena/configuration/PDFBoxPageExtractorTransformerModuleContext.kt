package pl.beone.promena.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ComponentScan(basePackages = ["pl.beone.promena.transformer.pageextractor.pdfbox.configuration"])
@PropertySource("classpath:transformer-page-extractor-pdfbox.properties")
class PDFBoxPageExtractorTransformerModuleContext