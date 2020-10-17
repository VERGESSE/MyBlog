package top.vergessen.blog.config;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.helper.MySqlDialect;
import org.apache.ibatis.cache.CacheKey;

/**
 * 实现自定义Mysql分页方言，优化大表查询时默认配置查询时间长的问题
 * @author Vergessen
 * @date 2020/9/23 20:03.
 */
public class MyMysqlDialect extends MySqlDialect {

    private static final String LIMIT_SELF = "limit_table";
    private static final String LEFT_BRACKET = ")";

    @Override
    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);

        // 查询是否为优化分页
        int limitIndex = sql.lastIndexOf(LIMIT_SELF);
        if (limitIndex == -1){
            sqlBuilder.append(sql);
            if (page.getStartRow() == 0) {
                sqlBuilder.append(" LIMIT ? ");
            } else {
                sqlBuilder.append(" LIMIT ?, ? ");
            }
        }
        // 优化分页情况
        else {
            String prefix = sql.substring(0, limitIndex);
            int bracketIndex = prefix.lastIndexOf(LEFT_BRACKET);
            sqlBuilder.append(sql, 0, bracketIndex);
            if (page.getStartRow() == 0) {
                sqlBuilder.append(" LIMIT ? ");
            } else {
                sqlBuilder.append(" LIMIT ?, ? ");
            }
            sqlBuilder.append(sql, bracketIndex, sql.length());
        }

        return sqlBuilder.toString();
    }
}
