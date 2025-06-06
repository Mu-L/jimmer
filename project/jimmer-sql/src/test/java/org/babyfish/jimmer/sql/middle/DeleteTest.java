package org.babyfish.jimmer.sql.middle;

import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.DeleteMode;
import org.babyfish.jimmer.sql.common.AbstractMutationTest;
import org.babyfish.jimmer.sql.common.NativeDatabases;
import org.babyfish.jimmer.sql.dialect.H2Dialect;
import org.babyfish.jimmer.sql.dialect.MySqlDialect;
import org.babyfish.jimmer.sql.event.TriggerType;
import org.babyfish.jimmer.sql.model.ld.Category;
import org.babyfish.jimmer.sql.model.ld.Post;
import org.babyfish.jimmer.sql.model.ld.PostProps;
import org.babyfish.jimmer.sql.model.middle.LDValueGenerator;
import org.babyfish.jimmer.sql.model.middle.Shop;
import org.babyfish.jimmer.sql.model.middle.Vendor;
import org.babyfish.jimmer.sql.runtime.ScalarProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DeleteTest extends AbstractMutationTest {

    @Test
    public void testDeleteVendor() {
        LDValueGenerator.reset();
        executeAndExpectResult(
                getSqlClient().getEntities().deleteCommand(Vendor.class, 2L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "update shop_vendor_mapping " +
                                        "set deleted_millis = ? " +
                                        "where " +
                                        "--->vendor_id = ? " +
                                        "and " +
                                        "--->deleted_millis = ? " +
                                        "and " +
                                        "--->type = ?"
                        );
                        it.variables(100000L, 2L, 0L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update shop_vendor_mapping " +
                                        "set deleted_millis = ? " +
                                        "where " +
                                        "--->vendor_id = ? " +
                                        "and " +
                                        "--->deleted_millis = ? " +
                                        "and " +
                                        "--->type = ?"
                        );
                        it.variables(100001L, 2L, 0L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update VENDOR " +
                                        "set DELETED_MILLIS = ? " +
                                        "where ID = ?"
                        );
                        it.variables(100002L, 2L);
                    });
                }
        );
    }

    @Test
    public void testDeleteVendorWithTrigger() {

        JSqlClient sqlClient = getSqlClient(it -> it.setTriggerType(TriggerType.TRANSACTION_ONLY));
        List<String> events = new ArrayList<>();
        sqlClient.getTriggers().addAssociationListener(e -> {
            StringBuilder builder = new StringBuilder();
            builder
                    .append(e.getImmutableProp().getDeclaringType().getJavaClass().getSimpleName())
                    .append('.')
                    .append(e.getImmutableProp().getName())
                    .append(": ");
            builder.append(e.getSourceId());
            if (e.getDetachedTargetId() != null) {
                builder.append(" - ").append(e.getDetachedTargetId());
            }
            if (e.getAttachedTargetId() != null) {
                builder.append(" + ").append(e.getAttachedTargetId());
            }
            String text = builder.toString();
            int index = Collections.binarySearch(events, text);
            if (index < 0) {
                events.add(-index - 1, text);
            } else {
                events.add(index + 1, text);
            }
        });

        LDValueGenerator.reset();
        executeAndExpectResult(
                sqlClient.getEntities().deleteCommand(Vendor.class, 2L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "select shop_id " +
                                        "from shop_vendor_mapping " +
                                        "where vendor_id = ? " +
                                        "and deleted_millis = ? " +
                                        "and type = ?"
                        );
                        it.variables(2L, 0L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update shop_vendor_mapping " +
                                        "set deleted_millis = ? " +
                                        "where vendor_id = ? " +
                                        "and shop_id = ? " +
                                        "and deleted_millis = ? " +
                                        "and type = ?"
                        );
                        it.variables(100000L, 2L, 2L, 0L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "select shop_id " +
                                        "from shop_vendor_mapping " +
                                        "where vendor_id = ? " +
                                        "and deleted_millis = ? " +
                                        "and type = ?"
                        );
                        it.variables(2L, 0L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update shop_vendor_mapping " +
                                        "set deleted_millis = ? " +
                                        "where vendor_id = ? " +
                                        "and shop_id = ? " +
                                        "and deleted_millis = ? " +
                                        "and type = ?"
                        );
                        it.variables(100001L, 2L, 1L, 0L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "select tb_1_.ID, tb_1_.NAME, tb_1_.DELETED_MILLIS " +
                                        "from VENDOR tb_1_ " +
                                        "where tb_1_.ID = ? and tb_1_.DELETED_MILLIS = ?"
                        );
                        it.variables(2L, 0L);
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update VENDOR " +
                                        "set DELETED_MILLIS = ? " +
                                        "where ID = ?"
                        );
                        it.variables(100002L, 2L);
                    });
                }
        );
        Assertions.assertEquals(
                "[Shop.ordinaryVendors: 1 - 2, " +
                        "Shop.vipVendors: 2 - 2, " +
                        "Vendor.ordinaryShops: 2 - 1, " +
                        "Vendor.vipShops: 2 - 2]",
                events.toString()
        );
    }

    @Test
    public void testDeleteShop() {
        executeAndExpectResult(
                getSqlClient()
                        .getEntities()
                        .deleteCommand(Shop.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_customer_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_vendor_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_customer_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_vendor_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql("delete from SHOP where ID = ?");
                        it.variables(1L);
                    });
                }
        );
    }

    @Test
    public void testDeleteShopWithTrigger() {

        JSqlClient sqlClient = getSqlClient(it -> it.setTriggerType(TriggerType.TRANSACTION_ONLY));
        List<String> events = new ArrayList<>();
        sqlClient.getTriggers().addAssociationListener(e -> {
            StringBuilder builder = new StringBuilder();
            builder
                    .append(e.getImmutableProp().getDeclaringType().getJavaClass().getSimpleName())
                    .append('.')
                    .append(e.getImmutableProp().getName())
                    .append(": ");
            builder.append(e.getSourceId());
            if (e.getDetachedTargetId() != null) {
                builder.append(" - ").append(e.getDetachedTargetId());
            }
            if (e.getAttachedTargetId() != null) {
                builder.append(" + ").append(e.getAttachedTargetId());
            }
            String text = builder.toString();
            int index = Collections.binarySearch(events, text);
            if (index < 0) {
                events.add(-index - 1, text);
            } else {
                events.add(index + 1, text);
            }
        });

        executeAndExpectResult(
                sqlClient.getEntities().deleteCommand(Shop.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "select customer_id " +
                                        "from shop_customer_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_customer_mapping " +
                                        "where shop_id = ? and customer_id = ? and type = ?"
                        );
                        it.batchVariables(0, 1L, 2L, "ORDINARY");
                        it.batchVariables(1, 1L, 3L, "ORDINARY");
                        it.batchVariables(2, 1L, 4L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "select vendor_id " +
                                        "from shop_vendor_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_vendor_mapping " +
                                        "where shop_id = ? and vendor_id = ? and type = ?"
                        );
                        it.variables(1L, 2L, "ORDINARY");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "select customer_id " +
                                        "from shop_customer_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_customer_mapping " +
                                        "where shop_id = ? and customer_id = ? and type = ?"
                        );
                        it.variables(1L, 1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "select vendor_id " +
                                        "from shop_vendor_mapping " +
                                        "where shop_id = ? and type = ?"
                        );
                        it.variables(1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from shop_vendor_mapping " +
                                        "where shop_id = ? and vendor_id = ? and type = ?"
                        );
                        it.variables(1L, 1L, "VIP");
                    });
                    ctx.statement(it -> {
                        it.sql("select tb_1_.ID, tb_1_.NAME from SHOP tb_1_ where tb_1_.ID = ?");
                        it.variables(1L);
                    });
                    ctx.statement(it -> {
                        it.sql("delete from SHOP where ID = ?");
                        it.variables(1L);
                    });
                }
        );
        Assertions.assertEquals(
                "[Customer.ordinaryShops: 2 - 1, " +
                        "Customer.ordinaryShops: 3 - 1, " +
                        "Customer.ordinaryShops: 4 - 1, " +
                        "Customer.vipShops: 1 - 1, " +
                        "Shop.ordinaryCustomers: 1 - 2, " +
                        "Shop.ordinaryCustomers: 1 - 3, " +
                        "Shop.ordinaryCustomers: 1 - 4, " +
                        "Shop.ordinaryVendors: 1 - 2, " +
                        "Shop.vipCustomers: 1 - 1, " +
                        "Shop.vipVendors: 1 - 1, " +
                        "Vendor.ordinaryShops: 2 - 1, " +
                        "Vendor.vipShops: 1 - 1]",
                events.toString()
        );
    }

    @Test
    public void testLogicallyDeletePost() {
        executeAndExpectResult(
                getSqlClient(it -> {
                    it.setDialect(new H2Dialect());
                    it.addScalarProvider(ScalarProvider.uuidByByteArray());
                })
                        .getEntities()
                        .deleteCommand(Post.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2_category_2_mapping " +
                                        "set DELETED_UUID = ?::varbinary " +
                                        "where POST_ID = ? and DELETED_UUID = ?::varbinary"
                        );
                        it.variables(UNKNOWN_VARIABLE, 1L, new byte[16]);
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post2_item " +
                                        "set POST_ID = null " +
                                        "where POST_ID = ? and DELETED_UUID = ?::varbinary"
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2 " +
                                        "set DELETED_UUID = ?::varbinary " +
                                        "where ID = ?"
                        );
                    });
                    ctx.totalRowCount(3);
                }
        );
    }

    @Test
    public void testLogicallyDeleteCategory() {
        executeAndExpectResult(
                getSqlClient()
                        .getEntities()
                        .deleteCommand(Category.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2_category_2_mapping " +
                                        "set DELETED_UUID = ? " +
                                        "where CATEGORY_ID = ? and DELETED_UUID = ?"
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update category_2 " +
                                        "set DELETED_MILLIS = ? " +
                                        "where ID = ?"
                        );
                    });
                    ctx.totalRowCount(3);
                }
        );
    }

    @Test
    public void testPhysicallyDeletePost() {
        executeAndExpectResult(
                getSqlClient()
                        .getEntities()
                        .deleteCommand(Post.class, 1L, DeleteMode.PHYSICAL),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "delete from post_2_category_2_mapping " +
                                        "where POST_ID = ?"
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post2_item " +
                                        "set POST_ID = null " +
                                        "where POST_ID = ? and DELETED_UUID = ?"
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from post_2 " +
                                        "where ID = ?"
                        );
                    });
                    ctx.totalRowCount(3);
                }
        );
    }

    @Test
    public void testPhysicallyDeleteCategory() {
        executeAndExpectResult(
                getSqlClient()
                        .getEntities()
                        .deleteCommand(Category.class, 1L, DeleteMode.PHYSICAL),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "delete from post_2_category_2_mapping " +
                                        "where CATEGORY_ID = ?"
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "delete from category_2 " +
                                        "where ID = ?"
                        );
                    });
                    ctx.totalRowCount(3);
                }
        );
    }

    @Test
    public void testIssue1025() {
        connectAndExpect(
                con -> getSqlClient(it -> {
                    it.setDialect(new H2Dialect());
                    it.addScalarProvider(ScalarProvider.uuidByByteArray());
                }).getEntities().forConnection(con).delete(Post.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2_category_2_mapping " +
                                        "set DELETED_UUID = ?::varbinary " +
                                        "where POST_ID = ? and DELETED_UUID = ?::varbinary"
                        );
                        it.variables(UNKNOWN_VARIABLE, 1L, new byte[16]);
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post2_item " +
                                        "set POST_ID = null " +
                                        "where POST_ID = ? and DELETED_UUID = ?::varbinary"
                        );
                        it.variables(1L, new byte[16]);
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2 " +
                                        "set DELETED_UUID = ?::varbinary " +
                                        "where ID = ?"
                        );
                    });
                    ctx.value(result -> {
                        Assertions.assertEquals(3, result.getTotalAffectedRowCount());
                        Assertions.assertEquals(2, result.getAffectedRowCount(PostProps.CATEGORIES));
                        Assertions.assertEquals(1, result.getAffectedRowCount(Post.class));
                    });
                }
        );
    }

    @Test
    public void testIssue1025ByMySql() {
        NativeDatabases.assumeNativeDatabase();
        connectAndExpect(
                NativeDatabases.MYSQL_DATA_SOURCE,
                con -> getSqlClient(it ->
                        it.setDialect(new MySqlDialect())
                                .addScalarProvider(ScalarProvider.uuidByByteArray())
                ).getEntities().forConnection(con).delete(Post.class, 1L),
                ctx -> {
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2_category_2_mapping " +
                                        "set DELETED_UUID = ? " +
                                        "where POST_ID = ? and DELETED_UUID = ?"
                        );
                        it.variables(
                                UNKNOWN_VARIABLE,
                                1L,
                                new byte[16]
                        );
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post2_item " +
                                        "set POST_ID = null " +
                                        "where POST_ID = ? and DELETED_UUID = ?"
                        );
                        it.variables(1L, new byte[16]);
                    });
                    ctx.statement(it -> {
                        it.sql(
                                "update post_2 " +
                                        "set DELETED_UUID = ? " +
                                        "where ID = ?"
                        );
                        it.variables(list -> {
                            byte[] bytes = (byte[]) list.get(0);
                            Assertions.assertEquals(16, bytes.length);
                        });
                    });
                    ctx.value(result -> {
                        Assertions.assertEquals(3, result.getTotalAffectedRowCount());
                        Assertions.assertEquals(2, result.getAffectedRowCount(PostProps.CATEGORIES));
                        Assertions.assertEquals(1, result.getAffectedRowCount(Post.class));
                    });
                }
        );
    }
}
