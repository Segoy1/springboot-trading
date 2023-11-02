package de.segoy.springboottradingweb.security.utils;

public class RolesHierarchyBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();

    public RolesHierarchyBuilder append(String uplineRole, String downlineRole) {
        stringBuilder.append(String.format("ROLE_%s > ROLE_%s\n", uplineRole, downlineRole));
        return this;
    }

    public String build() {
        return stringBuilder.toString();
    }
}
