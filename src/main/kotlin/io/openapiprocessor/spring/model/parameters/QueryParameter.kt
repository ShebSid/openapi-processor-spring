/*
 * Copyright 2019-2020 the original authors
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

package io.openapiprocessor.spring.model.parameters

import io.openapiprocessor.core.model.datatypes.DataType
import io.openapiprocessor.core.model.parameters.ParameterBase
import io.openapiprocessor.core.model.datatypes.MappedDataType
import io.openapiprocessor.core.model.datatypes.MappedMapDataType
import io.openapiprocessor.core.model.datatypes.ObjectDataType

/**
 * OpenAPI query parameter.
 *
 * @author Martin Hauner
 */
class QueryParameter(name: String, dataType: DataType, required: Boolean, deprecated: Boolean)
    : ParameterBase(name, dataType, required, deprecated) {

    /**
     * controls if a parameter should have a {@code @RequestParam} annotation.
     */
    override val withAnnotation: Boolean
        get() {
            // Map should be annotated
            if (dataType is MappedMapDataType) {
                return true
            }

            // Pojo's should NOT be annotated
            if (dataType is ObjectDataType) {
                return false
            }

            // usually a pojo....
            if (dataType is MappedDataType) {
                return false
            }

            return true
        }

    /**
     * controls if a {@code @RequestParam} should have any parameters.
     */
    override val withParameters: Boolean
        get() {
            // Map should not have parameters
            if (dataType is MappedMapDataType) {
                return false
            }

            // Pojo should not have parameters
            if (dataType is ObjectDataType) {
                return false
            }

            return true
        }

}
