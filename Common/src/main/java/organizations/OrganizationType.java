package organizations;

import java.io.Serializable;

public enum OrganizationType implements Serializable {
    COMMERCIAL("commercial"),
    TRUST("trust"),
    OPEN_JOINT_STOCK_COMPANY("open_joint_stock_company");
    private String name;

    OrganizationType(String str){
        this.name = str;
    }
}
