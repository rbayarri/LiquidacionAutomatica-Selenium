package liquidacionautomatica;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HtmlElement {

    public static HtmlElement INSTANCE;
    public boolean isLocal;

    @JsonProperty("PILAGA_URL")
    public String pilagaUrl;

    @JsonProperty("LOGIN_USER_INPUT")
    public String loginUserInput;

    @JsonProperty("LOGIN_PASSWORD_INPUT")
    public String loginPasswordInput;

    @JsonProperty("CSS_LOGIN_BUTTON")
    public String cssLoginButton;

    @JsonProperty("CSS_LOGIN_OTHER_ENTITY")
    public String cssLoginOtherEntity;

    @JsonProperty("ERROR_BUTTON")
    public String errorButton;

    @JsonProperty("ROOT_BUTTON")
    public String rootButton;

    @JsonProperty("ROOT_EXPENSES")
    public String rootExpenses;

    @JsonProperty("ROOT_PURCHASES")
    public String rootPurchases;

    @JsonProperty("ROOT_DEVENGADO_PURCHASES")
    public String rootDevengadoPurchases;

    @JsonProperty("ROOT_NEW_DEVENGADO_PURCHASES")
    public String rootNewDevengadoPurchases;

    @JsonProperty("ROOT_BECAS")
    public String rootBecas;

    @JsonProperty("ROOT_DEVENGADO_BECAS")
    public String rootDevengadoBecas;

    @JsonProperty("ROOT_DEPENDENCY_DEVENGADO_BECAS")
    public String rootDependencyDevengadoBecas;

    @JsonProperty("ROOT_NEW_DEVENGADO_BECAS")
    public String rootNewDevengadoBecas;

    @JsonProperty("DOCUMENT_INPUT")
    public String documentInput;

    @JsonProperty("DOCUMENT_NUMBER_INPUT")
    public String documentNumberInput;

    @JsonProperty("DOCUMENT_YEAR_INPUT")
    public String documentYearInput;

    @JsonProperty("FILTER_BUTTON")
    public String filterButton;

    @JsonProperty("TOBA_FORM")
    public String tobaForm;

    @JsonProperty("CSS_ELEMENTS_FOUND")
    public String cssElementsFound;

    @JsonProperty("CSS_COMPROMISOS_FOUND_ROW")
    public String cssCompromisosFoundRow;

    @JsonProperty("COLLAPSE_FILTER_BUTTON")
    public String collapseFilterButton;

    @JsonProperty("CSS_COMPROMISO_ROW")
    public String cssCompromisoRow;

    @JsonProperty("ROW_SELECTION")
    public String rowSelection;

    @JsonProperty("OP_TYPE_INPUT")
    public String opTypeInput;

    @JsonProperty("FILE_TYPE_INPUT")
    public String fileTypeInput;

    @JsonProperty("FILE_NUMBER_INPUT")
    public String fileNumberInput;

    @JsonProperty("FILE_YEAR_INPUT")
    public String fileYearInput;

    @JsonProperty("CHANGE_TAB")
    public String changeTab;

    @JsonProperty("DESCRIPTION_INPUT")
    public String descriptionInput;

    @JsonProperty("PROVEEDOR_INPUT")
    public String proveedorInput;

    @JsonProperty("CHANGE_TAB_2")
    public String changeTab2;

    @JsonProperty("ADD_INVOICE_BUTTON")
    public String addInvoiceButton;

    @JsonProperty("INVOICE_TYPE_INPUT")
    public String invoiceTypeInput;

    @JsonProperty("INVOICE_NUMBER_INPUT")
    public String invoiceNumberInput;

    @JsonProperty("INVOICE_DATE_INPUT")
    public String invoiceDateInput;

    @JsonProperty("INVOICE_AMOUNT_INPUT")
    public String invoiceAmountInput;

    @JsonProperty("INVOICE_DESCRIPTION_INPUT")
    public String invoiceDescriptionInput;

    @JsonProperty("CHANGE_TAB_3")
    public String changeTab3;

    @JsonProperty("CANCEL_NEW")
    public String cancelNew;

    @JsonProperty("CHANGE_TAB_1_INCENTIVOS")
    public String changeTab1Incentivos;

    @JsonProperty("CHANGE_TAB_2_INCENTIVOS")
    public String changeTab2Incentivos;

    @JsonProperty("DESCRIPTION_INCENTIVOS_INPUT")
    public String descriptionIncentivosInput;

    @JsonProperty("BECA_PLAN_INPUT")
    public String becaPlanInput;

    @JsonProperty("BECA_DEPENDENCY_INPUT")
    public String becaDependencyInput;

    @JsonProperty("NAME_FILTER_INPUT")
    public String nameFilterInput;

    @JsonProperty("CSS_DEPENDENCY_SELECTION")
    public String cssDependencySelection;

    @JsonProperty("CSS_PARTIDAS_ROWS")
    public String cssPartidasRows;

    @JsonProperty("AVAILABLE_PARTIDA_ROW")
    public String availablePartidaRow;

    @JsonProperty("AMOUNT_PARTIDA_ROW")
    public String amountPartidaRow;

    @JsonProperty("PROCESS_BUTTON")
    public String processButton;

    @JsonProperty("CSS_ERROR_PROCESS")
    public String cssErrorProcess;

    @JsonProperty("CANCEL_BUTTON")
    public String cancelButton;

    @JsonProperty("MAIN_DOC")
    public String mainDoc;

    @JsonProperty("FINISH_BUTTON")
    public String finishButton;

    @JsonProperty("SEARCH_INPUT")
    public String searchInput;

    @JsonProperty("NEW_GROUP_BUTTON")
    public String newGroupButton;

    @JsonProperty("TYPE_OP_GROUP_INPUT")
    public String typeOpGroupInput;

    @JsonProperty("FILE_TYPE_GROUP_INPUT")
    public String fileTypeGroupInput;

    @JsonProperty("FILE_NUMBER_GROUP_INPUT")
    public String fileNumberGroupInput;

    @JsonProperty("FILE_YEAR_GROUP_INPUT")
    public String fileYearGroupInput;

    @JsonProperty("LEVEL_FROM_GROUP_INPUT")
    public String levelFromGroupInput;

    @JsonProperty("CVU_CHECK_INPUT")
    public String cvuCheckInput;

    @JsonProperty("LIQUIDATION_FILTER_GROUP_BUTTON")
    public String liquidationFilterGroupButton;

    @JsonProperty("COLLAPSE_FILTER_GROUP_BUTTON")
    public String collapseFilterGroupButton;

    @JsonProperty("GROUP_NAME_INPUT")
    public String groupNameInput;

    @JsonProperty("SELECT_ALL_GROUP_CHECK")
    public String selectAllGroupCheck;

    @JsonProperty("ADD_LIQUIDATION_TO_GROUP")
    public String addLiquidationToGroup;

    @JsonProperty("LEVEL_4_GROUP_AUTHORIZATION")
    public String level4GroupAuthorization;

    @JsonProperty("GROUP_SELECTION_AUTHORIZATION_INPUT")
    public String groupSelectionAuthorizationInput;

    @JsonProperty("GROUP_NAME_INPUT_SELECTION_GROUP_WINDOW")
    public String groupNameInputSelectionGroupWindow;

    @JsonProperty("FILTER_GROUP_WINDOW_BUTTON")
    public String filterGroupWindowButton;

    @JsonProperty("SELECT_GROUP_WINDOW_BUTTON")
    public String selectGroupWindowButton;

    @JsonProperty("FILTER_BUTTON_LEVEL_4_GROUP_AUTHORIZATION")
    public String filterButtonLevel4GroupAuthorization;

    @JsonProperty("CSS_OP_COLUMN_LEVEL_4_GROUP_AUTHORIZATION")
    public String cssOpColumnLevel4GroupAuthorization;

    @JsonProperty("LEVEL_4_AUTHORIZATION")
    public String level4Authorization;

    @JsonProperty("OP_TYPE_LEVEL_4_AUTHORIZATION")
    public String opTypeLevel4Authorization;

    @JsonProperty("OP_NUMBER_LEVEL_4_AUTHORIZATION")
    public String opNumberLevel4Authorization;

    @JsonProperty("OP_YEAR_LEVEL_4_AUTHORIZATION")
    public String opYearLevel4Authorization;

    @JsonProperty("FILTER_BUTTON_LEVEL_4_AUTHORIZATION")
    public String filterButtonLevel4Authorization;

    @JsonProperty("OP_SELECTION_LEVEL_4_AUTHORIZATION")
    public String opSelectionLevel4Authorization;

    @JsonProperty("AUTHORIZE_BUTTON_LEVEL_4_AUTHORIZATION")
    public String authorizeButtonLevel4Authorization;

    @JsonProperty("ADD_LINE_RETENTION")
    public String addLineRetention;

    @JsonProperty("RETENTION_AMOUNT")
    public String retentionAmount;

    @JsonProperty("PROCESS_RETENTION")
    public String processRetention;

    @JsonProperty("CANCEL_RETENTION_BUTTON")
    public String cancelRetentionButton;

    @JsonProperty("COLLAPSE_RETENTION_FILTER")
    public String collapseRetentionFilter;

    @JsonProperty("TAX_CONCEPT")
    public String taxConcept;

    @JsonProperty("RETENTION_FORM")
    public String retentionForm;

    @JsonProperty("TAX_REGIMEN")
    public String taxRegimen;

    @JsonProperty("TAX_BASE")
    public String taxBase;

    @JsonProperty("TAX_CALCULATE")
    public String taxCalculate;

    @JsonProperty("SELECT_ALL_LEVEL_4_GROUP")
    public String selectAllLevel4Group;

    @JsonProperty("PROCESS_LEVEL_4_AUTHORIZATION_GROUP")
    public String processLevel4AuthorizationGroup;

    @JsonProperty("LEVEL_7_GROUP_AUTHORIZATION")
    public String level7GroupAuthorization;

    @JsonProperty("CSS_LEVEL_TOTAL")
    public String cssLevelTotal;

    @JsonProperty("LIQUIDATION_LIST")
    public String liquidationList;

    @JsonProperty("FILE_TYPE_LIQUIDATION_LIST")
    public String fileTypeLiquidationList;

    @JsonProperty("FILE_YEAR_LIQUIDATION_LIST")
    public String fileYearLiquidationList;

    @JsonProperty("FILE_NUMBER_LIQUIDATION_LIST")
    public String fileNumberLiquidationList;

    @JsonProperty("FILTER_BUTTON_LIQUIDATION_LIST")
    public String filterButtonLiquidationList;

    @JsonProperty("WAITING_TIME_AFTER_LOGIN")
    public int waitingTimeAfterLogin;

    @JsonProperty("WAITING_TIME_AFTER_SCREEN_SELECTION")
    public int waitingTimeAfterScreenSelection;

    @JsonProperty("WAITING_TIME_AFTER_LOOK_FOR_COMPROMISO")
    public int waitingTimeAfterLookForCompromiso;

    @JsonProperty("WAITING_TIME_AFTER_ROW_SELECTION")
    public int waitingTimeAfterRowSelection;

    @JsonProperty("WAITING_TIME_TO_CHANGE_TAB")
    public int waitingTimeToChangeTab;

    @JsonProperty("WAITING_TIME_TO_OPEN_DEPENDENCY_WINDOW")
    public int waitingTimeToOpenDependencyWindow;

    @JsonProperty("WAITING_TIME_TO_COMPLETE_PARTIDAS")
    public int waitingTimeToCompletePartidas;

    @JsonProperty("WAITING_TIME_AFTER_PROCESS_LIQUIDATION")
    public int waitingTimeAfterProcessLiquidation;

    @JsonProperty("WAITING_TIME_AFTER_FILTERING")
    public int waitingTimeAfterFiltering;

    @JsonProperty("WAITING_TIME_TO_FILTER_GROUP_LIQUIDATIONS_LEVEL_4")
    public int waitingTimeToFilterGroupLiquidationsLevel4;

    @JsonProperty("WAITING_TIME_TO_PROCESS_RETENTION")
    public int waitingTimeToProcessRetention;

    @JsonProperty("WAITING_TIME_AFTER_TAX_SELECTION")
    public int waitingTimeAfterTaxSelection;

    @JsonProperty("WAITING_TIME_AFTER_FINISH_OP")
    public int waitingTimeAfterFinishOp;

    @JsonProperty("WAITING_TIME_BEFORE_ADD_LINE_RETENTION")
    public int waitingTimeBeforeAddLineRetention;

    public static void load(HtmlElement config) {
        INSTANCE = config;
    }
}