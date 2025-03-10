package customize;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class SelectSelectivePlugin extends org.mybatis.generator.api.PluginAdapter {

    @Override
    public boolean validate(java.util.List<String> warnings) {
        return true;
    }

    // 添加 selectSelective 方法到 Mapper 接口
    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        Method method = new Method("");
        method.setName("selectSelective");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType("List<" + introspectedTable.getBaseRecordType() + ">"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        interfaze.addMethod(method);
        return true;
    }

    // 添加 selectSelective 的 SQL 映射
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", "selectSelective"));
        selectElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        selectElement.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));

        StringBuilder sql = new StringBuilder();
        sql.append("select <include refid=\"Base_Column_List\" /> from ");
        sql.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sql.append(" where 1=1");

        // 动态 WHERE 条件
        XmlElement whereClause = new XmlElement("where");
        for (org.mybatis.generator.api.IntrospectedColumn column : introspectedTable.getAllColumns()) {
            XmlElement ifElement = new XmlElement("if");
            // String 类型 != null and != ''
            FullyQualifiedJavaType fullyQualifiedJavaType = column.getFullyQualifiedJavaType();
            String fullyQualifiedName = fullyQualifiedJavaType.getFullyQualifiedName();
            if("java.lang.String".equals(fullyQualifiedName)){
                ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null and "
                        + column.getJavaProperty() + " != '' "));
            } else {
                ifElement.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
            }
            ifElement.addElement(new TextElement("and " + column.getActualColumnName() + " = #{" + column.getJavaProperty() + "}"));
            whereClause.addElement(ifElement);
        }
        selectElement.addElement(whereClause);

        document.getRootElement().addElement(selectElement);
        return true;
    }
}