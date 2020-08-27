/*
 * Copyright 2020 the original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hauner.openapi.micronaut.processor

import com.github.hauner.openapi.api.OpenApiProcessor
import com.github.hauner.openapi.micronaut.writer.java.HeaderWriter
import com.github.hauner.openapi.micronaut.writer.java.MappingAnnotationWriter
import com.github.hauner.openapi.micronaut.writer.java.ParameterAnnotationWriter
import com.github.hauner.openapi.core.converter.ApiConverter
import com.github.hauner.openapi.core.writer.java.ApiWriter
import com.github.hauner.openapi.core.writer.java.BeanValidationFactory
import com.github.hauner.openapi.core.writer.java.DataTypeWriter
import com.github.hauner.openapi.core.writer.java.InterfaceWriter
import com.github.hauner.openapi.core.writer.java.MethodWriter
import com.github.hauner.openapi.core.writer.java.StringEnumWriter
import org.slf4j.LoggerFactory
import io.openapiprocessor.core.converter.ApiOptions
import io.openapiprocessor.core.parser.OpenApi
import io.openapiprocessor.core.parser.Parser
import io.openapiprocessor.core.processor.MappingConverter
import io.openapiprocessor.core.processor.MappingReader

/**
 *  Entry point of openapi-processor-micronaut.
 *
 *  @author Martin Hauner
 */
class MicronautProcessor implements OpenApiProcessor {
    private static final LOG = LoggerFactory.getLogger (MicronautProcessor)

    @Override
    String getName () {
        return 'micronaut'
    }

    @Override
    void run (Map<String, ?> processorOptions) {
        try {
            def parser = new Parser ()
            OpenApi openapi = parser.parse (processorOptions)
            if (processorOptions.showWarnings) {
                openapi.printWarnings ()
            }

            def framework = new MicronautFramework()
            def annotations = new MicronautFrameworkAnnotations()

            def options = convertOptions (processorOptions)
            def cv = new ApiConverter(options, framework)
            def api = cv.convert (openapi)

            def headerWriter = new HeaderWriter()
            def beanValidationFactory = new BeanValidationFactory()

            def writer = new ApiWriter (options,
                new InterfaceWriter(
                    headerWriter: headerWriter,
                    methodWriter: new MethodWriter(
                        mappingAnnotationWriter: new MappingAnnotationWriter (),
                        parameterAnnotationWriter: new ParameterAnnotationWriter(
                            annotations: annotations),
                        beanValidationFactory: beanValidationFactory,
                        apiOptions: options
                    ),
                    beanValidationFactory: beanValidationFactory,
                    annotations: annotations,
                    apiOptions: options
                ),
                new DataTypeWriter(
                    headerWriter: headerWriter,
                    beanValidationFactory: beanValidationFactory,
                    apiOptions: options
                ),
                new StringEnumWriter(headerWriter: headerWriter)
            )

            writer.write (api)
        } catch (Exception e) {
            LOG.error ("processing failed!", e)
            throw e
        }
    }

    private ApiOptions convertOptions (Map<String, ?> processorOptions) {
        def reader = new MappingReader ()
        def converter = new MappingConverter ()
        def mapping

        if (processorOptions.containsKey ('mapping')) {
            mapping = reader.read (processorOptions.mapping as String)
        }

        def options = new ApiOptions ()
        options.apiPath = processorOptions.apiPath
        options.targetDir = processorOptions.targetDir

        if (mapping) {
            if (mapping?.options?.packageName != null) {
                options.packageName = mapping.options.packageName
            } else {
                LOG.warn ("no 'options:package-name' set in mapping!")
            }

            if (mapping?.options?.beanValidation != null) {
                options.beanValidation = mapping.options.beanValidation
            }

            options.typeMappings = converter.convert (mapping)
        } else {
            LOG.error ("missing 'mapping.yaml' configuration!")
        }

        options
    }

}
