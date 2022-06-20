/*
 *    Copyright 2016-2022 the original author or authors.
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
@file:Suppress("TooManyFunctions")
package org.mybatis.dynamic.sql.util.kotlin.mybatis3

import org.mybatis.dynamic.sql.BasicColumn
import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider
import org.mybatis.dynamic.sql.insert.render.GeneralInsertStatementProvider
import org.mybatis.dynamic.sql.insert.render.InsertSelectStatementProvider
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider
import org.mybatis.dynamic.sql.util.kotlin.CountCompleter
import org.mybatis.dynamic.sql.util.kotlin.DeleteCompleter
import org.mybatis.dynamic.sql.util.kotlin.GeneralInsertCompleter
import org.mybatis.dynamic.sql.util.kotlin.InsertSelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.KotlinBatchInsertCompleter
import org.mybatis.dynamic.sql.util.kotlin.KotlinInsertCompleter
import org.mybatis.dynamic.sql.util.kotlin.KotlinMultiRowInsertCompleter
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.UpdateCompleter

fun count(
    mapper: (SelectStatementProvider) -> Long,
    column: BasicColumn,
    table: SqlTable,
    completer: CountCompleter
): Long =
    mapper(count(column) {
        from(table)
        run(completer)
    })

fun countDistinct(
    mapper: (SelectStatementProvider) -> Long,
    column: BasicColumn,
    table: SqlTable,
    completer: CountCompleter
): Long =
    mapper(countDistinct(column) {
        from(table)
        run(completer)
    })

fun countFrom(mapper: (SelectStatementProvider) -> Long, table: SqlTable, completer: CountCompleter): Long =
    mapper(countFrom(table, completer))

fun deleteFrom(mapper: (DeleteStatementProvider) -> Int, table: SqlTable, completer: DeleteCompleter): Int =
    mapper(deleteFrom(table, completer))

fun <T : Any> insert(
    mapper: (InsertStatementProvider<T>) -> Int,
    row: T,
    table: SqlTable,
    completer: KotlinInsertCompleter<T>
): Int =
    mapper(insert(row) {
        into(table)
        run(completer)
    })

/**
 * This function simply inserts all rows using the supplied mapper. It is up
 * to the user to manage MyBatis3 batch processing externally. When executed with a SqlSession
 * in batch mode, the return value will not contain relevant update counts (each entry in the
 * list will be [org.apache.ibatis.executor.BatchExecutor.BATCH_UPDATE_RETURN_VALUE]).
 * To retrieve update counts, execute [org.apache.ibatis.session.SqlSession.flushStatements].
 */
fun <T : Any> insertBatch(
    mapper: (InsertStatementProvider<T>) -> Int,
    records: Collection<T>,
    table: SqlTable,
    completer: KotlinBatchInsertCompleter<T>
): List<Int> =
    insertBatch(records) {
        into(table)
        run(completer)
    }.insertStatements().map(mapper)

fun insertInto(
    mapper: (GeneralInsertStatementProvider) -> Int,
    table: SqlTable,
    completer: GeneralInsertCompleter
): Int =
    mapper(insertInto(table, completer))

fun <T : Any> insertMultiple(
    mapper: (MultiRowInsertStatementProvider<T>) -> Int,
    records: Collection<T>,
    table: SqlTable,
    completer: KotlinMultiRowInsertCompleter<T>
): Int =
    mapper(insertMultiple(records) {
        into(table)
        run(completer)
    })

fun <T : Any> insertMultipleWithGeneratedKeys(
    mapper: (String, List<T>) -> Int,
    records: Collection<T>,
    table: SqlTable,
    completer: KotlinMultiRowInsertCompleter<T>
): Int =
    insertMultiple(records) {
        into(table)
        run(completer)
    }.run {
        mapper(insertStatement, this.records)
    }

fun insertSelect(
    mapper: (InsertSelectStatementProvider) -> Int,
    table: SqlTable,
    completer: InsertSelectCompleter
): Int =
    mapper(insertSelect(table, completer))

fun <T> selectDistinct(
    mapper: (SelectStatementProvider) -> List<T>,
    selectList: List<BasicColumn>,
    table: SqlTable,
    completer: SelectCompleter
): List<T> =
    mapper(selectDistinct(selectList) {
        from(table)
        run(completer)
    })

fun <T> selectList(
    mapper: (SelectStatementProvider) -> List<T>,
    selectList: List<BasicColumn>,
    table: SqlTable,
    completer: SelectCompleter
): List<T> =
    mapper(select(selectList) {
        from(table)
        run(completer)
    })

fun <T> selectOne(
    mapper: (SelectStatementProvider) -> T?,
    selectList: List<BasicColumn>,
    table: SqlTable,
    completer: SelectCompleter
): T? =
    mapper(select(selectList) {
        from(table)
        run(completer)
    })

fun update(mapper: (UpdateStatementProvider) -> Int, table: SqlTable, completer: UpdateCompleter): Int =
    mapper(update(table, completer))
