# Promena Transformer - `page extractor - PDFBox`
This transformer provides functionality to extract range of pages from `application/pdf` documents using PDFBox 2.0.16.

Visit [Promena#Transformers](https://gitlab.office.beone.pl/promena/promena#transformers) to understand the repository structure.

## Transformation [`PdfBoxPageExtractorDsl`](./application-model/src/main/kotlin/pl/beone/promena/transformer/pageextractor/pdfbox/applicationmodel/PdfBoxPageExtractorDsl.kt), [`PdfBoxPageExtractorParametersDsl`](./application-model/src/main/kotlin/pl/beone/promena/transformer/pageextractor/pdfbox/applicationmodel/PdfBoxPageExtractorParametersDsl.kt)
The [`DataDescriptor`](https://gitlab.office.beone.pl/promena/promena/blob/master/base/promena-transformer/contract/src/main/kotlin/pl/beone/promena/transformer/contract/data/DataDescriptor.kt) has to contain at least one descriptor. If more than one descriptor is passed, the transformation will be performed on each of them separately.

## Support [`PdfBoxPageExtractorSupport`](./application-model/src/main/kotlin/pl/beone/promena/transformer/pageextractor/pdfbox/applicationmodel/PdfBoxPageExtractorSupport.kt)
### Media type [`PdfBoxPageExtractorSupport.MediaTypeSupport`](./application-model/src/main/kotlin/pl/beone/promena/transformer/pageextractor/pdfbox/applicationmodel/PdfBoxPageExtractorSupport.kt)
* `application/pdf; UTF-8` :arrow_right: `application/pdf; UTF-8`

### Parameters [`PdfBoxPageExtractorSupport.ParametersSupport`](./application-model/src/main/kotlin/pl/beone/promena/transformer/pageextractor/pdfbox/applicationmodel/PdfBoxPageExtractorSupport.kt)
* `pages`, `List<List<Int>>`, optional - extracts each list of the pages (indexed from 1) of the lists to separate [`TransformedDataDescriptor`](https://gitlab.office.beone.pl/promena/promena/blob/master/base/promena-transformer/contract/src/main/kotlin/pl/beone/promena/transformer/contract/data/TransformedDataDescriptor.kt)
* `splitByBarcodeMetadata`, `Boolean`, optional - extracts pages based on [barcode detector metadata](https://gitlab.office.beone.pl/promena/promena-transformer-barcode-detector-metadata) producing by `barcode detector` transformers. This parameter causes that the pages between subsequent barcodes are extracted to separate [`TransformedDataDescriptor`](https://gitlab.office.beone.pl/promena/promena/blob/master/base/promena-transformer/contract/src/main/kotlin/pl/beone/promena/transformer/contract/data/TransformedDataDescriptor.kt) with metadata for the given range of pages

## Dependency
```xml
<dependency>
    <groupId>pl.beone.promena.transformer</groupId>
    <artifactId>page-extractor-pdfbox-configuration</artifactId>
    <version>1.0.0</version>
</dependency>
```

### `promena-docker-maven-plugin`
```xml
<dependency>
    <groupId>pl.beone.promena.transformer</groupId>
    <artifactId>page-extractor-pdfbox</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Properties
```properties
transformer.pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer.priority=1
transformer.pl.beone.promena.transformer.pageextractor.pdfbox.PdfBoxPageExtractorTransformer.actors=1

transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.memoryUsageSetting=org.apache.pdfbox.io.MemoryUsageSetting::setupMainMemoryOnly
transformer.pl.beone.promena.transformer.pageextractor.pdfbox.settings.fallbackMemoryUsageSetting=org.apache.pdfbox.io.MemoryUsageSetting::setupTempFileOnly

transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.split-by-barcode-metadata=true
transformer.pl.beone.promena.transformer.pageextractor.pdfbox.default.parameters.timeout=
```