package org.babyfish.jimmer.sql.kt.ast.table.impl

import org.babyfish.jimmer.kt.toImmutableProp
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.sql.JoinType
import org.babyfish.jimmer.sql.ast.impl.Ast
import org.babyfish.jimmer.sql.ast.impl.AstContext
import org.babyfish.jimmer.sql.ast.impl.AstVisitor
import org.babyfish.jimmer.sql.ast.impl.PredicateImplementor
import org.babyfish.jimmer.sql.ast.impl.render.AbstractSqlBuilder
import org.babyfish.jimmer.sql.ast.impl.table.TableImplementor
import org.babyfish.jimmer.sql.ast.impl.table.TableSelection
import org.babyfish.jimmer.sql.ast.impl.table.WeakJoinHandle
import org.babyfish.jimmer.sql.ast.table.TableEx
import org.babyfish.jimmer.sql.kt.ast.expression.KNonNullExpression
import org.babyfish.jimmer.sql.kt.ast.expression.impl.JavaToKotlinPredicateWrapper
import org.babyfish.jimmer.sql.kt.ast.expression.impl.toJavaPredicate
import org.babyfish.jimmer.sql.kt.ast.table.*
import org.babyfish.jimmer.sql.runtime.SqlBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

internal abstract class KTableExImpl<E: Any>(
    override val javaTable: TableImplementor<E>
): Ast, KTableEx<E>, KTableImplementor<E>, TableSelection by(javaTable) {

    override fun <X : Any> outerJoin(prop: String): KNullableTableEx<X> =
        KNullableTableExImpl(javaTable.join(prop, JoinType.LEFT))

    override fun <X : Any> outerJoin(prop: ImmutableProp): KNullableTableEx<X> =
        KNullableTableExImpl(javaTable.join(prop, JoinType.LEFT))

    override fun <X : Any> outerJoinReference(prop: KProperty1<E, X?>): KNullableTableEx<X> =
        KNullableTableExImpl(javaTable.join(prop.name, JoinType.LEFT))

    override fun <X : Any> outerJoinList(prop: KProperty1<E, List<X>>): KNullableTableEx<X> =
        KNullableTableExImpl(javaTable.join(prop.name, JoinType.LEFT))

    override fun <X : Any> inverseOuterJoin(backProp: ImmutableProp): KNullableTableEx<X> =
        KNullableTableExImpl(
            javaTable.inverseJoin(backProp, JoinType.LEFT)
        )

    override fun <X : Any> inverseOuterJoinReference(backProp: KProperty1<X, E?>): KNullableTableEx<X> =
        KNullableTableExImpl(
            javaTable.inverseJoin(backProp.toImmutableProp(), JoinType.LEFT)
        )

    override fun <X : Any> inverseOuterJoinList(backProp: KProperty1<X, List<E>>): KNullableTableEx<X> =
        KNullableTableExImpl(
            javaTable.inverseJoin(backProp.toImmutableProp(), JoinType.LEFT)
        )

    override fun <X : Any> weakOuterJoin(
        targetType: KClass<X>,
        weakJoinFun: KWeakJoinFun<E, X>
    ): KNullableTableEx<X> =
        KNullableTableExImpl(
            createWeakJoinTable(javaTable, targetType.java, weakJoinFun, JoinType.LEFT)
        )

    override fun <X : Any> weakOuterJoin(
        weakJoinType: KClass<out KWeakJoin<E, X>>
    ): KNullableTableEx<X> =
        KNullableTableExImpl(
            javaTable.weakJoinImplementor(weakJoinType.java, JoinType.LEFT)
        )

    override fun <X : Any> exists(
        prop: String,
        block: KImplicitSubQueryTable<X>.() -> KNonNullExpression<Boolean>?
    ): KNonNullExpression<Boolean>? =
        javaTable.exists<TableEx<X>>(prop) {
            block(KImplicitSubQueryTableImpl(it as TableImplementor))?.toJavaPredicate()
        }?.let {
            JavaToKotlinPredicateWrapper(it as PredicateImplementor)
        }

    override fun <X : Any> exists(
        prop: ImmutableProp,
        block: KImplicitSubQueryTable<X>.() -> KNonNullExpression<Boolean>?
    ): KNonNullExpression<Boolean>? =
        javaTable.exists<TableEx<X>>(prop) {
            block(KImplicitSubQueryTableImpl(it as TableImplementor))?.toJavaPredicate()
        }?.let {
            JavaToKotlinPredicateWrapper(it as PredicateImplementor)
        }

    override fun accept(visitor: AstVisitor) {
        javaTable.accept(visitor)
    }

    override fun renderTo(builder: AbstractSqlBuilder<*>) {
        javaTable.renderTo(builder)
    }

    override fun hasVirtualPredicate(): Boolean = false

    override fun resolveVirtualPredicate(ctx: AstContext): Ast = this
}