package model;

/**
 * generated at 2023-10-09 09:30:08
 */
public class MicroConfig {
    /**
     */
    private Long id;

    /**
     * 配置标识
     */
    private String key1;

    /**
     * 配置值
     */
    private String value1;

    /**
     * 应用: common special
     */
    private String application;

    /**
     * 环境: template
     */
    private String profile;

    /**
     * 分支: master
     */
    private String label;

    /**
     * 业务分类: common domain notice ...
     */
    private String configtype;

    /**
     * 描述
     */
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getConfigtype() {
        return configtype;
    }

    public void setConfigtype(String configtype) {
        this.configtype = configtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}