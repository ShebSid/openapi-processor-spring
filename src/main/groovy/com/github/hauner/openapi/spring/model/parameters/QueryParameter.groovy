/*
 * Copyright 2019 the original authors
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

package com.github.hauner.openapi.spring.model.parameters

import com.github.hauner.openapi.spring.model.datatypes.MapDataType
import com.github.hauner.openapi.spring.model.datatypes.MappedDataType
import com.github.hauner.openapi.spring.model.datatypes.ObjectDataType

/**
 * OpenAPI query parameter.
 *
 * @author Martin Hauner
 */
class QueryParameter extends Parameter {

    String getAnnotationName () {
        "RequestParam"
    }

    boolean isRequired () {
        if (dataType instanceof MapDataType) {
            return true
        }

        this.@required
    }

    /**
     * If the query parameter is mapped to a pojo object it should not have a {@code @RequestParam}
     * annotation.
     */
    boolean withAnnotation () {
        ! (
            dataType instanceof ObjectDataType
         || dataType instanceof MappedDataType
        )
    }

    /**
     * todo validate.
     *
     * If the query parameter is mapped to a pojo object it should not have any parameters.
     */
    boolean withParameters () {
        ! (dataType instanceof ObjectDataType)
    }

}
