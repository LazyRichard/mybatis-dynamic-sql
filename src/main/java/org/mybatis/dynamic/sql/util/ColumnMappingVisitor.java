/**
 *    Copyright 2016-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.util;

/**
 * Visitor for all column mappings. Various column mappings are used by insert and update
 * statements. Only the null and constant mappings are supported by all statements. Other mappings
 * may or may not be supported. For example, it makes no sense to map a column to another column in
 * an insert - so the ColumnToColumnMapping is only supported on update statements.
 * 
 * @author Jeff Butler
 *
 * @param <T> The type of object created by the visitor
 */
public interface ColumnMappingVisitor<T> {
    T visit(NullMapping mapping);

    T visit(ConstantMapping mapping);

    T visit(StringConstantMapping mapping);

    default <R> T visit(ValueMapping<R> mapping) {
        return null;
    }
    
    default T visit(SelectMapping mapping) {
        return null;
    }

    default T visit(PropertyMapping mapping) {
        return null;
    }

    default T visit(ColumnToColumnMapping columnMapping) {
        return null;
    }
}
