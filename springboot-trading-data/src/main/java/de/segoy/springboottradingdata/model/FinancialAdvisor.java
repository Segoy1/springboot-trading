package de.segoy.springboottradingdata.model;

public class FinancialAdvisor {

    private boolean m_rc = false;
    private String      groupsXML ;
    private String      profilesXML ;
    private String      aliasesXML ;


    public boolean isM_rc() {
        return m_rc;
    }

    public void setM_rc(boolean m_rc) {
        this.m_rc = m_rc;
    }

    public String getGroupsXML() {
        return groupsXML;
    }

    public void setGroupsXML(String groupsXML) {
        this.groupsXML = groupsXML;
    }

    public String getProfilesXML() {
        return profilesXML;
    }

    public void setProfilesXML(String profilesXML) {
        this.profilesXML = profilesXML;
    }

    public String getAliasesXML() {
        return aliasesXML;
    }

    public void setAliasesXML(String aliasesXML) {
        this.aliasesXML = aliasesXML;
    }

    public void receiveInitialXML(String faGroupXML, String faProfilesXML, String faAliasesXML) {
        //TODO Implement
    }

    public void setVisible(boolean b) {
        //TODO Implement
    }
}
