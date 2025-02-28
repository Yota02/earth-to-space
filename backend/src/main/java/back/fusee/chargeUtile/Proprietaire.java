package back.fusee.chargeUtile;

public enum Proprietaire {

    NASA("NASA"),
    ESA("ESA - Agence Spatiale Européenne"),
    ROSCOSMOS("Roscosmos - Agence Spatiale Russe"),
    CNSA("CNSA - Agence Spatiale Chinoise"),
    ISRO("ISRO - Organisation Indienne de Recherche Spatiale"),
    JAXA("JAXA - Agence Spatiale Japonaise"),
    CSA("CSA - Agence Spatiale Canadienne"),
    KARI("KARI - Agence Spatiale Sud-coréenne"),
    INMARSAT("Inmarsat - Fournisseur de Communication par Satellite"),
    SES("SES - Société de Communication par Satellite"),
    INTELSAT("Intelsat - Société de Communication par Satellite"),
    EUTELSAT("Eutelsat - Fournisseur de services satellitaires"),
    THALES_ALENIA("Thales Alenia Space - Fabricant de satellites et opérateur"),
    SPACE_X("SpaceX - Entreprise privée américaine"),
    BLUE_ORIGIN("Blue Origin - Entreprise privée américaine"),
    AMAZON("Amazon - Entreprise privée américaine (Project Kuiper)"),
    TENCENT("Tencent - Entreprise privée chinoise"),
    RELIANCE_JIO("Reliance Jio - Entreprise indienne de télécommunications"),
    O3B("O3B Networks - Fournisseur de satellites pour l'Internet"),
    ONEWEB("OneWeb - Constellation de satellites pour l'Internet"),
    IRIDIUM("Iridium Communications - Fournisseur de services de communication par satellite"),
    TELECOM_ITALIA("Telecom Italia - Entreprise italienne de télécommunications"),
    UAE_Space_Agency("UAE Space Agency - Agence spatiale des Émirats arabes unis"),
    BRICS_Satellite_Consortium("BRICS Satellite Consortium - Consortia d'agences spatiales des pays BRICS"),
    TURKSAT("Turksat - Entreprise turque de communication par satellite"),
    ASTRONOMY_ORG("Organisations astronomiques et de recherche - Plusieurs observatoires et centres de recherche");

    private final String description;

    Proprietaire(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
