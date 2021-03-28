//package com.lni.intermediary;
package ca.uhn.fhir.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A set of tools to access the MDC nomenclature codes and reference identifiers and human
 * readable descriptions. Class is for internal use by the encoder only
 */
public class Mdc {
    private static final HashMap<Long, String> mdcDictionary = new HashMap<>();
    private static final HashMap<Long, String> mdcRefIds = new HashMap<>();

    public static final long MINIMUM_32_BIT_CODE = 0x10000;

    // public static void main(String[] args) {
    //     System.out.println(get32BitCodeFromReferenceId("MDC_HF_CAD"));
    // }

    /**
     * Allows an application to add a new dictionary entry for a new nomenclature code
     * to the existing ones provided in this class. If one has this file one could always
     * add it in the existing list and not from the application.
     *
     * @param partition the 16-bit partition value
     * @param termCode  the 16-bit term code value
     * @param str       a text description for the added nomenclature term
     */
    private static void addNewDictionaryEntry(int partition, int termCode, String str) {
        mdcDictionary.put(((long) partition << 16) + (long) termCode, str);
    }

    /**
     * Allows an application to add a new reference identifier entry for a new nomenclature code
     * to the existing ones provided in this class. If one has this file one could always
     * add it in the existing list and not from the application.
     *
     * @param partition the 16-bit partition value
     * @param termCode  the 16-bit term code value
     * @param str       the reference identifier for the new nomenclature code
     */
    private static void addNewReferenceIdEntry(int partition, int termCode, String str) {
        mdcRefIds.put(((long) partition << 16) + (long) termCode, str);
    }

    /**
     * Gets the human readable textual description from the 32-bit nomenclature code
     * Due to MDC conflict this cannot be used to get the code for the BO time resolution
     *
     * @param nomenclatureCode the 32-bit nomenclature code
     * @return the text description for the code. null if not found.
     */
    public static String getStringFromCode(long nomenclatureCode) {
        String info = mdcDictionary.get(nomenclatureCode);
        if (info == null) {
            if ((nomenclatureCode & 0xFFFF) >= 0xF000) {
                info = "Unknown Private Attribute " + nomenclatureCode;
            } else {
                info = "unknown code " + nomenclatureCode;
            }
        }
        return info;
    }

    public static String getStringFromCode(int partition, int termCode) {
        return getStringFromCode((long) (partition << 16) + (long) termCode);
    }

    /**
     * Gets the reference identifier from the 32-bit nomenclature code
     * Due to MDC conflict this cannot be used to get the code for the BO time resolution
     *
     * @param nomenclatureCode the 32-bit nomenclature code
     * @return the reference identifier. null if not found
     */
    public static String getReferenceIdFromCode(long nomenclatureCode) {
        String refId = mdcRefIds.get(nomenclatureCode);
        if (refId == null) {
            if ((nomenclatureCode & 0xFFFF) >= 0xF000) {
                refId = "Private Attribute";
            }
        }
        return refId;
    }

    public static String getReferenceIdFromCode(int partition, int termCode) {
        return getReferenceIdFromCode(get32BitCode(partition, termCode));
    }

    public static long get32BitCode(int partition, int termCode) {
        return (((long) partition) << 16) + (long) termCode;
    }

    /**
     * Method takes the reference identifier as a string and returns the
     * 32-bit nomenclature code
     *
     * @param str the reference identifier
     * @return the 32-bit nomenclature code. null if not found.
     */
    public static Long get32BitCodeFromReferenceId(String str) {
        Set<Map.Entry<Long, String>> entrySet = mdcRefIds.entrySet();
        for (Map.Entry<Long, String> entry : entrySet) {
            if (entry.getValue().equalsIgnoreCase(str)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets the partition from the reference identifier
     * Due to MDC conflict this cannot be used to get the code for the BO time resolution
     *
     * @param str the reference identifier
     * @return the 16-bit partition value. null if not found
     */
    public static Integer getPartitionCodeFromReferenceId(String str) {
        Long i = get32BitCodeFromReferenceId(str);
        return (i != null) ? (int) ((i >> 16) & 0xFFFF) : null;
    }

    /**
     * Gets the term code from the reference identifier
     * Due to MDC conflict this cannot be used to get the code for the BO time resolution
     *
     * @param str the reference identifier
     * @return the 16 bit term code. null if not found.
     */
    public static Integer getTermCodeFromReferenceId(String str) {
        Long i = get32BitCodeFromReferenceId(str);
        return (i != null) ? (int) (i & 0xFFFF) : null;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static final int
            MDC_PART_OBJ = 1,
            MDC_PART_SCADA = 2,
            MDC_PART_EVT = 3,
            MDC_PART_DIM = 4,
            MDC_PART_SITES = 7,
            MDC_PART_INFRA = 8,
            MDC_PART_PHD_DM = 128,
            MDC_PART_PHD_HF = 129,
            MDC_PART_PHD_AI = 130;

    /**
     * These static classes allow one to take advantage of intellisensing IDEs to select a
     * list of possible 16- bit term codes in the given partition. The reference identifiers
     * are used for the integer names since they are standardized and more or less readable.
     *
     * @author brian
     */
    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class OBJ {

        public static final int
                PARTITION = MDC_PART_OBJ,
                MDC_MOC_VMO_METRIC = 4,
                MDC_MOC_VMO_METRIC_ENUM = 5,
                MDC_MOC_VMO_METRIC_NU = 6,
                MDC_MOC_VMO_METRIC_SA_RT = 9,
                MDC_MOC_SCAN = 16,
                MDC_MOC_SCAN_CFG = 17,
                MDC_MOC_SCAN_CFG_EPI = 18,
                MDC_MOC_SCAN_CFG_PERI = 19,
                MDC_MOC_VMS_MDS_SIMP = 37,
                MDC_MOC_VMO_PMSTORE = 61,
                MDC_MOC_PM_SEGMENT = 62,
                MDC_ATTR_AL_OP_STAT = 2310,
                MDC_ATTR_CONFIRM_MODE = 2323,
                MDC_ATTR_CONFIRM_TIMEOUT = 2324,
                MDC_ATTR_ID_HANDLE = 2337,
                MDC_ATTR_ID_INSTNO = 2338,
                MDC_ATTR_ID_LABEL_STRING = 2343,
                MDC_ATTR_ID_MODEL = 2344,
                MDC_ATTR_ID_PHYSIO = 2347,
                MDC_ATTR_ID_PROD_SPECN = 2349,
                MDC_ATTR_ID_TYPE = 2351,
                MDC_ATTR_LIMIT_CURR = 2356,
                MDC_ATTR_METRIC_STORE_CAPAC_CNT = 2369,
                MDC_ATTR_METRIC_STORE_SAMPLE_ALG = 2371,
                MDC_ATTR_METRIC_STORE_USAGE_CNT = 2372,
                MDC_ATTR_MSMT_STAT = 2375,
                MDC_ATTR_NU_ACCUR_MSMT = 2378,
                MDC_ATTR_NU_CMPD_VAL_OBS = 2379,
                MDC_ATTR_NU_VAL_OBS = 2384,
                MDC_ATTR_NUM_SEG = 2385,
                MDC_ATTR_OP_STAT = 2387,
                MDC_ATTR_POWER_STAT = 2389,
                MDC_ATTR_SA_SPECN = 2413,
                MDC_ATTR_SCAN_REP_PD = 2421,
                MDC_ATTR_SEG_USAGE_CNT = 2427,
                MDC_ATTR_SYS_ID = 2436,
                MDC_ATTR_SYS_TYPE = 2438,
                MDC_ATTR_TIME_ABS = 2439,
                MDC_ATTR_TIME_BATT_REMAIN = 2440,
                MDC_ATTR_TIME_END_SEG = 2442,
                MDC_ATTR_TIME_PD_SAMP = 2445,
                MDC_ATTR_TIME_REL = 2447,
                MDC_ATTR_TIME_STAMP_ABS = 2448,
                MDC_ATTR_TIME_STAMP_REL = 2449,
                MDC_ATTR_TIME_START_SEG = 2450,
                MDC_ATTR_TX_WIND = 2453,
                MDC_ATTR_UNIT_CODE = 2454,
                MDC_ATTR_UNIT_LABEL_STRING = 2457,
                MDC_ATTR_VAL_BATT_CHARGE = 2460, // also exists in SCADA
                MDC_ATTR_VAL_ENUM_OBS = 2462,
                MDC_ATTR_AL_COND = 2476,
                MDC_ATTR_AL_OP_TEXT_STRING = 2478,
                MDC_ATTR_TIME_REL_HI_RES = 2536,
                MDC_ATTR_TIME_STAMP_REL_HI_RES = 2537,
                MDC_ATTR_DEV_CONFIG_ID = 2628,
                MDC_ATTR_MDS_TIME_INFO = 2629,
                MDC_ATTR_METRIC_SPEC_SMALL = 2630,
                MDC_ATTR_SOURCE_HANDLE_REF = 2631,
                MDC_ATTR_SIMP_SA_OBS_VAL = 2632,
                MDC_ATTR_ENUM_OBS_VAL_SIMP_OID = 2633,
                MDC_ATTR_ENUM_OBS_VAL_SIMP_STR = 2634,
                MDC_ATTR_REG_CERT_DATA_LIST = 2635,
                MDC_ATTR_NU_VAL_OBS_BASIC = 2636,
                MDC_ATTR_PM_STORE_CAPAB = 2637,
                MDC_ATTR_PM_SEG_MAP = 2638,
                MDC_ATTR_PM_SEG_PERSON_ID = 2639,
                MDC_ATTR_SEG_STATS = 2640,
                MDC_ATTR_SEG_FIXED_DATA = 2641,
                MDC_ATTR_PM_SEG_ELEM_STAT_ATTR = 2642,
                MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP = 2643,
                MDC_ATTR_SCAN_REP_PD_MIN = 2644,
                MDC_ATTR_ATTRIBUTE_VAL_MAP = 2645,
                MDC_ATTR_NU_VAL_OBS_SIMP = 2646,
                MDC_ATTR_PM_STORE_LABEL_STRING = 2647,
                MDC_ATTR_PM_SEG_LABEL_STRING = 2648,
                MDC_ATTR_TIME_PD_MSMT_ACTIVE = 2649,
                MDC_ATTR_SYS_TYPE_SPEC_LIST = 2650,
                MDC_ATTR_METRIC_ID_PART = 2655,
                MDC_ATTR_ENUM_OBS_VAL_PART = 2656,
                MDC_ATTR_SUPPLEMENTAL_TYPES = 2657,
                MDC_ATTR_TIME_ABS_ADJUST = 2658,
                MDC_ATTR_CLEAR_TIMEOUT = 2659,
                MDC_ATTR_TRANSFER_TIMEOUT = 2660,
                MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR = 2661,
                MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR = 2662,
                MDC_ATTR_METRIC_STRUCT_SMALL = 2675,
                MDC_ATTR_NU_CMPD_VAL_OBS_SIMP = 2676,
                MDC_ATTR_NU_CMPD_VAL_OBS_BASIC = 2677,
                MDC_ATTR_ID_PHYSIO_LIST = 2678,
                MDC_ATTR_SCAN_HANDLE_LIST = 2679,
                MDC_ATTR_CONTEXT_KEY = 2680,
                MDC_ATTR_SRC_HANDLE_REF_LIST = 2681,
                MDC_REG_CERT_DATA_AUTH_BODY = 2682,
                MDC_TIME_CAP_STATE = 2683,
                MDC_TIME_SYNC_PROTOCOL = 2684,
                MDC_TIME_SYNC_ACCURACY = 2685,
                MDC_TIME_RES_ABS = 2686,
                MDC_TIME_RES_REL = 2687,
                MDC_TIME_RES_REL_HI_RES = 2688,
                MDC_ATTR_TIME_BO = 2689,
                MDC_ATTR_TIME_STAMP_BO = 2690,
                MDC_ATTR_TIME_START_SEG_BO = 2691,
                MDC_ATTR_TIME_END_SEG_BO = 2692,
                MDC_ATTR_TICK_RES = 2693,
                MDC_ATTR_THRES_NOTIF_TEXT_STRING = 2696,
                MDC_ATTR_MSMT_CONFIDENCE_95 = 2700,
                MDC_ATTR_EVENT_CONTEXT = 2702,
        // ---- Schedule store stuff
                MDC_ATTR_SCHED_SEG_PERIOD = 2819,
                MDC_ATTR_SCHED_SEG_REF_ABS_TIME = 2831,
                MDC_TIME_RES_BO = 2703,
                MDC_ACT_SEG_CLR = 3084,
                MDC_ACT_SEG_GET_INFO = 3085,
                MDC_ACT_SET_TIME = 3095,
                MDC_ACT_DATA_REQUEST = 3099,
                MDC_ACT_SEG_TRIG_XFER = 3100,
                MDC_NOTI_CONFIG = 3356,
                MDC_NOTI_SCAN_REPORT_FIXED = 3357,
                MDC_NOTI_SCAN_REPORT_VAR = 3358,
                MDC_NOTI_SCAN_REPORT_MP_FIXED = 3359,
                MDC_NOTI_SCAN_REPORT_MP_VAR = 3360,
                MDC_NOTI_SEGMENT_DATA = 3361,
                MDC_NOTI_UNBUF_SCAN_REPORT_VAR = 3362,
                MDC_NOTI_UNBUF_SCAN_REPORT_FIXED = 3363,
                MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED = 3364,
                MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR = 3365,
                MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED = 3366,
                MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED = 3367,
                MDC_NOTI_BUF_SCAN_REPORT_VAR = 3368,
                MDC_NOTI_BUF_SCAN_REPORT_FIXED = 3369,
                MDC_NOTI_BUF_SCAN_REPORT_GROUPED = 3370,
                MDC_NOTI_BUF_SCAN_REPORT_MP_VAR = 3371,
                MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED = 3372,
                MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED = 3373;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class SCADA {

        public static final int
                PARTITION = MDC_PART_SCADA,

        MDC_ECG_ELEC_POTL = 256,
                MDC_ECG_ELEC_POTL_I = 257, //	Lead I
                MDC_ECG_ELEC_POTL_II = 258, //	Lead II
                MDC_ECG_ELEC_POTL_III = 317, //	Lead III
                MDC_ECG_ELEC_POTL_AVR = 318, //	Augmented voltage right (aVR)
                MDC_ECG_ELEC_POTL_AVL = 319, //	Augmented voltage left (aVL)
                MDC_ECG_ELEC_POTL_AVF = 320, //	Augmented voltage foot (aVF)
                MDC_ECG_ELEC_POTL_V1 = 259, //	Lead V1
                MDC_ECG_ELEC_POTL_V2 = 260, //	Lead V2
                MDC_ECG_ELEC_POTL_V3 = 261, //	Lead V3
                MDC_ECG_ELEC_POTL_V4 = 262, //	Lead V4
                MDC_ECG_ELEC_POTL_V5 = 263, //	Lead V5
                MDC_ECG_ELEC_POTL_V6 = 264, //	Lead V6

        MDC_ECG_AMPL_ST = 768,
                MDC_ECG_TIME_PD_QTc = 16164,
                MDC_ECG_TIME_PD_QT_GL = 16160,
                MDC_ECG_TIME_PD_RR_GL = 16168,
                MDC_ECG_TACHY = 16616,
                MDC_ECG_ATR_FIB = 16648,
                MDC_ECG_SV_BRADY = 16912,
                MDC_ECG_V_P_C_CNT = 16993,
                MDC_ECG_SINUS_RHY = 16402,
                MDC_ECG_HEART_RATE = 16770,
                MDC_PULS_OXIM_PULS_RATE = 18458,
                MDC_PULS_RATE_NON_INV = 18474,
                MDC_PRESS_BLD_NONINV = 18948,
                MDC_PRESS_BLD_NONINV_SYS = 18949,
                MDC_PRESS_BLD_NONINV_DIA = 18950,
                MDC_PRESS_BLD_NONINV_MEAN = 18951,
                MDC_SAT_O2_QUAL = 19248,
                MDC_TEMP_BODY = 19292,
                MDC_TEMP_TYMP = 19320,
                MDC_PULS_OXIM_PERF_REL = 19376,
                MDC_PULS_OXIM_PLETH = 19380,
                MDC_PULS_OXIM_SAT_O2 = 19384,
                MDC_MODALITY_FAST = 19508,
                MDC_MODALITY_SLOW = 19512,
                MDC_PULS_OXIM_PULS_CHAR = 19533,
                MDC_MODALITY_SPOT = 19516,
                MDC_PULS_OXIM_DEV_STATUS = 19532,
                MDC_RESP_RATE = 20490,
                MDC_AWAY_RESP_RATE = 20498,
                MDC_PRESS_AWAY_MAX = 20721,
                MDC_PRESS_AWAY_MIN = 20722,
                MDC_RATIO_IE = 20760,

        MDC_PRESS_AWAY_AVG = 21395,
                MDC_PRESS_AWAY_MEAN = 21396,
                MDC_PRESS_AWAY_P50 = 21397,
                MDC_PRESS_AWAY_P90 = 21398,
                MDC_PRESS_AWAY_P95 = 21399,
                MDC_PRESS_AWAY_WAVE = 21400,
                MDC_PRESS_FLOW_AWAY_WAVE = 21401,

        MDC_CONC_GLU_GEN = 28948,
                MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD = 29112,
                MDC_CONC_GLU_CAPILLARY_PLASMA = 29116,
                MDC_CONC_GLU_VENOUS_WHOLEBLOOD = 29120,
                MDC_CONC_GLU_VENOUS_PLASMA = 29124,
                MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD = 29128,
                MDC_CONC_GLU_ARTERIAL_PLASMA = 29132,
                MDC_CONC_GLU_CONTROL = 29136,
                MDC_CONC_GLU_ISF = 29140,
                MDC_CONC_HBA1C = 29148,
                MDC_CONC_GLU_URINE = 28788, // Glucose concentration in urine
                MDC_CONC_PH_URINE = 28772, // PH grade of urine
                MDC_SPEC_GRAV_URINE = 28972, //  Specific gravity of urine
                MDC_CONC_BILIRUBIN_URINE = 29152, // Bilirubin concentration in urine
                MDC_CONC_KETONE_URINE = 29156,  //  Ketone concentration in urine
                MDC_CONC_LEUK_ESTE_URINE = 29160, // Leukocyte esterase concentration in urine
                MDC_CONC_NITRITE_URINE = 29164,  //  Nitrite concentration in urine
                MDC_CONC_OCCULT_BLOOD_URINE = 29168,  // Occult blood concentration in urine
                MDC_CONC_PROTEIN_URINE = 29172,  //  Protein concentration in urine
                MDC_CONC_UROBILINOGEN_URINE = 29176, // Urobilinogen concentration in urine
                MDC_RATIO_INR_COAG = 29188,
                MDC_TIME_PD_COAG = 29192,
                MDC_QUICK_VALUE_COAG = 29196,
                MDC_ISI_COAG = 29200,
                MDC_COAG_CONTROL = 29204,
                MDC_CONC_GLU_UNDETERMINED_WHOLEBLOOD = 29292,
                MDC_CONC_GLU_UNDETERMINED_PLASMA = 29296,
                MDC_CONC_GLU_CONTROL_LEVEL_LOW = 29300,
                MDC_CONC_GLU_CONTROL_LEVEL_MEDIUM = 29304,
                MDC_CONC_GLU_CONTROL_LEVEL_HIGH = 29308,

                MDC_ECG_STAT_RHY = 53255,
                MDC_TRIG = 53250,
                MDC_TRIG_BEAT = 53251,
                MDC_TRIG_BEAT_MAX_INRUSH = 53259,
                MDC_TEMP_RECT = 57348,
                MDC_TEMP_ORAL = 57352,
                MDC_TEMP_EAR = 57356,
                MDC_TEMP_FINGER = 57360,
                MDC_TEMP_TOE = 57376,
                MDC_TEMP_AXILLA = 57380,
                MDC_TEMP_GIT = 57384,
                MDC_MASS_BODY_ACTUAL = 57664,
                MDC_LEN_BODY_ACTUAL = 57668,
                MDC_BODY_FAT = 57676,
                MDC_RATIO_MASS_BODY_LEN_SQ = 57680,
                MDC_METRIC_NOS = 61439,
                MDC_MASS_BODY_FAT_FREE = 57684,
                MDC_MASS_BODY_SOFT_LEAN = 57688,
                MDC_BODY_WATER = 57692,
                MDC_BASAL_METABOLISM = 57696,    // Not in 20601
                MDC_BODY_MUSCLE = 57700,         // Not in 20601
                MDC_MASS_BODY_MUSCLE = 57704,    // Not in 20601
                MDC_BODY_ELECTRICAL_IMPEDANCE = 57708,      // Not in 20601
                MDC_INS_DELIV_BASAL = 58368,
                MDC_INS_DELIV_BASAL_TEMP_REL = 58372,
                MDC_INS_DELIV_BASAL_TEMP_ABS = 58276,
                MDC_INS_DELIV_BOLUS_FAST = 58380,
                MDC_INS_DELIV_BOLUS_SLOW = 58384,
                MDC_INS_DELIV_DAILY_DOSE = 58388,
                MDC_INS_DELIV_DAILY_DOSE_TOTAL = 58392,
                MDC_INS_DELIV_DAILY_DOSE_BASAL = 58396,
                MDC_INS_DELIV_DAILY_DOSE_BOLUS = 58400,
                MDC_PRIVATE_EMPATICA_CONDUCTANCE = 0xFF14,      // Not in 20601

                MDC_FLOW_AWAY = 20692,   // Gas flow in airway
                MDC_FLOW_AWAY_INSP = 20700,  // Inspiratory gas flow in airway
                MDC_VOL_AWAY_TIDAL = 20796,  // Volume of gas leaving the patient through the patient connection port during an expiratory phase.
                MDC_VOL_MINUTE_AWAY = 20808,

                MDC_VOL_AWAY = 21636,    // Integral flow of gas in airway, typically as a waveform or spirometry waveform segment
                MDC_FLOW_AWAY_EXP_FORCED_PEAK = 21512,
                MDC_FLOW_AWAY_EXP_FORCED_PEAK_PB = 21513,
                MDC_VOL_AWAY_EXP_FORCED_1S = 21514,
                MDC_VOL_AWAY_EXP_FORCED_6S = 21515,      // Ref id corrected (had extra EXP)
                MDC_CAPAC_VITAL = 20608, // Difference in volume between maximum inspiration and maximum expiration
                MDC_PRESS_AIR_AMBIENT = 21764,

                MDC_TEMP_ROOM = 57436,
                MDC_VOL_AWAY_EXP_FORCED_CAPACITY = 57856,
                MDC_VOL_AWAY_SLOW_CAPACITY = 57860,
                MDC_RATIO_AWAY_EXP_FORCED_FEV1_FEV6 = 57864,
                MDC_VOL_AWAY_EXP_FORCED_0_5S = 57868,
                MDC_VOL_AWAY_EXP_FORCED_0_75S = 57872,
                MDC_RATIO_AWAY_EXP_FORCED_1S_FVC = 57876,
                MDC_RATIO_AWAY_EXP_FORCED_0_5S_FVC = 57880,
                MDC_RATIO_AWAY_EXP_FORCED_0_75S_FVC = 57884,
                MDC_FLOW_AWAY_EXP_FORCED_25_75_FVC = 57888,
                MDC_FLOW_AWAY_EXP_FORCED_25_FVC = 57892,
                MDC_FLOW_AWAY_EXP_FORCED_50_FVC = 57896,
                MDC_FLOW_AWAY_EXP_FORCED_75_FVC = 57900,
                MDC_FLOW_AWAY_INSP_FORCED_PEAK = 57904,
                MDC_VOL_AWAY_INSP_FORCED_CAPACITY = 57908,
                MDC_VOL_AWAY_INSP_FORCED_1S = 57912,
                MDC_FLOW_AWAY_INSP_FORCED_25 = 57916,
                MDC_FLOW_AWAY_INSP_FORCED_50 = 57920,
                MDC_FLOW_AWAY_INSP_FORCED_75 = 57924,

                MDC_VOL_AWAY_INSP_CAPACITY = 57928,
                MDC_VOL_AWAY_EXP_RESERVE = 57932,
                MDC_VOL_AWAY_INSP_RESERVE = 57936,
                MDC_VOL_AWAY_INSP_SLOW_CAPACITY = 57940,
                MDC_VOL_AWAY_EXP_FORCED_TIME = 57944,
                MDC_VOL_AWAY_EXTRAP = 57948,
                MDC_AWAY_BTPS = 57952,
                MDC_REL_HUMIDITY_AMBIENT = 57732, //57960

                MDC_VOL_AWAY_EXP_SLOW_CAPACITY = 57964,
                MDC_VOL_AWAY_EXP_FORCED_2S = 57968,
                MDC_VOL_AWAY_EXP_FORCED_3S = 57972,
                MDC_VOL_AWAY_EXP_FORCED_5S = 57976,
                MDC_RATIO_AWAY_EXP_FORCED_2S_FVC = 57980,
                MDC_RATIO_AWAY_EXP_FORCED_3S_FVC = 57984,
                MDC_RATIO_AWAY_EXP_FORCED_5S_FVC = 57988,
                MDC_RATIO_AWAY_EXP_FORCED_6S_FVC = 57992,
                MDC_FLOW_AWAY_EXP_FORCED_MAX = 57996,
                MDC_FLOW_AWAY_EXP_FORCED_25_50 = 58000,
                MDC_FLOW_AWAY_EXP_FORCED_75_85 = 58004,
                MDC_FLOW_AWAY_EXP_FORCED_0_2L_1_2L = 58008,
                MDC_FLOW_AWAY_EXP_FORCED_85 = 58012,
                MDC_VOL_AWAY_EXP_TIDAL_TIME = 58016,
                MDC_VOL_AWAY_INSP_TIDAL_TIME = 58020,
                MDC_RATIO_AWAY_TIN_TEX = 58024,
                MDC_FLOW_AWAY_INSP_FORCED_25_50 = 58028,
                MDC_FLOW_AWAY_INSP_FORCED_25_75 = 58032,
                MDC_RATIO_AWAY_INSP_FORCED_1S_FIVC = 58036,
                MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_12S = 58040,
                MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_15S = 58044,
                MDC_VOL_AWAY_EXP_25_75_TIME = 58048,
                MDC_FLOW_AWAY_EXP_PEAK_TIME = 58052,
                MDC_FLOW_AWAY_EXP_TIDAL_MEAN = 58056,

                MDC_PRIVATE_HR_BP_PROD = 0xF700,
                MDC_PRIVATE_HR_VARIABILITY = 0xF701,
                MDC_PRIVATE_SPIROMETRY_STATUS = 0xF71A,
                MDC_PRIVATE_SPIROMETRY_TYPE = 0xF71C,
                MDC_PRIVATE_SPIROMETRY_TYPE_FVC = 0xF71D,
                MDC_PRIVATE_SPIROMETRY_TYPE_VC = 0xF71E,
                MDC_PRIVATE_SPIROMETRY_TYPE_FVC_VC = 0xF71F;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class EVT {

        public static final int
                PARTITION = MDC_PART_EVT,
                MDC_EVT_PEF_POST_MED = 21106,
                MDC_EVT_PEF_POST_MED_TRUE = 21107,
                MDC_EVT_PEF_POST_MED_FALSE = 21108,
                MDC_EVT_PEF_LONG_TIME_TO_PEAK = 21109,
                MDC_EVT_PEF_LONG_TIME_TO_PEAK_TRUE = 21110,
                MDC_EVT_PEF_LONG_TIME_TO_PEAK_FALSE = 21111,
                MDC_EVT_PEF_SHORT_EFFORT = 21112,
                MDC_EVT_PEF_SHORT_EFFORT_TRUE = 21113,
                MDC_EVT_PEF_SHORT_EFFORT_FALSE = 21114,
                MDC_EVT_PEF_COUGH = 21115,
                MDC_EVT_PEF_COUGH_TRUE = 21116,
                MDC_EVT_PEF_COUGH_FALSE = 21117;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class DIM {

        public static final int
                PARTITION = MDC_PART_DIM,
                MDC_DIM_DIMLESS = 512,
                MDC_DIM_PERCENT = 544,
                MDC_DIM_ANG_DEG = 736,
                MDC_DIM_PH = 992,
                MDC_DIM_BEAT = 1088,
                MDC_DIM_M = 1280,
                MDC_DIM_CENTI_M = 1297,
                MDC_DIM_FOOT = 1344,
                MDC_DIM_INCH = 1376,
                MDC_DIM_L = 1600,
                MDC_DIM_CENTI_L = 1617,
                MDC_DIM_MILLI_L = 1618,
                MDC_DIM_G = 1728,
                MDC_DIM_KILO_G = 1731,
                MDC_DIM_MILLI_G = 1746,
                MDC_DIM_LB = 1760,
                MDC_DIM_KG_PER_M_SQ = 1952,
                MDC_DIM_MILLI_G_PER_DL = 2130,
                MDC_DIM_SEC = 2176,
                MDC_DIM_MILLI_SEC = 2194,
                MDC_DIM_MICRO_SEC = 2195,
                MDC_DIM_MIN = 2208,
                MDC_DIM_HR = 2240,
                MDC_DIM_DAY = 2272,
                MDC_DIM_YR = 2368,
                MDC_DIM_BEAT_PER_MIN = 2720,
                MDC_DIM_RESP_PER_MIN = 2784,
                MDC_DIM_M_PER_SEC = 2816,
                MDC_DIM_L_PER_MIN_PER_M_SQ = 2848, // 2848 is this in 10101R
                MDC_DIM_L_PER_MIN = 3072,  // which is correct 2848 0r 3072? 3072 is in 10101R
                MDC_DIM_CENTI_L_PER_SEC = 3057,
                MDC_DIM_HECTO_PASCAL = 3842,
                MDC_DIM_KILO_PASCAL = 3843,
                MDC_DIM_HECTO_PASCAL_PER_SEC = 3850,
                MDC_DIM_MMHG = 3872,
                MDC_DIM_JOULES = 3968,
                MDC_DIM_WATT = 4032,
                MDC_DIM_NANO_WATT = 4052, // Add 20 to WATT
                MDC_DIM_VOLT = 4256,
                MDC_DIM_MILLI_VOLT = 4274,
                MDC_DIM_OHM = 4288,
                MDC_DIM_MEGA_OHM = 4292,
                MDC_DIM_KELVIN = 4384,
                MDC_DIM_FAHR = 4416,
                MDC_DIM_MILLI_MOLE_PER_L = 4722,
                MDC_DIM_MICRO_MOLE_PER_L = 4723,
                MDC_DIM_MILLI_G_PER_DL_PER_MIN = 4724,
                MDC_DIM_MILLI_MOLE_PER_L_PER_MIN = 4728,
                MDC_DIM_EVT_PER_HR = 4732,
                MDC_DIM_INTL_UNIT = 5472,
                MDC_DIM_INTL_UNIT_PER_HR = 5696,
                MDC_DIM_DEGC = 6048,
                MDC_DIM_DECIBEL = 6432,
                MDC_DIM_M_PER_MIN = 6560,
                MDC_DIM_INR = 6608,
                MDC_DIM_M_PER_SEC_SQ = 6624,
                MDC_DIM_STEP = 6656,
                MDC_DIM_FOOT_PER_MIN = 6688,
                MDC_DIM_INCH_PER_MIN = 6720,
                MDC_DIM_STEP_PER_MIN = 6752,
                MDC_DIM_CAL = 6784,        // Wrong in 10441)
                MDC_DIM_NUTRI_CAL = 8384,   // According to new draft 10101 (wrong in 10441 on value and ref id)
                MDC_DIM_RPM = 6816,
                MDC_DIM_TICK = 6848,
                MDC_DIM_PRIVATE_MMHG_BEAT_PER_MIN = 0xF700, // this is a Blood pressure - beats special measure on the bodimetrics
                MDC_DIM_PRIVATE_MICRO_SIEMENS = 0xFF20;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class SITES {

        public static final int
                PARTITION = MDC_PART_SITES,
                MDC_MUSC_SKELETAL = 248,
                MDC_MUSC_HEAD = 252,
                MDC_MUSC_HEAD_EYE = 256,
                MDC_MUSC_HEAD_RECT_SUP = 260,
                MDC_MUSC_HEAD_RECT_INF = 264,
                MDC_MUSC_HEAD_RECT_MED = 268,
                MDC_MUSC_HEAD_RECT_LAT = 272,
                MDC_MUSC_HEAD_OBLIQ_SUP = 276,
                MDC_MUSC_HEAD_OBLIQ_INF = 280,
                MDC_MUSC_HEAD_FACIAL = 284,
                MDC_MUSC_HEAD_OCCIPITOFRONT_VENTER = 288,
                MDC_MUSC_HEAD_ORBIC_OCUL = 292,
                MDC_MUSC_HEAD_ORBIC_OCUL_PARS_ORBIT = 296,
                MDC_MUSC_HEAD_AURIC_POST = 300,
                MDC_MUSC_HEAD_ORBIC_ORIS = 304,
                MDC_MUSC_HEAD_DEPRESSOR_ANGUL_ORIS = 308,
                MDC_MUSC_HEAD_RISOR = 312,
                MDC_MUSC_HEAD_ZYGOMATIC_MAJOR = 316,
                MDC_MUSC_HEAD_ZYGOMATIC_MINOR = 320,
                MDC_MUSC_HEAD_LEVATOR_LAB_SUP = 324,
                MDC_MUSC_HEAD_LEVATOR_LAB_SUP_AL_NASI = 328,
                MDC_MUSC_HEAD_DEPRESSOR_LAB_INF = 332,
                MDC_MUSC_HEAD_LEVATOR_ANGUL_ORIS = 336,
                MDC_MUSC_HEAD_BUCCINATOR = 340,
                MDC_MUSC_HEAD_MENTAL = 344,
                MDC_MUSC_HEAD_MASSETER = 348,
                MDC_MUSC_HEAD_TEMPOR = 352,
                MDC_MUSC_HEAD_PTERYGOID = 356,
                MDC_MUSC_HEAD_PTERYGOID_LAT = 360,
                MDC_MUSC_HEAD_PTERYGOID_MED = 364,
                MDC_MUSC_HEAD_LING = 368,
                MDC_MUSC_HEAD_GENIOGLOSS = 372,
                MDC_MUSC_HEAD_LARING = 376,
                MDC_MUSC_HEAD_CRICOTHYROID = 380,
                MDC_MUSC_HEAD_THYROARYTEROID = 384,
                MDC_MUSC_NECK = 388,
                MDC_MUSC_NECK_PLATYSMA = 392,
                MDC_MUSC_NECK_CAPT_LONG = 396,
                MDC_MUSC_NECK_STERNOCLEIDOMASTOID = 400,
                MDC_MUSC_NECK_DIGRASTRIC = 404,
                MDC_MUSC_NECK_DIGRASTRIC_VENTER_ANT = 408,
                MDC_MUSC_NECK_DIGRASTRIC_VENTER_POST = 412,
                MDC_MUSC_NECK_MYLOHYOID = 416,
                MDC_MUSC_TRUNK = 420,
                MDC_MUSC_BACK = 424,
                MDC_MUSC_BACK_UPPER = 428,
                MDC_MUSC_BACK_LOWER = 432,
                MDC_MUSC_BACK_TRAPEZ = 436,
                MDC_MUSC_BACK_LASTISSIM_DORS = 440,
                MDC_MUSC_BACK_RHOMB_MAJOR = 444,
                MDC_MUSC_BACK_RHOMB_MINOR = 448,
                MDC_MUSC_BACK_SCAP_LEVATOR = 452,
                MDC_MUSC_BACK_SERRAT_POST = 456,
                MDC_MUSC_BACK_SPLEN_CAPT = 460,
                MDC_MUSC_BACK_SPLEN_CERVIC = 464,
                MDC_MUSC_BACK_SPLEN = 468,
                MDC_MUSC_BACK_SPINAL_ERECTOR = 472,
                MDC_MUSC_BACK_SPINAL = 476,
                MDC_MUSC_BACK_SPINAL_THORAC = 480,
                MDC_MUSC_BACK_SPINAL_CERVIC = 484,
                MDC_MUSC_BACK_SPINAL_CAPIT = 488,
                MDC_MUSC_BACK_SEMISPINAL = 492,
                MDC_MUSC_BACK_SEMISPINAL_THOR = 496,
                MDC_MUSC_BACK_SEMISPINAL_CERV = 500,
                MDC_MUSC_BACK_SEMISPINAL_CAPIT = 504,
                MDC_MUSC_BACK_MULTIFID = 508,
                MDC_MUSC_BACK_INTERSPINAL = 512,
                MDC_MUSC_BACK_INTERSPINAL_CERVIC = 516,
                MDC_MUSC_BACK_INTERSPINAL_THORAC = 520,
                MDC_MUSC_BACK_INTERSPINAL_LUMBOR = 524,
                MDC_MUSC_THORAX = 528,
                MDC_MUSC_THORAX_PECTORAL_MAJOR = 532,
                MDC_MUSC_THORAX_PECTORAL_MINOR = 536,
                MDC_MUSC_THORAX_SUBCLAV = 540,
                MDC_MUSC_THORAX_SERRAT_ANT = 544,
                MDC_MUSC_THORAX_INTERCOSTAL = 548,
                MDC_MUSC_THORAX_DIAPHRAGM = 552,
                MDC_MUSC_ABDOM = 556,
                MDC_MUSC_ABDOM_ABDOMIN = 560,
                MDC_MUSC_ABDOM_OBLIQ_EXT = 564,
                MDC_MUSC_ABDOM_OBLIQ_INT = 568,
                MDC_MUSC_ABDOM_ABDOM_TRANSVERS = 572,
                MDC_MUSC_ABDOM_LUMBOR_QUADRAT = 576,
                MDC_MUSC_ABDOM_PELV = 580,
                MDC_MUSC_ABDOM_PUBORECT = 584,
                MDC_MUSC_ABDOM_COCCYG = 588,
                MDC_MUSC_ABDOM_ANI_SPHINCTER = 592,
                MDC_MUSC_ABDOM_ANI_SPHINCTER_EXT = 596,
                MDC_MUSC_UPEXT = 600,
                MDC_MUSC_UPEXT_DELTOID = 604,
                MDC_MUSC_UPEXT_SUPRASPINAT = 608,
                MDC_MUSC_UPEXT_INFRASPINAT = 612,
                MDC_MUSC_UPEXT_TERES_MINOR = 616,
                MDC_MUSC_UPEXT_TERES_MAJOR = 620,
                MDC_MUSC_UPEXT_SUBSCAP = 624,
                MDC_MUSC_UPEXT_BRACHI_BICEPS = 628,
                MDC_MUSC_UPEXT_BRACHIAL = 632,
                MDC_MUSC_UPEXT_CORACOBRACH = 636,
                MDC_MUSC_UPEXT_BRACH_TRICEPS = 640,
                MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LONG = 644,
                MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LAT = 648,
                MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_MED = 652,
                MDC_MUSC_UPEXT_ANCON = 656,
                MDC_MUSC_UPEXT_PRONATOR = 660,
                MDC_MUSC_UPEXT_FLEX_CARPI_RADIAL = 664,
                MDC_MUSC_UPEXT_PALMAR_LONG = 668,
                MDC_MUSC_UPEXT_FLEX_CARPI_ULNAR = 672,
                MDC_MUSC_UPEXT_FLEX_DIGIT_SUPERF = 676,
                MDC_MUSC_UPEXT_FLEX_DIGIT_PROFUND = 680,
                MDC_MUSC_UPEXT_FLEX_POLLIC_LONG = 684,
                MDC_MUSC_UPEXT_PRONATOR_QUADRAT = 688,
                MDC_MUSC_UPEXT_BRACHIORADIAL = 692,
                MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_LONG = 696,
                MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_BREV = 700,
                MDC_MUSC_UPEXT_EXTENS_DIGIT = 704,
                MDC_MUSC_UPEXT_EXTENS_DIGIT_MIN = 708,
                MDC_MUSC_UPEXT_EXTENS_CARP_ULNAR = 712,
                MDC_MUSC_UPEXT_SUPINATOR = 716,
                MDC_MUSC_UPEXT_ABDUC_POLLIC_LONG = 720,
                MDC_MUSC_UPEXT_EXTENS_POLLIC_BREV = 724,
                MDC_MUSC_UPEXT_EXTENS_POLLIC_LONG = 728,
                MDC_MUSC_UPEXT_EXTENS_INDIC = 732,
                MDC_MUSC_UPEXT_PALMAR_BREV = 736,
                MDC_MUSC_UPEXT_ABDUC_POLLIC_BREV = 740,
                MDC_MUSC_UPEXT_FLEX_POLLIC_BREV = 744,
                MDC_MUSC_UPEXT_OPPON_POLLIC = 748,
                MDC_MUSC_UPEXT_ADDUC_POLLIC = 752,
                MDC_MUSC_UPEXT_ABDUC_DIGIT_MIN = 756,
                MDC_MUSC_UPEXT_FLEX_DIGIT_BREV_MIN = 760,
                MDC_MUSC_UPEXT_OPPON_DIGIT_MIN = 764,
                MDC_MUSC_UPEXT_LUMBRICAL = 768,
                MDC_MUSC_UPEXT_INTEROSS_DORSAL = 772,
                MDC_MUSC_UPEXT_INTEROSS_PALMAR = 776,
                MDC_MUSC_LOEXT_HIP_THIGH = 780,
                MDC_MUSC_LOEXT_LEG = 784,
                MDC_MUSC_LOEXT_FOOT = 788,
                MDC_MUSC_LOEXT_ILLIOPS = 792,
                MDC_MUSC_LOEXT_GLUT_MAX = 796,
                MDC_MUSC_LOEXT_GLUT_MED = 800,
                MDC_MUSC_LOEXT_GLUT_MIN = 804,
                MDC_MUSC_LOEXT_TENSOR_FASC_LAT = 808,
                MDC_MUSC_LOEXT_PIRIFORM = 812,
                MDC_MUSC_LOEXT_OBTURATOR = 816,
                MDC_MUSC_LOEXT_GEMEL = 820,
                MDC_MUSC_LOEXT_QUADRAT_FEMOR = 824,
                MDC_MUSC_LOEXT_SARTOR = 828,
                MDC_MUSC_LOEXT_QUADRICEPS_FEMOR = 832,
                MDC_MUSC_LOEXT_RECT_FEMOR = 836,
                MDC_MUSC_LOEXT_VAST_LAT = 840,
                MDC_MUSC_LOEXT_VAST_INTERMED = 844,
                MDC_MUSC_LOEXT_VAST_MED = 848,
                MDC_MUSC_LOEXT_PECTIN = 852,
                MDC_MUSC_LOEXT_ABDUC_LONG = 856,
                MDC_MUSC_LOEXT_ABDUC_BREV = 860,
                MDC_MUSC_LOEXT_ABDUC_MAGN = 864,
                MDC_MUSC_LOEXT_GRACIL = 868,
                MDC_MUSC_LOEXT_BICEPS_FEMOR = 872,
                MDC_MUSC_LOEXT_BICEPS_FEMOR_LONG = 876,
                MDC_MUSC_LOEXT_BICEPS_FEMOR_BREV = 880,
                MDC_MUSC_LOEXT_SEMITENDIN = 884,
                MDC_MUSC_LOEXT_SEMIMEMBRAN = 888,
                MDC_MUSC_LOEXT_TIBIAL_ANT = 892,
                MDC_MUSC_LOEXT_EXTENS_DIGIT_LONG = 896,
                MDC_MUSC_LOEXT_EXTENS_HALLUC_LONG = 900,
                MDC_MUSC_LOEXT_PERON = 904,
                MDC_MUSC_LOEXT_PERON_LONG = 908,
                MDC_MUSC_LOEXT_PERON_BREV = 912,
                MDC_MUSC_LOEXT_TRICEPS_SUR = 916,
                MDC_MUSC_LOEXT_GASTROCNEM = 920,
                MDC_MUSC_LOEXT_GASTROCNEM_LAT = 924,
                MDC_MUSC_LOEXT_GASTROCNEM_MED = 928,
                MDC_MUSC_LOEXT_SOL = 932,
                MDC_MUSC_LOEXT_PLANTAR = 936,
                MDC_MUSC_LOEXT_POPLIT = 940,
                MDC_MUSC_LOEXT_TIBIAL_POST = 944,
                MDC_MUSC_LOEXT_FLEX_DIGIT_LONG = 948,
                MDC_MUSC_LOEXT_EXTENS_HALLUC_BREV = 952,
                MDC_MUSC_LOEXT_EXTENS_DIGIT_BREV = 956,
                MDC_MUSC_LOEXT_ABDUC_HALLUC = 960,
                MDC_MUSC_LOEXT_FLEX_HALLUC_BREV = 964,
                MDC_MUSC_LOEXT_ADDUC_HALLUC = 968,
                MDC_MUSC_LOEXT_ABDUC_DIGIT_MIN = 972,
                MDC_MUSC_LOEXT_FLEX_DIGIT_BREV_MIN = 976,
                MDC_MUSC_LOEXT_QUADRAT_PLANT = 980,
                MDC_MUSC_LOEXT_LUMBRICAL = 984,
                MDC_MUSC_LOEXT_INTEROSS_DORSAL = 988,
                MDC_MUSC_LOEXT_INTEROSS_PLANTAR = 992,
                MDC_LOEXT_LEG = 1604,
                MDC_LOEXT_LEG_L = 1605,
                MDC_LOEXT_LEG_R = 1606,
                MDC_LOEXT_THIGH = 1612,
                MDC_LOEXT_THIGH_L = 1613,
                MDC_LOEXT_THIGH_R = 1614,
                MDC_TRUNK_BREAST = 1664,    // BTLE HR monitor Chest
                MDC_UPEXT_FOREARM = 1768,
                MDC_UPEXT_FOREARM_L = 1769,
                MDC_UPEXT_FOREARM_R = 1770,
                MDC_UPEXT_ARM_UPPER = 1780,
                MDC_UPEXT_ARM_UPPER_L = 1781,
                MDC_UPEXT_ARM_UPPER_R = 1782,
                MDC_UPEXT_WRIST = 1784,
                MDC_UPEXT_FINGER = 1748,
                MDC_UPEXT_HAND = 1772,
                MDC_HEAD_EAR = 1520,        // ear lobe
                MDC_LOEXT_FOOT = 1588;
    }

    @SuppressWarnings({"SpellCheckingInspection"})
    public static class INFRA {

        public static final int
                PARTITION = MDC_PART_INFRA,
                MDC_DEV_SPEC_PROFILE_HYDRA = 4096,
                MDC_DEV_SPEC_PROFILE_PULS_OXIM = 4100,
                MDC_DEV_SPEC_PROFILE_MIN_ECG = 4102,
                MDC_DEV_SPEC_PROFILE_BP = 4103,
                MDC_DEV_SPEC_PROFILE_TEMP = 4104,
                MDC_DEV_SPEC_PROFILE_RESP_RATE = 4109,
                MDC_DEV_SPEC_PROFILE_SCALE = 4111,
                MDC_DEV_SPEC_PROFILE_GLUCOSE = 4113,
                MDC_DEV_SPEC_PROFILE_COAG = 4114,
                MDC_DEV_SPEC_PROFILE_INSULIN_PUMP = 4115,
                MDC_DEV_SPEC_PROFILE_BCA = 4116,
                MDC_DEV_SPEC_PROFILE_PEAK_FLOW = 4117,//public static final INFRA, 4118, "DEV_SPEC_PROFILE_COAG");        // This is an error but in the spec
                MDC_DEV_SPEC_PROFILE_URINE = 4118,
                MDC_DEV_SPEC_PROFILE_SABTE = 4120,
                MDC_DEV_SPEC_PROFILE_PSM = 4124,
                MDC_DEV_SUB_SPEC_PROFILE_CPAP = 4244,
                MDC_DEV_SUB_SPEC_PROFILE_CPAP_AUTO = 4245,
                MDC_DEV_SUB_SPEC_PROFILE_BPAP = 4246,
                MDC_DEV_SUB_SPEC_PROFILE_BPAP_AUTO = 4247,  // For future use
                MDC_DEV_SUB_SPEC_PROFILE_ACSV = 4248,    // For future use
                MDC_DEV_SPEC_PROFILE_CGM = 4121,
                MDC_DEV_SPEC_PROFILE_HF_CARDIO = 4137,
                MDC_DEV_SPEC_PROFILE_HF_STRENGTH = 4138,
                MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB = 4167,
                MDC_DEV_SPEC_PROFILE_AI_MED_MINDER = 4168,
                MDC_DEV_SPEC_PROFILE_GENERIC = 4169,
                MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER = 4200,
                MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR = 4213,
                MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR = 4214,
                MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR = 4215,
                MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR = 4216,
                MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR = 4217,
                MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR = 4218,
                MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR = 4219,
                MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR = 4220,
                MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR = 4221,
                MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR = 4222,
                MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR = 4223,
                MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR = 4224,
                MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR = 4225,
                MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR = 4226,
                MDC_DEV_SUB_SPEC_PROFILE_ECG = 4236,
                MDC_DEV_SUB_SPEC_PROFILE_HR = 4237,
                MDC_ID_MODEL_NUMBER = 7681,
                MDC_ID_MODEL_MANUFACTURER = 7682,
                MDC_ID_PROD_SPEC_UNSPECIFIED = 7683,
                MDC_ID_PROD_SPEC_SERIAL = 7684,
                MDC_ID_PROD_SPEC_PART = 7685,
                MDC_ID_PROD_SPEC_HW = 7686,
                MDC_ID_PROD_SPEC_SW = 7687,
                MDC_ID_PROD_SPEC_FW = 7688,
                MDC_ID_PROD_SPEC_PROTOCOL_REV = 7689,
                MDC_ID_PROD_SPEC_GMDN = 7690,
                MDC_MODALITY_AVERAGING_TIME = 7691,
                MDC_SA_SPECN_FLAGS = 7692,
                MDC_MOC_VMS_MDS_AHD = 7693,
                MDC_TIME_SYNC_NONE = 7936,
                MDC_TIME_SYNC_NTPV3 = 7937,
                MDC_TIME_SYNC_NTPV4 = 7938,
                MDC_TIME_SYNC_SNTPV4 = 7939,
                MDC_TIME_SYNC_SNTPV4330 = 7940,
                MDC_TIME_SYNC_BTV1 = 7941,
                MDC_TIME_SYNC_RADIO = 7942,
                MDC_TIME_SYNC_HL7_NCK = 7943,
                MDC_TIME_SYNC_CDMA = 7944,
                MDC_TIME_SYNC_GSM = 7945,
                MDC_TIME_SYNC_EBWW = 7946,
                MDC_TIME_SYNC_USB_SOF = 7947,
                MDC_TIME_SYNC_OTHER = 7948,
                MDC_TIME_SYNC_OTHER_MOBILE = 7949,
                MDC_TIME_SYNC_GPS = 7950,
                MDC_REG_CERT_DATA_CONTINUA_VERSION = 8064,
                MDC_REG_CERT_DATA_CONTINUA_CERT_DEV_LIST = 8065,
                MDC_REG_CERT_DATA_CONTINUA_REG_STATUS = 8066,
                MDC_REG_CERT_DATA_CONTINUA_AHD_CERT_LIST = 8067,
                MDC_DEV_SPEC_PROFILE_STETH = 0xFFF6;

        public static final long
                MDC_DEV_SPEC_PROFILE_HYDRA32 = 0x80000 + 4096,
                MDC_DEV_SPEC_PROFILE_PULS_OXIM32 = 0x80000 + 4100,
                MDC_DEV_SPEC_PROFILE_MIN_ECG32 = 0x80000 + 4102,
                MDC_DEV_SPEC_PROFILE_BP32 = 0x80000 + 4103,
                MDC_DEV_SPEC_PROFILE_TEMP32 = 0x80000 + 4104,
                MDC_DEV_SPEC_PROFILE_RESP_RATE32 = 0x80000 + 4109,
                MDC_DEV_SPEC_PROFILE_SCALE32 = 0x80000 + 4111,
                MDC_DEV_SPEC_PROFILE_GLUCOSE32 = 0x80000 + 4113,
                MDC_DEV_SPEC_PROFILE_COAG32 = 0x80000 + 4114,
                MDC_DEV_SPEC_PROFILE_INSULIN_PUMP32 = 0x80000 + 4115,
                MDC_DEV_SPEC_PROFILE_BCA32 = 0x80000 + 4116,
                MDC_DEV_SPEC_PROFILE_PEAK_FLOW32 = 0x80000 + 4117,//public static final INFRA, 4118, "DEV_SPEC_PROFILE_COAG");        // This is an error but in the spec
                MDC_DEV_SPEC_PROFILE_URINE32 = 0x80000 + 4118,
                MDC_DEV_SPEC_PROFILE_SABTE32 = 0x80000 + 4120,
                MDC_DEV_SPEC_PROFILE_PSM32 = 0x80000 + 4124,
                MDC_DEV_SUB_SPEC_PROFILE_CPAP32 = 0x80000 + 4244,
                MDC_DEV_SUB_SPEC_PROFILE_CPAP_AUTO32 = 0x80000 + 4245,
                MDC_DEV_SUB_SPEC_PROFILE_BPAP32 = 0x80000 + 4246,
                MDC_DEV_SUB_SPEC_PROFILE_BPAP_AUTO32 = 0x80000 + 4247,  // For future use
                MDC_DEV_SUB_SPEC_PROFILE_ACSV32 = 0x80000 + 4248,    // For future use
                MDC_DEV_SPEC_PROFILE_CGM32 = 0x80000 + 4121,
                MDC_DEV_SPEC_PROFILE_HF_CARDIO32 = 0x80000 + 4137,
                MDC_DEV_SPEC_PROFILE_HF_STRENGTH32 = 0x80000 + 4138,
                MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB32 = 0x80000 + 4167,
                MDC_DEV_SPEC_PROFILE_AI_MED_MINDER32 = 0x80000 + 4168,
                MDC_DEV_SPEC_PROFILE_GENERIC32 = 0x80000 + 4169,
                MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER32 = 0x80000 + 4200,
                MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR32 = 0x80000 + 4213,
                MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR32 = 0x80000 + 4214,
                MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR32 = 0x80000 + 4215,
                MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR32 = 0x80000 + 4216,
                MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR32 = 0x80000 + 4217,
                MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR32 = 0x80000 + 4218,
                MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR32 = 0x80000 + 4219,
                MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR32 = 0x80000 + 4220,
                MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR32 = 0x80000 + 4221,
                MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR32 = 0x80000 + 4222,
                MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR32 = 0x80000 + 4223,
                MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR32 = 0x80000 + 4224,
                MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR32 = 0x80000 + 4225,
                MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR32 = 0x80000 + 4226,
                MDC_DEV_SUB_SPEC_PROFILE_ECG32 = 0x80000 + 4236,
                MDC_DEV_SUB_SPEC_PROFILE_HR32 = 0x80000 + 4237,
                MDC_ID_MODEL_NUMBER32 = 0x80000 + 7681,
                MDC_ID_MODEL_MANUFACTURER32 = 0x80000 + 7682,
                MDC_ID_PROD_SPEC_UNSPECIFIED32 = 0x80000 + 7683,
                MDC_ID_PROD_SPEC_SERIAL32 = 0x80000 + 7684,
                MDC_ID_PROD_SPEC_PART32 = 0x80000 + 7685,
                MDC_ID_PROD_SPEC_HW32 = 0x80000 + 7686,
                MDC_ID_PROD_SPEC_SW32 = 0x80000 + 7687,
                MDC_ID_PROD_SPEC_FW32 = 0x80000 + 7688,
                MDC_ID_PROD_SPEC_PROTOCOL_REV32 = 0x80000 + 7689,
                MDC_ID_PROD_SPEC_GMDN32 = 0x80000 + 7690,
                MDC_MODALITY_AVERAGING_TIME32 = 0x80000 + 7691,
                MDC_SA_SPECN_FLAGS32 = 0x80000 + 7692,
                MDC_MOC_VMS_MDS_AHD32 = 0x80000 + 7693,
                MDC_TIME_SYNC_NONE32 = 0x80000 + 7936,
                MDC_TIME_SYNC_NTPV332 = 0x80000 + 7937,
                MDC_TIME_SYNC_NTPV432 = 0x80000 + 7938,
                MDC_TIME_SYNC_SNTPV432 = 0x80000 + 7939,
                MDC_TIME_SYNC_SNTPV433032 = 0x80000 + 7940,
                MDC_TIME_SYNC_BTV132 = 0x80000 + 7941,
                MDC_TIME_SYNC_RADIO32 = 0x80000 + 7942,
                MDC_TIME_SYNC_HL7_NCK32 = 0x80000 + 7943,
                MDC_TIME_SYNC_CDMA32 = 0x80000 + 7944,
                MDC_TIME_SYNC_GSM32 = 0x80000 + 7945,
                MDC_TIME_SYNC_EBWW32 = 0x80000 + 7946,
                MDC_TIME_SYNC_USB_SOF32 = 0x80000 + 7947,
                MDC_TIME_SYNC_OTHER32 = 0x80000 + 7948,
                MDC_TIME_SYNC_OTHER_MOBILE32 = 0x80000 + 7949,
                MDC_TIME_SYNC_GPS32 = 0x80000 + 7950,
                MDC_REG_CERT_DATA_CONTINUA_VERSION32 = 0x80000 + 8064,
                MDC_REG_CERT_DATA_CONTINUA_CERT_DEV_LIST32 = 0x80000 + 8065,
                MDC_REG_CERT_DATA_CONTINUA_REG_STATUS32 = 0x80000 + 8066,
                MDC_REG_CERT_DATA_CONTINUA_AHD_CERT_LIST32 = 0x80000 + 8067;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class PHD_DM {

        public static final int
                PARTITION = MDC_PART_PHD_DM,
                MDC_PHD_DM_DEV_STAT = 20000,    // From SABTE
                MDC_ECG_DEV_STAT = 21976,       // Standard has name MDC_ECG_DEV_STATUS and SCADA in one version
                MDC_ECG_EVT_CTXT_GEN = 21977,
                MDC_ECG_EVT_CTXT_USER = 21978,
                MDC_ECG_EVT_CTXT_PERIODIC = 21979,
                MDC_ECG_EVT_CTXT_DETECTED = 21980,
                MDC_ECG_EVT_CTXT_EXTERNAL = 21981,
                MDC_ECG_HEART_RATE_INSTANT = 21982,
                MDC_BLOOD_PRESSURE_MEASUREMENT_STATUS = 22000,
                MDC_SABTE_TIME_PD_FLOW_GEN_TOTAL = 22100,
                MDC_SABTE_TIME_PD_USAGE_TOTAL = 22104,
                MDC_SABTE_TIME_PD_USAGE_W_HUM = 22108,
                MDC_SABTE_TIME_PD_USAGE_WO_HUM = 22112,
                MDC_SABTE_TIME_PD_SNORING_TOTAL = 22116,
                MDC_SABTE_TIME_PD_CSR_TOTAL = 22120,
                MDC_SABTE_TIME_PD_RAMP_SET = 22136,
                MDC_SABTE_FLOW_TOTAL = 22140,
                MDC_SABTE_FLOW_WO_PURGE = 22144,
                MDC_SABTE_FLOW_RESP = 22148,
                MDC_SABTE_AHI = 22180,
                MDC_SABTE_AHI_TOTAL = 22184,
                MDC_SABTE_AHI_UNCLASS = 22188,
                MDC_SABTE_AHI_OBSTRUC = 22192,
                MDC_SABTE_AHI_CENT = 22196,
                MDC_SABTE_LVL_HUMID_STAGE_SET = 22220,
                MDC_SABTE_LVL_HUMID_TEMP_SET = 22224,
                MDC_SABTE_LVL_HUMID_HUM_SET = 22228,
                MDC_SABTE_LVL_TRIG_SENS_SET = 22232,
                MDC_SABTE_LVL_INSP_PRESS_RISE_SET = 22234,
                MDC_SABTE_LVL_ADAPT_SET = 22240,
                MDC_SABTE_MODE_ADAPT_FREEZE_SET = 22260,
                MDC_SABTE_MODE_ADAPT_FREEZE_OFF = 22261,
                MDC_SABTE_MODE_ADAPT_FREEZE_ON = 22262,
                MDC_SABTE_MODE_AUTOSTARTSTOP_SET = 22264,
                MDC_SABTE_MODE_DEV_SET = 22268,
                MDC_SABTE_MODE_DEV_UNDETERMINED = 22269,
                MDC_SABTE_MODE_DEV_STANDBY = 22270,
                MDC_SABTE_MODE_DEV_THERAPY = 22271,
                MDC_SABTE_MODE_DEV_MASK_FITTING = 22272,
                MDC_SABTE_MODE_DEV_DRYING = 22273,
                MDC_SABTE_MODE_DEV_EXPORTING = 22274,
                MDC_SABTE_MODE_THERAPY_SET = 22280,
                MDC_SABTE_MODE_THERAPY_UNDETERMINED = 22281,
                MDC_SABTE_MODE_THERAPY_CPAP = 22282,
                MDC_SABTE_MODE_THERAPY_CPAP_AUTO = 22283,
                MDC_SABTE_MODE_THERAPY_BPAP_S = 22284,
                MDC_SABTE_MODE_THERAPY_BPAP_T = 22285,
                MDC_SABTE_MODE_THERAPY_BPAP_ST = 22286,
                MDC_SABTE_MODE_THERAPY_BPAP_S_AUTO = 22287,
                MDC_SABTE_MODE_THERAPY_BPAP_T_AUTO = 22288,
                MDC_SABTE_MODE_THERAPY_BPAP_ST_AUTO = 22289,
                MDC_SABTE_MODE_THERAPY_ACSV = 22290,
                MDC_SABTE_PATT_COMPLIANCE_CLS = 22300,
                MDC_SABTE_PATT_EFFICACY_CLS = 22308,
                MDC_SABTE_PRESS = 22340,
                MDC_SABTE_PRESS_MIN = 22341,
                MDC_SABTE_PRESS_MAX = 22342,
                MDC_SABTE_PRESS_MEAN = 22343,
                MDC_SABTE_PRESS_P50 = 22347,
                MDC_SABTE_PRESS_P90 = 22349,
                MDC_SABTE_PRESS_P95 = 22350,
                MDC_SABTE_PRESS_INSTANT = 22351,
                MDC_SABTE_PRESS_TARGET = 22360,
                MDC_SABTE_PRESS_CPAP_SET = 22364,
                MDC_SABTE_PRESS_CPAP_AUTO_MIN_SET = 22368,
                MDC_SABTE_PRESS_CPAP_AUTO_MAX_SET = 22372,
                MDC_SABTE_PRESS_IPAP_SET = 22376,
                MDC_SABTE_PRESS_EPAP_SET = 22380,
                MDC_SABTE_PRESS_RAMP_START_SET = 22396,
                MDC_SABTE_RESP_RATE_MIN = 22401,
                MDC_SABTE_RESP_RATE_MAX = 22402,
                MDC_SABTE_RESP_RATE_MEAN = 22403,
                MDC_SABTE_RESP_RATE_P50 = 22407,
                MDC_SABTE_RESP_RATE_P90 = 22409,
                MDC_SABTE_RESP_RATE_P95 = 22410,
                MDC_SABTE_RESP_RATE_INSTANT = 22411,
                MDC_SABTE_RESP_RATE_SET = 22436,
                MDC_SABTE_RATIO_IE_MIN = 22441,
                MDC_SABTE_RATIO_IE_MAX = 22442,
                MDC_SABTE_RATIO_IE_MEAN = 22443,
                MDC_SABTE_RATIO_IE_P50 = 22447,
                MDC_SABTE_RATIO_IE_P90 = 22449,
                MDC_SABTE_RATIO_IE_P95 = 22450,
                MDC_SABTE_RATIO_IE_INSTANT = 22451,
                MDC_SABTE_RATIO_IE_SET = 22476,
                MDC_SABTE_VOL_LEAK = 22480,
                MDC_SABTE_VOL_LEAK_MIN = 22481,
                MDC_SABTE_VOL_LEAK_MAX = 22482,
                MDC_SABTE_VOL_LEAK_MEAN = 22483,
                MDC_SABTE_VOL_LEAK_P50 = 22487,
                MDC_SABTE_VOL_LEAK_P90 = 22489,
                MDC_SABTE_VOL_LEAK_P95 = 22490,
                MDC_SABTE_VOL_LEAK_INSTANT = 22491,
                MDC_SABTE_VOL_MINUTE_MIN = 22521,
                MDC_SABTE_VOL_MINUTE_MAX = 22522,
                MDC_SABTE_VOL_MINUTE_MEAN = 22523,
                MDC_SABTE_VOL_MINUTE_P50 = 22527,
                MDC_SABTE_VOL_MINUTE_P90 = 22529,
                MDC_SABTE_VOL_MINUTE_P95 = 22530,
                MDC_SABTE_VOL_MINUTE_INSTANT = 22531,
                MDC_SABTE_VOL_TIDAL_MIN = 22561,
                MDC_SABTE_VOL_TIDAL_MAX = 22562,
                MDC_SABTE_VOL_TIDAL_MEAN = 22563,
                MDC_SABTE_VOL_TIDAL_P50 = 22567,
                MDC_SABTE_VOL_TIDAL_P90 = 22569,
                MDC_SABTE_VOL_TIDAL_P95 = 22570,
                MDC_SABTE_VOL_TIDAL_INSTANT = 22571,
                MDC_GLU_METER_DEV_STATUS = 29144,
                MDC_CTXT_GLU_EXERCISE = 29152,
                MDC_CTXT_GLU_CARB = 29156,
                MDC_CTXT_GLU_CARB_BREAKFAST = 29160,
                MDC_CTXT_GLU_CARB_LUNCH = 29164,
                MDC_CTXT_GLU_CARB_DINNER = 29168,
                MDC_CTXT_GLU_CARB_SNACK = 29172,
                MDC_CTXT_GLU_CARB_DRINK = 29176,
                MDC_CTXT_GLU_CARB_SUPPER = 29180,
                MDC_CTXT_GLU_CARB_BRUNCH = 29184,
                MDC_CTXT_MEDICATION = 29188,
                MDC_CTXT_MEDICATION_RAPIDACTING = 29192,
                MDC_CTXT_MEDICATION_SHORTACTING = 29196,
                MDC_CTXT_MEDICATION_INTERMEDIATEACTING = 29200,
                MDC_CTXT_MEDICATION_LONGACTING = 29204,
                MDC_CTXT_MEDICATION_PREMIX = 29208,
                MDC_CTXT_GLU_HEALTH = 29212,
                MDC_CTXT_GLU_HEALTH_MINOR = 29216,
                MDC_CTXT_GLU_HEALTH_MAJOR = 29220,
                MDC_CTXT_GLU_HEALTH_MENSES = 29224,
                MDC_CTXT_GLU_HEALTH_STRESS = 29228,
                MDC_CTXT_GLU_HEALTH_NONE = 29232,
                MDC_CTXT_GLU_SAMPLELOCATION = 29236,
                MDC_CTXT_GLU_SAMPLELOCATION_UNDETERMINED = 29237,
                MDC_CTXT_GLU_SAMPLELOCATION_OTHER = 29238,
                MDC_CTXT_GLU_SAMPLELOCATION_FINGER = 29240,
                MDC_CTXT_GLU_SAMPLELOCATION_SUBCUTANEOUS = 29241,
                MDC_CTXT_GLU_SAMPLELOCATION_AST = 29244,
                MDC_CTXT_GLU_SAMPLELOCATION_EARLOBE = 29248,
                MDC_CTXT_GLU_SAMPLELOCATION_CTRLSOLUTION = 29252,
                MDC_CTXT_GLU_MEAL = 29256,
                MDC_CTXT_GLU_MEAL_PREPRANDIAL = 29260,
                MDC_CTXT_GLU_MEAL_BEDTIME = 29261,
                MDC_CTXT_GLU_MEAL_POSTPRANDIAL = 29264,
                MDC_CTXT_GLU_MEAL_FASTING = 29268,
                MDC_CTXT_GLU_MEAL_CASUAL = 29272,
                MDC_CTXT_GLU_TESTER = 29276,
                MDC_CTXT_GLU_TESTER_SELF = 29280,
                MDC_CTXT_GLU_TESTER_HCP = 29284,
                MDC_CTXT_GLU_TESTER_LAB = 29288,
                MDC_BATCHCODE_COAG = 29300,
                MDC_INR_METER_DEV_STATUS = 29301,
                MDC_TARGET_LEVEL_COAG = 29304,
                MDC_MED_CURRENT_COAG = 29308,
                MDC_MED_NEW_COAG = 29312,
                MDC_CTXT_INR_TESTER = 29316,
                MDC_CTXT_INR_TESTER_SELF = 29317,
                MDC_CTXT_INR_TESTER_HCP = 29318,
                MDC_CTXT_INR_TESTER_LAB = 29319,
                MDC_CONC_GLU_TREND = 29400,
                MDC_CONC_GLU_PATIENT_THRESHOLDS_LOW_HIGH = 29404,
                MDC_CONC_GLU_PATIENT_THRESHOLD_LOW = 29405,
                MDC_CONC_GLU_PATIENT_THRESHOLD_HIGH = 29406,
                MDC_CONC_GLU_THRESHOLDS_HYPO_HYPER = 29408,
                MDC_CONC_GLU_THRESHOLD_HYPO = 29409,
                MDC_CONC_GLU_THRESHOLD_HYPER = 29410,
                MDC_CONC_GLU_RATE_THRESHOLDS = 29412,
                MDC_CONC_GLU_RATE_THRESHOLD_INCREASE = 29413,
                MDC_CONC_GLU_RATE_THRESHOLD_DECREASE = 29414,
                MDC_CGM_SENSOR_CALIBRATION = 29428,
                MDC_CGM_SENSOR_RUN_TIME = 29432,
                MDC_CGM_SENSOR_SAMPLE_INTERVAL = 29436,
                MDC_CGM_DEV_STAT = 29452,
                MDC_CGM_DEV_TYPE_SENSOR = 29460,
                MDC_CGM_DEV_TYPE_TRANSMITTER = 29461,
                MDC_CGM_DEV_TYPE_RECEIVER = 29462,
                MDC_CGM_DEV_TYPE_OTHER = 29463,

                MDC_INS_BASAL = 29680,    /* Delivered basal insulin */
                MDC_INS_BASAL_RATE_SETTING = 29692,    /* Current basal insulin rate setting*/
                MDC_INS_BASAL_PRGM = 29693,    /* Programed basal insulin rate    */
                MDC_INS_BASAL_TEMP_ABS = 29694,    /* Temporary basal rate, absolute*/
                MDC_INS_BASAL_TEMP_REL = 29695,    /* Temporary basal rate, relative*/
                MDC_INS_BASAL_UNDETERMINED = 29696,    /* Undetermined basal*/
                MDC_INS_BASAL_DEVICE = 29697,    /* Insulin pump device set basal    */
                MDC_INS_BASAL_REMOTE = 29698,    /* Remote control set basal */
                MDC_INS_BASAL_AP_CTRL = 29699,    /* Controller set basal insulin rate     */
                MDC_INS_BASAL_OTHER = 29700,    /* Rate set by an other source    */
                MDC_INS_BASAL_RATE_SCHED = 29712,    /* Basal rate schedule setting    */
                MDC_INS_BOLUS_SETTING = 29724,    /* Bolus amount set    */
                MDC_INS_BOLUS = 29736,    /* Delivered bolus insulin  */
                MDC_INS_BOLUS_FAST = 29737,    /* Fast bolus    */
                MDC_INS_BOLUS_EXT = 29738,    /* Extended bolus    */
                MDC_INS_BOLUS_CORR = 29739,    /* Correction bolus    */
                MDC_INS_BOLUS_MEAL = 29740,    /* Meal bolus    */
                MDC_INS_BOLUS_UNDETERMINED = 29741,    /* Undetermined bolus    */


                MDC_INS_BOLUS_MANUAL = 29742,    /* Manual, user defined bolus    */
                MDC_INS_BOLUS_RECOMMENDED = 29743,    /* Recommended bolus    */
                MDC_INS_BOLUS_MANUAL_CHANGE = 29744,    /* Recommended bolus Changed by user    */
                MDC_INS_BOLUS_COMMANDED = 29745,    /* Commanded bolus    */
                MDC_INS_BOLUS_OTHER = 29746,    /* Other bolus     */
                MDC_INS_BOLUS_PENDING_DELAY = 29747,    /* Bolus pending delay    */
                MDC_INS_I2CHO_SCHED = 29756,    /* I:CHO schedule setting*/
                MDC_INS_ISF_SCHED = 29768,    /* ISF schedule setting    */
                MDC_INS_RESERVOIR = 29780,    /* Insulin reservoir remaining    */
                MDC_INS_CONC = 29792,    /* Insulin concentration    */
                MDC_INS_PUMP_OP_STAT = 29804,    /* Operational status    */
                MDC_INS_PUMP_DEV_STAT = 29836,    /* Insulin pump device status    */

                MDC_BATTERY_STATUS = 29904, /* Battery status */
                MDC_BATTERY_1 = 29912, /* first battery */
                MDC_BATTERY_2 = 29920, /* second battery */
                MDC_BATTERY_3 = 29928, /* third battery  */
                MDC_BATTERY_4 = 29936, /* fourth battery  */
                MDC_BATTERY_5 = 29944, /* fifth battery  */
                MDC_BATTERY_6 = 29952, /* sixth battery  */
                MDC_BATTERY_7 = 29960, /* seventh battery  */
                MDC_BATTERY_8 = 29968, /* eighth battery  */
                MDC_BATTERY_9 = 29976, /* ninth battery  */
                MDC_BATTERY_10 = 29984, /* tenth battery  */
                MDC_BATTERY_11 = 29992, /* eleventh battery  */
                MDC_BATTERY_12 = 30000, /* twelfth battery  */
                MDC_BATTERY_13 = 30008, /* thirteenth battery  */
                MDC_BATTERY_14 = 30016, /* fourteenth battery  */
                MDC_BATTERY_15 = 30024, /* fifteenth battery  */
                MDC_BATTERY_16 = 30032, /* sixteenth battery */

                MDC_PEFM_READING_STATUS = 30720,
                MDC_PRIV_STETH_AUDIO_SIGNAL = 0xF9F9;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class PHD_HF {

        public static final int
                PARTITION = MDC_PART_PHD_HF,
                MDC_HF_ALT_GAIN = 100,
                MDC_HF_ALT_LOSS = 101,
                MDC_HF_ALT = 102,
                MDC_HF_DISTANCE = 103,
                MDC_HF_ASC_TME_DIST = 104,
                MDC_HF_DESC_TIME_DIST = 105,
                MDC_HF_LATITUDE = 106,
                MDC_HF_LONGITUDE = 107,
                MDC_HF_PROGRAM_ID = 108,
                MDC_HF_SLOPES = 109,
                MDC_HF_SPEED = 110,
                MDC_HF_CAD = 111,
                MDC_HF_INCLINE = 112,
                MDC_HF_HR_MAX_USER = 113,
                MDC_HF_HR = 114,
                MDC_HF_POWER = 115,
                MDC_HF_RESIST = 116,
                MDC_HF_STRIDE = 117,
                MDC_HF_ENERGY = 119,
                MDC_HF_CAL_INGEST = 120,
                MDC_HF_CAL_INGEST_CARB = 121,
                MDC_HF_SUST_PA_THRESHOLD = 122,
                MDC_HF_SESSION = 123,
                MDC_HF_SUBSESSION = 124,
                MDC_HF_ACTIVITY_TIME = 125,
                MDC_HF_AGE = 126,
                MDC_HF_ACTIVITY_INTENSITY = 127,
                MDC_HF_SET = 200,
                MDC_HF_REPETITION = 201,
                MDC_HF_REPETITION_COUNT = 202,
                MDC_HF_RESISTANCE = 203,
                MDC_HF_EXERCISE_POSITION = 204,
                MDC_HF_EXERCISE_LATERALITY = 205,
                MDC_HF_EXERCISE_GRIP = 206,
                MDC_HF_EXERCISE_MOVEMENT = 207,
                MDC_HF_ACT_AMB = 1000,
                MDC_HF_ACT_REST = 1001,
                MDC_HF_ACT_MOTOR = 1002,
                MDC_HF_ACT_LYING = 1003,
                MDC_HF_ACT_SLEEP = 1004,
                MDC_HF_ACT_PHYS = 1005,
                MDC_HF_ACT_SUS_PHYS = 1006,
                MDC_HF_ACT_UNKNOWN = 1007,
                MDC_HF_ACT_MULTIPLE = 1008,
                MDC_HF_ACT_MONITOR = 1009,
                MDC_HF_ACT_SKI = 1010,
                MDC_HF_ACT_RUN = 1011,
                MDC_HF_ACT_BIKE = 1012,
                MDC_HF_ACT_STAIR = 1013,
                MDC_HF_ACT_ROW = 1014,
                MDC_HF_ACT_HOME = 1015,
                MDC_HF_ACT_WORK = 1016,
                MDC_HF_ACT_WALK = 1017,
                MDC_HF_LATERALITY_BOTH = 1200,
                MDC_HF_LATERALITY_RIGHT = 1201,
                MDC_HF_LATERALITY_LEFT = 1202,
                MDC_HF_POSITION_INCLINE = 1203,
                MDC_HF_POSITION_DECLINE = 1204,
                MDC_HF_POSITION_SEATED = 1205,
                MDC_HF_POSITION_STANDING = 1206,
                MDC_HF_POSITION_KNEELING = 1207,
                MDC_HF_POSITION_BENTOVER = 1208,
                MDC_HF_POSITION_HANGING = 1209,
                MDC_HF_POSITION_OVERHEAD = 1210,
                MDC_HF_POSITION_LYING = 1211,
                MDC_HF_MOVEMENT_FLEXION = 1300,
                MDC_HF_MOVEMENT_EXTENSION = 1301,
                MDC_HF_MOVEMENT_ROTATION = 1302,
                MDC_HF_MOVEMENT_ABDUCTION = 1303,
                MDC_HF_MOVEMENT_ADDUCTION = 1304,
                MDC_HF_GRIP_PARALLEL = 1400,
                MDC_HF_GRIP_OVERHAND = 1401,
                MDC_HF_GRIP_UNDERHAND = 1402,
                MDC_HF_GRIP_CLOSE = 1403,
                MDC_HF_GRIP_WIDE = 1404,
                MDC_HF_GRIP_GRIPLESS = 1405,
                MDC_HF_MEAN_NULL_INCLUDE = 2000,
                MDC_HF_MEAN_NULL_EXCLUDE = 2001,
                MDC_HF_MAX = 2002,
                MDC_HF_MIN = 2003,
                MDC_HF_RMS = 2004,
                MDC_HF_INST = 2018,
                MDC_HF_3D_ACC_X = 2011,
                MDC_HF_3D_ACC_Y = 2012,
                MDC_HF_3D_ACC_Z = 2013,
                MDC_HF_RESP_RATE = 2020;
    }

    @SuppressWarnings({"unused", "SpellCheckingInspection"})
    public static class PHD_AI {
        public static final int
                PARTITION = MDC_PART_PHD_AI,
                MDC_AI_TYPE_SENSOR_FALL = 1,
                MDC_AI_TYPE_SENSOR_PERS = 2,
                MDC_AI_TYPE_SENSOR_SMOKE = 3,
                MDC_AI_TYPE_SENSOR_CO = 4,
                MDC_AI_TYPE_SENSOR_WATER = 5,
                MDC_AI_TYPE_SENSOR_GAS = 6,
                MDC_AI_TYPE_SENSOR_MOTION = 7,
                MDC_AI_TYPE_SENSOR_PROPEXIT = 8,
                MDC_AI_TYPE_SENSOR_ENURESIS = 9,
                MDC_AI_TYPE_SENSOR_CONTACTCLOSURE = 10,
                MDC_AI_TYPE_SENSOR_USAGE = 11,
                MDC_AI_TYPE_SENSOR_SWITCH = 12,
                MDC_AI_TYPE_SENSOR_DOSAGE = 13,
                MDC_AI_TYPE_SENSOR_TEMP = 14,
                MDC_AI_LOCATION = 1023,
                MDC_AI_LOCATION_START = 1024,
                MDC_AI_LOCATION_UNKNOWN = 1024,
                MDC_AI_LOCATION_UNSPECIFIED = 1088,
                MDC_AI_LOCATION_RESIDENT = 1152,
                MDC_AI_LOCATION_LOCALUNIT = 1216,
                MDC_AI_LOCATION_BEDROOM = 3072,
                MDC_AI_LOCATION_BEDROOMMASTER = 3136,
                MDC_AI_LOCATION_TOILET = 3200,
                MDC_AI_LOCATION_TOILETMAIN = 3264,
                MDC_AI_LOCATION_OUTSIDETOILET = 3328,
                MDC_AI_LOCATION_SHOWERROOM = 3392,
                MDC_AI_LOCATION_KITCHEN = 3456,
                MDC_AI_LOCATION_KITCHENMAIN = 3520,
                MDC_AI_LOCATION_LIVINGAREA = 3584,
                MDC_AI_LOCATION_LIVINGROOM = 3648,
                MDC_AI_LOCATION_DININGROOM = 3712,
                MDC_AI_LOCATION_STUDY = 3776,
                MDC_AI_LOCATION_HALL = 3840,
                MDC_AI_LOCATION_LANDING = 3904,
                MDC_AI_LOCATION_STAIRS = 3968,
                MDC_AI_LOCATION_HALLLANDINGSTAIRS = 4032,
                MDC_AI_LOCATION_GARAGE = 4096,
                MDC_AI_LOCATION_GARDENGARAGE = 4160,
                MDC_AI_LOCATION_GARDENGARAGEAREA = 4224,
                MDC_AI_LOCATION_FRONTGARDEN = 4288,
                MDC_AI_LOCATION_BACKGARDEN = 4352,
                MDC_AI_LOCATION_SHED = 4416,
                MDC_AI_APPLIANCE_KETTLE = 7168,
                MDC_AI_APPLIANCE_TELEVISION = 7232,
                MDC_AI_APPLIANCE_STOVE = 7296,
                MDC_AI_APPLIANCE_MICROWAVE = 7360,
                MDC_AI_APPLIANCE_TOASTER = 7424,
                MDC_AI_APPLIANCE_VACUUM = 7488,
                MDC_AI_APPLIANCE_APPLIANCE = 7552,
                MDC_AI_APPLIANCE_FAUCET = 7616,
                MDC_AI_LOCATION_FRONTDOOR = 9216,
                MDC_AI_LOCATION_BACKDOOR = 9280,
                MDC_AI_LOCATION_FRIDGEDOOR = 9344,
                MDC_AI_LOCATION_MEDCABDOOR = 9408,
                MDC_AI_LOCATION_WARDROBEDOOR = 9472,
                MDC_AI_LOCATION_FRONTCUPBOARDDOOR = 9536,
                MDC_AI_LOCATION_OTHERDOOR = 9600,
                MDC_AI_LOCATION_BED = 11264,
                MDC_AI_LOCATION_CHAIR = 11328,
                MDC_AI_LOCATION_SOFA = 11392,
                MDC_AI_LOCATION_TOILET_SEAT = 11456,
                MDC_AI_LOCATION_STOOL = 11520,
                MDC_AI_MED_DISPENSED_FIXED = 13312,
                MDC_AI_MED_DISPENSED_VARIABLE = 13313,
                MDC_AI_MED_STATUS = 13314,
                MDC_AI_MED_FEEDBACK = 13315,
                MDC_AI_MED_UF_LOCATION = 13316,
                MDC_AI_MED_UF_RESPONSE = 13317,
                MDC_AI_MED_UF_TYPE_YESNO = 13318;
    }

    /*
     * This section loads the two dictionaries with the current data
     */
    static {

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL, "Electric potential");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_I, "ECG Lead I"); //	Lead I
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_II, "ECG Lead II"); //	Lead II
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_III, "ECG Lead III"); //	Lead III
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVR, "ECG Augmentment voltage right"); //	Augmented voltage right (aVR)
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVL, "ECG Augmentment voltage left"); //	Augmented voltage left (aVL)
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVF, "ECG Augmentment voltage foot"); //	Augmented voltage foot (aVF)
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V1, "ECG Heart local Lead V1"); //	Lead V1
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V2, "ECG Heart local Lead V2"); //	Lead V2
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V3, "ECG Heart local Lead V3"); //	Lead V3
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V4, "ECG Heart local Lead V4"); //	Lead V4
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V5, "ECG Heart local Lead V5"); //	Lead V5
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V6, "ECG Heart local Lead V6"); //	Lead V6

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_AMPL_ST, "ECG ST-segment amplitude");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_QTc, "Corrected ECG QT-segment");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_QT_GL, "ECG QRS-segment");   // QRS
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TACHY, "Rapid resting heart rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_SV_BRADY, "Slow resting heart rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ATR_FIB, "Atrial fibrillation");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_V_P_C_CNT, "Premature ventricular count");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_ACTUAL, "Body mass");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_LEN_BODY_ACTUAL, "Height");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_BODY_FAT, "Body fat");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_MASS_BODY_LEN_SQ, "Body mass index");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_FAT_FREE, "Fat free body mass");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_SOFT_LEAN, "Lean body mass");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_BODY_WATER, "Body water");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_BODY, "Body temp");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_TYMP, "Ear Drum temp");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_SAT_O2, "Blood 0\u2082");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_FAST, "Fast modality");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_SLOW, "Slow modality");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_SPOT, "Stable mean");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_DEV_STATUS, "Pulse Ox sensor status");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RESP_RATE, "Respiration rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_AWAY_RESP_RATE, "Spirometry repiration rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PULS_RATE, "Pulse rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_SAT_O2_QUAL, "Pulse Amplitude Quality");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PLETH, "Pleth Wave");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_RATE_NON_INV, "Pulse rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_HEART_RATE, "ECG pulse rate");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_RR_GL, "ECG RR 'interbeat' interval");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_SINUS_RHY, "Sinus Rhythm (normal heartrate)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV, "Blood Pressure");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_SYS, "Systolic Blood Pressure");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_DIA, "Diastolic Blood Pressure");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_MEAN, "Mean Blood Pressure");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_DEV_STATUS, "Device Status");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RESP_RATE, "Breathing rate");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_GEN, "Generic glucose");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD, "Glucose whole blood");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CAPILLARY_PLASMA, "Glucose capillary plasma");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_VENOUS_WHOLEBLOOD, "Glucose venous wholeblood");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_VENOUS_PLASMA, "Glucose venous plasma");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD, "Glucose arterial whole blood");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ARTERIAL_PLASMA, "Glucose arterial plasma");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CONTROL, "Glucose control solution");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ISF, "Glucose interstitial fluid (ISF)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_HBA1C, "Glucose glycated hemoglobin (HbA1c)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_URINE, "Glucose concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_PH_URINE, "PH grade of urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_SPEC_GRAV_URINE, "Specific gravity of urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_BILIRUBIN_URINE, "Bilirubin concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_KETONE_URINE, "Ketone concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_LEUK_ESTE_URINE, "Leukocyte esterase concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_NITRITE_URINE, "Nitrite concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_OCCULT_BLOOD_URINE, "Occult blood concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_PROTEIN_URINE, "Protein concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_UROBILINOGEN_URINE, "Urobilinogen concentration in urine");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_INR_COAG, "INR Coagulation value");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TIME_PD_COAG, "Prothrombin time");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_QUICK_VALUE_COAG, "INR quick PT/PT(normal)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ISI_COAG, "International Sensitivity Index");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_COAG_CONTROL, "INR Coagulation control");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_UNDETERMINED_WHOLEBLOOD, "Blood Sugar unknown src");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_UNDETERMINED_PLASMA, "Blood Plasma Sugar unknown src");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CONTROL_LEVEL_LOW, "Glucose control solution low value");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CONTROL_LEVEL_MEDIUM, "Glucose control solution medium value");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CONTROL_LEVEL_HIGH, "Glucose control solution high value");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_RECT, "Rectal temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_ORAL, "Oral temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_EAR, "Ear temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_FINGER, "Finger temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_TOE, "Toe temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_AXILLA, "Axillia temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_GIT, "Gastro intestinal GIT temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_BASAL_METABOLISM, "Basal Metabolism");    // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_BODY_MUSCLE, "Body muscle");         // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_MUSCLE, "Mass of body muscle"); // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_BODY_ELECTRICAL_IMPEDANCE, "Body impedance");      // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_ECG_STAT_RHY, "Heart Rhythm status");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_CAPAC_VITAL, "Volume difference between max inspiration and max expiration");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_PEAK, "Peak exhaled flow");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_PEAK_PB, "Pers best Peak exhaled flow");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_1S, "Forced Exhaled vol in 1 sec (FEV1)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_6S, "Forced Exhaled vol in 6 sec (FEV6)");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY, "Accumulated volume");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY, "Instantaneous flow rate");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_CAPACITY, "Forced vital capacity (FVC)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_SLOW_CAPACITY, "Slow vital capacity (SVC)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_FEV1_FEV6, "Forced expiratory volume ratio FEV1/FEV6");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_0_5S, "Forced expiratory volume after .5 sec. (FEV0.5)");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_0_75S, "FEV0.75  Forced expiratory volume after .75 sec.");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_1S_FVC, "FEV1/FVC  Forced expiratory volume ratio after 1 sec to total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_0_5S_FVC, "FEV0.5/FVC Forced expiratory volume ratio after 0.5 sec to total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_0_75S_FVC, "FEV0.75/FVC Forced expiratory volume ratio after 0.75 sec to total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_75_FVC, "FEF25-75 Forced expiratory flow mean 25 to 75% of total.");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_FVC, "FEF25 Forced expiratory flow after 25% of total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_50_FVC, "FEF50 Forced expiratory flow after 50% of total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_75_FVC, "FEF75 Forced expiratory flow after 75% of total");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_PEAK, "PIF Peak inspiratory flow");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_FORCED_CAPACITY, "FIVC Forced inspiratory vital capacity");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_FORCED_1S, "FIV1 Forced inspiratory volume after 1 sec");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25, "FIF25 Forced inspiratory flow at 25% of total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_50, "FIF50 Forced inspiratory flow at 50% of total");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_75, "FIF75 Forced inspiratory flow at 75% of total");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_CAPACITY, "IC Inspiratory capacity");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_RESERVE, "Expiratory Reserve Volume");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_RESERVE, "Inspiratory Reserve Volume");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_SLOW_CAPACITY, "Slow expiratory vital capacity from maximum inhalation");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_TIME, "FET Forced expiratory time");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXTRAP, "Extrapolated volume");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_AWAY_BTPS, "Adjusted temperature-pressure-saturated factor");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AIR_AMBIENT, "Ambient air pressure");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_ROOM, "Ambient room temperature");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_REL_HUMIDITY_AMBIENT, "Ambient relative humidity");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_SLOW_CAPACITY, "Slow expiratory vital capacity");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_2S, "Forced expiratory volume after 2 sec");       //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_3S, "Forced expiratory volume after 3 sec");       //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_5S, "Forced expiratory volume after 5 sec");       //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_2S_FVC, "Ratio forced expiratory volume after 2 sec to FVC");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_3S_FVC, "Ratio forced expiratory volume after 3 sec to FVC");    //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_5S_FVC, "Ratio forced expiratory volume after 5 sec to FVC");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_6S_FVC, "Ratio forced expiratory volume after 6 sec to FVC");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_MAX, "Maximal expiratory flow");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_50, "Forced expiratory flow mean 25 to 50% of FVC");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_75_85, "Forced expiratory flow mean 75 to 85% of FVC");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_0_2L_1_2L, "Forced expiratory flow mean 0.2L to 1.2L");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_85, "Forced expiratory flow at 85% of FVC");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_TIDAL_TIME, "Tidal breathing expiration time (Tex)");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_TIDAL_TIME, "Tidal breathing inspiration time (Tin)");      //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_TIN_TEX, "Ratio of Tidal insp to Tidal exp");           //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25_50, "Forced inspiratory flow mean 25 to 50% of FIV");   //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25_75, "Forced inspiratory flow mean 25 to 75% of FIV");   //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_INSP_FORCED_1S_FIVC, "Ratio forced inspiratory volumeafter 1 sec/FIVC");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_12S, "Maximal voluntary ventilation, volume exhaled after 12 s");     //
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_15S, "Maximal voluntary ventilation, volume exhaled after 15 s");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_25_75_TIME, "Mean Expiratory Time between 25% - 75% ");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_PEAK_TIME, "Time to peak expiratory flow");
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_TIDAL_MEAN, "Mean tidal flow");


        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_HR_BP_PROD, "Rate-Blood Pressure product");    // Bodimetrics Not in 20601 Rate-Pressure product
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_HR_VARIABILITY, "Heart rate variability");

        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_STATUS, "Spirometer measurement status");    // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE, "Spirometry type");    // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_FVC, "Forced Expiration test");    // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_VC, "Slow expiration test");    // Not in 20601
        addNewDictionaryEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_FVC_VC, "Both Forced and slow tests");    // Not in 20601

        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_TEMP, "Ambient/Object temperature");
        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_DISPENSED_FIXED, "Fixed dosage");
        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_DISPENSED_VARIABLE, "Variable dosage");
        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_STATUS, "Status");
        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_FEEDBACK, "User Feedback");
        addNewDictionaryEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_UF_LOCATION, "Location");

        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT_GAIN, "Altitude gain");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT_LOSS, "Altitude loss");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT, "Altitude");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_DISTANCE, "Exercise distance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ASC_TME_DIST, "Ascent distance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_DESC_TIME_DIST, "Descent distance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATITUDE, "Latitude");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LONGITUDE, "Longitude");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_PROGRAM_ID, "Program identifier");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SLOPES, "Slopes");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SPEED, "Speed");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAD, "Cadence");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_INCLINE, "Incline");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_HR_MAX_USER, "User max heart rate");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_HR, "Heart rate");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POWER, "Power");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESIST, "Resistance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_STRIDE, "Stride");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ENERGY, "Energy Expended");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAL_INGEST, "Calories ingested");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAL_INGEST_CARB, "Calories carbs ingested");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SUST_PA_THRESHOLD, "Sustained phys activity thresh");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SESSION, "Session");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SUBSESSION, "Subsession");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACTIVITY_TIME, "Activity Time");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_AGE, "Age");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACTIVITY_INTENSITY, "Activity Intensity");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SET, "Exercise Set");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_REPETITION, "Push distance/rep");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_REPETITION_COUNT, "Repetition Count");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESISTANCE, "Resistance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_POSITION, "Position");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_LATERALITY, "Lateral motion");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_GRIP, "Grip");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_MOVEMENT, "Movement");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_AMB, "Ambient (passing time)");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_REST, "Resting");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MOTOR, "Motorized transport");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_LYING, "Lying");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SLEEP, "Sleeping");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_PHYS, "Physical activity");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SUS_PHYS, "Sustained phys activity");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_UNKNOWN, "Unknown");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MULTIPLE, "Multiple activities");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MONITOR, "Gen activity monitoring");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SKI, "Skiing");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_RUN, "Running");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_BIKE, "Biking");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_STAIR, "Climbing stairs");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_ROW, "Rowing");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_HOME, "at home");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_WORK, "at work");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_WALK, "walking");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_BOTH, "Lateral left & right");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_RIGHT, "Lateral right");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_LEFT, "Lateral left");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_INCLINE, "Inclined");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_DECLINE, "Declined");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_SEATED, "Seated");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_STANDING, "Standing");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_KNEELING, "Kneeling");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_BENTOVER, "Bent over");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_HANGING, "Hanging");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_OVERHEAD, "Overhead");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_FLEXION, "Flexion");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_EXTENSION, "Extension");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ROTATION, "Rotation");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ABDUCTION, "Abduction");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ADDUCTION, "Adduction");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_PARALLEL, "Parallel");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_OVERHAND, "Overhand");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_UNDERHAND, "Underhand");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_CLOSE, "Close");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_WIDE, "Wide");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_GRIPLESS, "Gripless");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MEAN_NULL_INCLUDE, "Session Average");  // MDC_HF_MEAN_NULL_INCLUDE
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MEAN_NULL_EXCLUDE, "Activity Average"); // MDC_HF_MEAN_NULL_EXCLUDE
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MAX, "Session Maximum");  // MDC_HF_MAX
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MIN, "Session Minimum");  // MDC_HF_MIN
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_X, "x-acceleration");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_Y, "y-acceleration");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_Z, "z-acceleration");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_INST, "Instantaneous");
        addNewDictionaryEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESP_RATE, "Breathing rate");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_PECTORAL_MAJOR, "Muscle group Thorax Pect Major");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PHD_DM_DEV_STAT, "Device status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_HEART_RATE_INSTANT, "Instantaneous ECG heart rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BLOOD_PRESSURE_MEASUREMENT_STATUS, "BP measurement status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_FLOW_GEN_TOTAL, "Time of usage since manufacture/service");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_TOTAL, "Therapy session Time of usage");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_W_HUM, "Therapy session time of usage with humidity");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_WO_HUM, "Therapy session time of usage without humidity");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_SNORING_TOTAL, "Total time of snoring");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_CSR_TOTAL, "Time of CSR duration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_RAMP_SET, "Pressure Ramp time");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_TOTAL, "Flow");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_WO_PURGE, "Flow Without intentional leak");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_RESP, "Patient Flow");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI, "Apnoae/hypoapnoea events/hr");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_TOTAL, "Total Apnoae/hypoapnoea events/hr");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_UNCLASS, "Unclassified Apnoae/hypoapnoea events/hr");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_OBSTRUC, "Obstructive Apnoae/hypoapnoea events/hr");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_CENT, "Central Apnoae/hypoapnoea events/hr");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_STAGE_SET, "Humidifier setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_TEMP_SET, "Patient air temperature");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_HUM_SET, "Patient air humidity setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_TRIG_SENS_SET, "BPAP trigger setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_INSP_PRESS_RISE_SET, "BPAP pressure rise time/rate setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_ADAPT_SET, "PAP pressure adaption setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_SET, "Disable Patient adaption pressure adjustment");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_OFF, "Patient pressure adaption off");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_ON, "Patient pressure adaption on");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_AUTOSTARTSTOP_SET, "Set autostart/autostop");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_SET, "Device mode setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_UNDETERMINED, "Unknown");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_STANDBY, "In standby");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_THERAPY, "In therapy");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_MASK_FITTING, "In mask fit mode");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_DRYING, "In drying/cool down");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_EXPORTING, "Transmitting data");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_SET, "Therapy mode setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_UNDETERMINED, "Unknown therapy mode"); // 22281
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_CPAP, "Continuous PAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_CPAP_AUTO, "Auto PAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_S, "Spontaneous BiLevel PAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_T, "Timed BiLevel PAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_ST, "Spontaneous-Timed BPAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_S_AUTO, "Spontaneous Auto BPAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_T_AUTO, "Timed Auto BiLevel PAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_ST_AUTO, "Spontaneous-Timed Auto BPAP");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_ACSV, "Anticyc. Servo-Vent. (AcSV");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PATT_COMPLIANCE_CLS, "Compliance annotation");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PATT_EFFICACY_CLS, "Efficacy annotation");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS, "PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MIN, "Min PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MAX, "Max PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MEAN, "Mean PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P50, "50th percentile PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P90, "90th percentile PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P95, "95th percentile PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_INSTANT, "Current PAP pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_TARGET, "Current PAP target pressure");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_SET, "Continuous PAP pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_AUTO_MIN_SET, "Auto PAP min pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_AUTO_MAX_SET, "Auto PAP max pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_IPAP_SET, "Inspiration PAP pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_EPAP_SET, "Expiration PAP pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_RAMP_START_SET, "Ramp start pressure setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MIN, "Min respiration rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MAX, "Max respiration rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MEAN, "Mean respiration rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P50, "50th percentile respiration rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P90, "90th percentile respiration rate ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P95, "95th percentile respiration rate ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_INSTANT, "Current respiration rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_SET, "Respiration rate setting ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MIN, "Min I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MAX, "Max I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MEAN, "Mean I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P50, "50th percentile I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P90, "90th percentile I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P95, "95th percentile I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_INSTANT, "Current I:E ratio");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_SET, "I:E ratio setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK, "Current leakage");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MIN, "Leak minute volume min");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MAX, "Leak minute volume max");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MEAN, "Leak minute volume mean");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P50, "Leak 50th percentile minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P90, "Leak 90th percentile minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P95, "Leak 95th percentile minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_INSTANT, "Leak current minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MIN, "Respiratory minute volume min");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MAX, "Respiratory minute Volume max");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MEAN, "Respiratory minute volume mean");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P50, "Respiratory 50th percentile minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P90, "Respiratory 90th percentile minute volume ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P95, "Respiratory 95th percentile minute volume ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_INSTANT, "Respiratory current minute volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MIN, "Tidal volume min");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MAX, "Tidal volume max");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MEAN, "Tidal volume mean");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P50, "50th percentile tidal volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P90, "90th percentile tidal volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P95, "95th percentile tidal volume");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_INSTANT, "Current tidal volume");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_GLU_METER_DEV_STATUS, "Glucose meter status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_EXERCISE, "Exercise intensity");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB, "Carbohydrate amount");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_BREAKFAST, "Carbs from Breakfast");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_LUNCH, "Carbs from Lunch");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_DINNER, "Carbs from Dinner");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_SNACK, "Carbs from Snack");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_DRINK, "Carbs from Drink");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_SUPPER, "Carbs from Supper");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_BRUNCH, "Carbs from Brunch");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION, "Medication properties");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_RAPIDACTING, "Medication Rapid acting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_SHORTACTING, "Medication Short acting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_INTERMEDIATEACTING, "Medication Intermediate acting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_LONGACTING, "Medication Long acting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_PREMIX, "Medication Pre mixture");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH, "Health State");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MINOR, "Health: Minor issues");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MAJOR, "Health: Major issues");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MENSES, "Health: menstruation");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_STRESS, "Health: under stress");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_NONE, "Health: No issues");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION, "Location of sample");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_UNDETERMINED, "Unknown location");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_OTHER, "Sample location: Other");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_FINGER, "Sample location: Finger");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_AST, "Sample loc: Alternative site");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_EARLOBE, "Sample location: Earlobe");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_CTRLSOLUTION, "Location - Control solution");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL, "Glucose meal time");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_PREPRANDIAL, "Meal: Pre meal");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_BEDTIME, "Meal: Bedtime snack");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_POSTPRANDIAL, "Meal: Post meal");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_FASTING, "Meal: Fasting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_CASUAL, "Meal: Casual snack");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER, "Glucometer test");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_SELF, "Tester: Self");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_HCP, "Tester: Health Care Provider");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_LAB, "Tester: Lab");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATCHCODE_COAG, "INR strip batch code");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INR_METER_DEV_STATUS, "INR device status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_TARGET_LEVEL_COAG, "INR Target level");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_MED_CURRENT_COAG, "INR Current medication level");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_MED_NEW_COAG, "INR New medication level");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_TREND, "Trending glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLDS_LOW_HIGH, "Patient low and high thresholds for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLD_LOW, "Patient low threshold value for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLD_HIGH, "Patient high threshold value for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLDS_HYPO_HYPER, "Hypo and hyper thresholds for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLD_HYPO, "Hypo threshold value for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLD_HYPER, "Hyper threshold value for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLDS, "Rate of change thresholds for glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLD_INCREASE, "Increase threshold value for rate of change of glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLD_DECREASE, "Decrease threshold value for rate of change of glucose concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_CALIBRATION, "CGM sensor calibration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_RUN_TIME, "CGM sensor run time");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_SAMPLE_INTERVAL, "CGM sensor sample interval");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_STAT, "CGM device status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_SENSOR, "CGM device type sensor");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_TRANSMITTER, "CGM device type_transmitter");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_RECEIVER, "CGM device type receiver");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_OTHER, "CGM device type other (does not match an available option)");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL, "Delivered basal insulin");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_RATE_SETTING, "Current basal insulin rate setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_PRGM, "Programed basal insulin rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_TEMP_ABS, "Temporary basal rate, absolute");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_TEMP_REL, "Temporary basal rate, relative");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_UNDETERMINED, "Undetermined basal");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_DEVICE, "Insulin pump device set basal");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_REMOTE, "Remote control set basal");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_AP_CTRL, "Controller set basal insulin rate");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_OTHER, "Rate set by an other source   ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_RATE_SCHED, "Basal rate profile setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_SETTING, "Preset Bolus amount");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS, "Delivered bolus insulin");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_FAST, "Fast bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_EXT, "Extended bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_CORR, "Correction bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MEAL, "Meal bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_UNDETERMINED, "Undetermined bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MANUAL, "Manual, user defined bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_RECOMMENDED, "Recommended bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MANUAL_CHANGE, " Recommended bolus Changed by user");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_COMMANDED, "Commanded bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_OTHER, "Other bolus");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_PENDING_DELAY, "Bolus delay for Gastroparesis");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_I2CHO_SCHED, "I:CHO schedule setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_ISF_SCHED, "ISF schedule setting");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_RESERVOIR, "Insulin reservoir remaining");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_CONC, "Insulin concentration");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_PUMP_OP_STAT, "Operational status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_PUMP_DEV_STAT, "Insulin pump device status");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_STATUS, "Battery status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_1, "first battery");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_2, "second battery");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_3, "third battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_4, "fourth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_5, "fifth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_6, "sixth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_7, "seventh battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_8, "eighth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_9, "ninth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_10, "tenth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_11, "eleventh battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_12, "twelfth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_13, "thirteenth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_14, "fourteenth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_15, "fifteenth battery ");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_16, "sixteenth battery");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PEFM_READING_STATUS, "Peak Flow read status");
        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PRIV_STETH_AUDIO_SIGNAL, "Stethoscope signal");

        addNewDictionaryEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_TREND, "Glucose conc trend");


        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_DIMLESS, "dimensionless"); // (dimensionless)
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_PERCENT, "%");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_PH, "pH");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_ANG_DEG, "Ang\u00B0");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_BEAT, "beats");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_M, "m");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_M, "\u339D"); // cm
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_FOOT, " feet");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_INCH, " inches");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_L, "l");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_L_PER_SEC, "cl/s");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_L, "cl");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_L, "ml");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_G, "g");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_KILO_G, "kg");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G, "mg");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_LB, "lbs");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_KG_PER_M_SQ, "\u338F/\u33A1"); // kg/m^2
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G_PER_DL, "mg/dL");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MIN, "min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_SEC, "sec");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_SEC, " ms");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_HR, " hrs");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_DAY, " days");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_YR, " years");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_BEAT_PER_MIN, "beats/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_RESP_PER_MIN, "breaths/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_SEC, "m/s");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_L_PER_MIN_PER_M_SQ, "(l/min)/m^2");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_L_PER_MIN, "l/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_L_PER_SEC, "cl/s");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_HECTO_PASCAL, "hPa");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_KILO_PASCAL, "kPa");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_HECTO_PASCAL_PER_SEC, "hPa/s");
        /* Android mangles the default "Hg" in "mmHg" */
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MMHG, "\u339CHg");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_JOULES, " joules");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_WATT, " watts");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_NANO_WATT, " nano watts");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_VOLT, "V");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_VOLT, "mV");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_OHM, " \u03A9");  // Ohms
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MEGA_OHM, " M\u03A9");  // mega Ohms
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_KELVIN, "\u00B0K");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_FAHR, "\u00B0F");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G_PER_DL_PER_MIN, "(mg/dl)/min"); // mg/dL per minute
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_MOLE_PER_L, "mmol/l");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MICRO_MOLE_PER_L, "umol/l");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_MOLE_PER_L_PER_MIN, "(mmol/l)/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_EVT_PER_HR, "events/hr");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_INTL_UNIT, "IU");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_INTL_UNIT_PER_HR, "IU/hr");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_DEGC, "\u00B0C");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_DECIBEL, "dB");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_MIN, "m/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_INR, " INR units");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_SEC_SQ, "\u33A8"); //m/s^2
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_STEP, " steps");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_FOOT_PER_MIN, " ft/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_INCH_PER_MIN, " in/min");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_STEP_PER_MIN, " steps/min");

        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_CAL, "\u3388"); // cal
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_RPM, "rpm");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_TICK, "ticks");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_NUTRI_CAL, "k\u3388"); // kilo cal
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_PRIVATE_MMHG_BEAT_PER_MIN, " mmHg-bpm");
        addNewDictionaryEntry(MDC_PART_DIM, DIM.MDC_DIM_PRIVATE_MICRO_SIEMENS, " \u03BC/\u03A9");  // micro /ohms (micro siemens)


        /* Omron Pedometer */
        addNewDictionaryEntry(MDC_PART_PHD_HF, 61441, "Aerobic Distance");
        addNewDictionaryEntry(MDC_PART_PHD_HF, 61442, "Shikkari 02");
        addNewDictionaryEntry(MDC_PART_PHD_HF, 61491, "Fat Burned");

        /* Omron BCA */
        addNewDictionaryEntry(MDC_PART_SCADA, 61441, "Resting metabolism rate");
        addNewDictionaryEntry(MDC_PART_SCADA, 61442, "Visceral fat");
        addNewDictionaryEntry(MDC_PART_SCADA, 61443, "Body age");
        addNewDictionaryEntry(MDC_PART_SCADA, 0xF00A, "Skeletal muscle amount");
        addNewDictionaryEntry(MDC_PART_SCADA, 0xF010, "Male (0) Female (1)");
        addNewDictionaryEntry(MDC_PART_SCADA, 0xF011, "Patient age");

        /* Device profiles and other device-related terms */
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HYDRA, "Multi-specialization device (hydra)");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_GENERIC, "Generic 20601 device");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PULS_OXIM, "Pulse Oximeter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_MIN_ECG, "Electro cardiogram");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_BP, "Blood Pressure meter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_TEMP, "Thermometer");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_RESP_RATE, "Respiration rate meter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_SCALE, "Weigh Scale");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_GLUCOSE, "Glucose Meter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_COAG, "Blood Coagulation meter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_INSULIN_PUMP, "Insulin Pump");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_BCA, "Body Composition Analyzer");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PEAK_FLOW, "Peak Respiratory Flow meter");
        //addNewDictionaryEntry(MDC_PART_INFRA, 4118, "Blood Coagulation meter");        // This is an error but in the spec
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_URINE, "Urine analyzer");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_SABTE, "Sleep Apnea Breathing Therapy Equipment");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PSM, "Power status monitor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CPAP, "CPAP Sleep Apnea unit");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CPAP_AUTO, "Auto CPAP Sleep Apnea unit");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_BPAP, "BPAP Sleep Apnea unit");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_BPAP_AUTO, "Auto BPAP Sleep Apnea unit");   // For future use
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ACSV, "ACSV Sleep Apnea unit");        // For future use
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_CGM, "Continuous Glucose Meter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HF_CARDIO, "Cardio health and fitness unit");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HF_STRENGTH, "Strength health and fitness unit");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB, "Independent Activity hub");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_AI_MED_MINDER, "Medication Monitor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER, "Step counter");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR, "Fall sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR, "Person sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR, "Smoke sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR, "Carbon Monoxide sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR, "Water sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR, "Gas sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR, "Motion sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR, "Property exit sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR, "Enuresis sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR, "Contact closure sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR, "Usage sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR, "Switch sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR, "Dosage sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR, "Temperature sensor");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ECG, "Electrocardiogram waveform device");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_HR, "Electrocardiogram heart rate device");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_MODEL_NUMBER, "Model number");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_MODEL_MANUFACTURER, "Manufacturer name");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_UNSPECIFIED, "Additional Device information");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_SERIAL, "Serial number");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_PART, "Part number");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_HW, "Hardware revision");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_SW, "Software revision");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_FW, "Firmware revision");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_PROTOCOL_REV, "Protocol revision");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_GMDN, "General Medical Device Number");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_MODALITY_AVERAGING_TIME, "Averaging time modality");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_MOC_VMS_MDS_AHD, "Personal Health Gateway");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NONE, "No time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NTPV3, "NTPV3 time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NTPV4, "NTPV4 time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_SNTPV4, "SNTPV4 time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_SNTPV4330, "SNTPV4330 time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_BTV1, "BTV1 time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_RADIO, "Radio time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_HL7_NCK, "HL7 NCK time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_CDMA, "CDMA time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_GSM, "GSM time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_EBWW, "Eyeball and wristwatch time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_USB_SOF, "USB SOF time synchronization");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_OTHER, "Time sync method that is out of scope for IEEE");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_OTHER_MOBILE, "Time sync based upon some other mobile tech");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_GPS, "Time sync method based on GPS information");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_VERSION, "Continua version");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_CERT_DEV_LIST, "Continua certified device list");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_REG_STATUS, "Regulation status");
        addNewDictionaryEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_AHD_CERT_LIST, "Continua certified Health&Fitness interfaces list");

        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMS_MDS_SIMP, "Personal Health Device");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_VAL_BATT_CHARGE, "Battery level");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_ABS, "Uses Absolute time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_REL, "Uses Relative time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_REL_HI_RES, "Uses High Resolution relative time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_BO, "Uses Base offset time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_LABEL_STRING, "Describes measurement type");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_LIMIT_CURR, "Lower and upper threshold limits");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_TIME_SYNC_PROTOCOL, "Time synchronization protocol");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_ACCUR_MSMT, "Measurement accuracy");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_REL, "Relative time of msmt");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_UNIT_LABEL_STRING, "Describes the units");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_AL_OP_TEXT_STRING, "Description of lower, upper threshold limits");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_REL_HI_RES, "Hi-res relative time of msmt");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_PD_MSMT_ACTIVE, "Measurement duration");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SUPPLEMENTAL_TYPES, "Supplemental information");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_THRES_NOTIF_TEXT_STRING, "Notification msg when threshold is met");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_MSMT_CONFIDENCE_95, "95% that msmt is in this range");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_EVENT_CONTEXT, "Context Identifier");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_ABS, "Resolution of absolute time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_BO, "Resolution of base offset time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_REL, "Resolution of relative time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_REL_HI_RES, "Resolution of hi-resolution relative time clock");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCHED_SEG_PERIOD, "Schedule repeat period");
        addNewDictionaryEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCHED_SEG_REF_ABS_TIME, "Schedule reference time");

        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_TRUNK_BREAST, "Body location chest");    // BTLE HR monitor Chest
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_WRIST, "Body location wrist");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FINGER, "Body location finger");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_HAND, "Body location hand");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_HEAD_EAR, "Body location ear");        // ear lobe
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_FOOT, "Body location foot");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG, "Lower leg");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG_L, "Lower left leg");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG_R, "Lower right leg");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH, "Thigh");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH_L, "Left thigh");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH_R, "Right thigh");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM, "Forearm");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM_L, "Left forearm");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM_R, "Right forearm");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER, "Upper arm");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER_L, "Left upper arm");
        addNewDictionaryEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER_R, "Right upper arm");

    }

    static {
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_DIMLESS, "MDC_DIM_DIMLESS");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_PERCENT, "MDC_DIM_PERCENT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_PH, "MDC_DIM_PH");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_ANG_DEG, "MDC_DIM_ANG_DEG");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_BEAT, "MDC_DIM_BEAT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_M, "MDC_DIM_M");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_M, "MDC_DIM_CENTI_M");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_FOOT, "MDC_DIM_FOOT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_INCH, "MDC_DIM_INCH");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_L, "MDC_DIM_L");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_L, "MDC_DIM_CENTI_L");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_L, "MDC_DIM_MILLI_L");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_G, "MDC_DIM_G");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_KILO_G, "MDC_DIM_KILO_G");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G, "MDC_DIM_MILLI_G");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_LB, "MDC_DIM_LB");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_KG_PER_M_SQ, "MDC_DIM_KG_PER_M_SQ");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G_PER_DL, "MDC_DIM_MILLI_G_PER_DL");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_SEC, "MDC_DIM_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_SEC, "MDC_DIM_MILLI_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MICRO_SEC, "MDC_DIM_MICRO_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MIN, "MDC_DIM_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_HR, "MDC_DIM_HR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_DAY, "MDC_DIM_DAY");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_YR, "MDC_DIM_YR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_BEAT_PER_MIN, "MDC_DIM_BEAT_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_RESP_PER_MIN, "MDC_DIM_RESP_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_SEC, "MDC_DIM_M_PER_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_L_PER_MIN_PER_M_SQ, "MDC_DIM_L_PER_MIN_PER_M_SQ");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_CENTI_L_PER_SEC, "MDC_DIM_CENTI_L_PER_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_L_PER_MIN, "MDC_DIM_L_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_HECTO_PASCAL, "MDC_DIM_HECTO_PASCAL");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_KILO_PASCAL, "MDC_DIM_KILO_PASCAL");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_HECTO_PASCAL_PER_SEC, "MDC_DIM_HECTO_PASCAL_PER_SEC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MMHG, "MDC_DIM_MMHG");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_JOULES, "MDC_DIM_JOULES");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_WATT, "MDC_DIM_WATT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_NANO_WATT, "MDC_DIM_NANO_WATT"); // Add 20 to WATT
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_VOLT, "MDC_DIM_VOLT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_VOLT, "MDC_DIM_MILLI_VOLT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_OHM, "MDC_DIM_OHM");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MEGA_OHM, "MDC_DIM_MEGA_OHM");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_FAHR, "MDC_DIM_FAHR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_KELVIN, "MDC_DIM_KELVIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_MOLE_PER_L, "MDC_DIM_MILLI_MOLE_PER_L");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MICRO_MOLE_PER_L, "MDC_DIM_MICRO_MOLE_PER_L");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_G_PER_DL_PER_MIN, "MDC_DIM_MILLI_G_PER_DL_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_MILLI_MOLE_PER_L_PER_MIN, "MDC_DIM_MILLI_MOLE_PER_L_PER_MIN");

        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_EVT_PER_HR, "MDC_DIM_EVT_PER_HR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_INTL_UNIT, "MDC_DIM_INTL_UNIT");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_INTL_UNIT_PER_HR, "MDC_DIM_INTL_UNIT_PER_HR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_DEGC, "MDC_DIM_DEGC");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_DECIBEL, "MDC_DIM_DECIBEL");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_MIN, "MDC_DIM_M_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_INR, "MDC_DIM_INR");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_M_PER_SEC_SQ, "MDC_DIM_M_PER_SEC_SQ");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_STEP, "MDC_DIM_STEP");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_FOOT_PER_MIN, "MDC_DIM_FOOT_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_INCH_PER_MIN, "MDC_DIM_INCH_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_STEP_PER_MIN, "MDC_DIM_STEP_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_CAL, "MDC_DIM_CAL");        // Wrong in 10441)
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_NUTRI_CAL, "MDC_DIM_NUTRI_CAL");   // According to new draft 10101 (wrong in 10441 on value and ref id)
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_RPM, "MDC_DIM_RPM");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_TICK, "MDC_DIM_TICK");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_PRIVATE_MMHG_BEAT_PER_MIN, "MDC_DIM_PRIVATE_MMHG_BEAT_PER_MIN");
        addNewReferenceIdEntry(MDC_PART_DIM, DIM.MDC_DIM_PRIVATE_MICRO_SIEMENS, "MDC_DIM_PRIVATE_MICRO_SIEMENS");

        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_POST_MED, "MDC_EVT_PEF_POST_MED");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_POST_MED_TRUE, "MDC_EVT_PEF_POST_MED_TRUE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_POST_MED_FALSE, "MDC_EVT_PEF_POST_MED_FALSE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_LONG_TIME_TO_PEAK, "MDC_EVT_PEF_LONG_TIME_TO_PEAK");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_LONG_TIME_TO_PEAK_TRUE, "MDC_EVT_PEF_LONG_TIME_TO_PEAK_TRUE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_LONG_TIME_TO_PEAK_FALSE, "MDC_EVT_PEF_LONG_TIME_TO_PEAK_FALSE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_SHORT_EFFORT, "MDC_EVT_PEF_SHORT_EFFORT");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_SHORT_EFFORT_TRUE, "MDC_EVT_PEF_SHORT_EFFORT_TRUE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_SHORT_EFFORT_FALSE, "MDC_EVT_PEF_SHORT_EFFORT_FALSE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_COUGH, "MDC_EVT_PEF_COUGH");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_COUGH_TRUE, "MDC_EVT_PEF_COUGH_TRUE");
        addNewReferenceIdEntry(MDC_PART_EVT, EVT.MDC_EVT_PEF_COUGH_FALSE, "MDC_EVT_PEF_COUGH_FALSE");

        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HYDRA, "MDC_DEV_SPEC_PROFILE_HYDRA");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PULS_OXIM, "MDC_DEV_SPEC_PROFILE_PULS_OXIM");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_MIN_ECG, "MDC_DEV_SPEC_PROFILE_MIN_ECG");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_BP, "MDC_DEV_SPEC_PROFILE_BP");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_TEMP, "MDC_DEV_SPEC_PROFILE_TEMP");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_RESP_RATE, "MDC_DEV_SPEC_PROFILE_RESP_RATE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_SCALE, "MDC_DEV_SPEC_PROFILE_SCALE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_GLUCOSE, "MDC_DEV_SPEC_PROFILE_GLUCOSE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_COAG, "MDC_DEV_SPEC_PROFILE_COAG");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_INSULIN_PUMP, "MDC_DEV_SPEC_PROFILE_INSULIN_PUMP");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_BCA, "MDC_DEV_SPEC_PROFILE_BCA");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PEAK_FLOW, "MDC_DEV_SPEC_PROFILE_PEAK_FLOW");
        //addNewReferenceIdEntry(MDC_PART_INFRA, 4118, "MDC_DEV_SPEC_PROFILE_COAG");        // This is an error but in the spec
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_URINE, "MDC_DEV_SPEC_PROFILE_URINE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_SABTE, "MDC_DEV_SPEC_PROFILE_SABTE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_PSM, "MDC_DEV_SPEC_PROFILE_PSM");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CPAP, "MDC_DEV_SUB_SPEC_PROFILE_CPAP");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CPAP_AUTO, "MDC_DEV_SUB_SPEC_PROFILE_CPAP_AUTO");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_BPAP, "MDC_DEV_SUB_SPEC_PROFILE_BPAP");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_BPAP_AUTO, "MDC_DEV_SUB_SPEC_PROFILE_BPAP_AUTO");   // For future use
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ACSV, "MDC_DEV_SUB_SPEC_PROFILE_ACSV");        // For future use
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_CGM, "MDC_DEV_SPEC_PROFILE_CGM");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HF_CARDIO, "MDC_DEV_SPEC_PROFILE_HF_CARDIO");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_HF_STRENGTH, "MDC_DEV_SPEC_PROFILE_HF_STRENGTH");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB, "MDC_DEV_SPEC_PROFILE_AI_ACTIVITY_HUB");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_AI_MED_MINDER, "MDC_DEV_SPEC_PROFILE_AI_MED_MINDER");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SPEC_PROFILE_GENERIC, "MDC_DEV_SPEC_PROFILE_GENERIC");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER, "MDC_DEV_SUB_SPEC_PROFILE_STEP_COUNTER");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_FALL_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_PERS_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_SMOKE_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_CO_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_WATER_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_GAS_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_MOTION_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_PROPEXIT_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_ENURESIS_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_CONTACTCLOSURE_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_USAGE_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_SWITCH_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_DOSAGE_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR, "MDC_DEV_SUB_SPEC_PROFILE_TEMP_SENSOR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_ECG, "MDC_DEV_SUB_SPEC_PROFILE_ECG");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_DEV_SUB_SPEC_PROFILE_HR, "MDC_DEV_SUB_SPEC_PROFILE_HR");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_MODEL_NUMBER, "MDC_ID_MODEL_NUMBER");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_MODEL_MANUFACTURER, "MDC_ID_MODEL_MANUFACTURER");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_UNSPECIFIED, "MDC_ID_PROD_SPEC_UNSPECIFIED");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_SERIAL, "MDC_ID_PROD_SPEC_SERIAL");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_PART, "MDC_ID_PROD_SPEC_PART");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_HW, "MDC_ID_PROD_SPEC_HW");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_SW, "MDC_ID_PROD_SPEC_SW");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_FW, "MDC_ID_PROD_SPEC_FW");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_PROTOCOL_REV, "MDC_ID_PROD_SPEC_PROTOCOL_REV");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_ID_PROD_SPEC_GMDN, "MDC_ID_PROD_SPEC_GMDN");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_MODALITY_AVERAGING_TIME, "MDC_MODALITY_AVERAGING_TIME");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_SA_SPECN_FLAGS, "MDC_SA_SPECN_FLAGS");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_MOC_VMS_MDS_AHD, "MDC_MOC_VMS_MDS_AHD");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NONE, "MDC_TIME_SYNC_NONE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NTPV3, "MDC_TIME_SYNC_NTPV3");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_NTPV4, "MDC_TIME_SYNC_NTPV4");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_SNTPV4, "MDC_TIME_SYNC_SNTPV4");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_SNTPV4330, "MDC_TIME_SYNC_SNTPV4330");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_BTV1, "MDC_TIME_SYNC_BTV1");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_RADIO, "MDC_TIME_SYNC_RADIO");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_HL7_NCK, "MDC_TIME_SYNC_HL7_NCK");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_CDMA, "MDC_TIME_SYNC_CDMA");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_GSM, "MDC_TIME_SYNC_GSM");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_EBWW, "MDC_TIME_SYNC_EBWW");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_USB_SOF, "MDC_TIME_SYNC_USB_SOF");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_OTHER, "MDC_TIME_SYNC_OTHER");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_OTHER_MOBILE, "MDC_TIME_SYNC_OTHER_MOBILE");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_TIME_SYNC_GPS, "MDC_TIME_SYNC_GPS");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_VERSION, "MDC_REG_CERT_DATA_CONTINUA_VERSION");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_CERT_DEV_LIST, "MDC_REG_CERT_DATA_CONTINUA_CERT_DEV_LIST");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_REG_STATUS, "MDC_REG_CERT_DATA_CONTINUA_REG_STATUS");
        addNewReferenceIdEntry(MDC_PART_INFRA, INFRA.MDC_REG_CERT_DATA_CONTINUA_AHD_CERT_LIST, "MDC_REG_CERT_DATA_CONTINUA_AHD_CERT_LIST");

        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMO_METRIC, "MDC_MOC_VMO_METRIC");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMO_METRIC_ENUM, "MDC_MOC_VMO_METRIC_ENUM");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMO_METRIC_NU, "MDC_MOC_VMO_METRIC_NU");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMO_METRIC_SA_RT, "MDC_MOC_VMO_METRIC_SA_RT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_SCAN, "MDC_MOC_SCAN");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_SCAN_CFG, "MDC_MOC_SCAN_CFG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_SCAN_CFG_EPI, "MDC_MOC_SCAN_CFG_EPI");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_SCAN_CFG_PERI, "MDC_MOC_SCAN_CFG_PERI");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMS_MDS_SIMP, "MDC_MOC_VMS_MDS_SIMP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_VMO_PMSTORE, "MDC_MOC_VMO_PMSTORE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_MOC_PM_SEGMENT, "MDC_MOC_PM_SEGMENT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_AL_OP_STAT, "MDC_ATTR_AL_OP_STAT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_CONFIRM_MODE, "MDC_ATTR_CONFIRM_MODE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_CONFIRM_TIMEOUT, "MDC_ATTR_CONFIRM_TIMEOUT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_HANDLE, "MDC_ATTR_ID_HANDLE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_INSTNO, "MDC_ATTR_ID_INSTNO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_LABEL_STRING, "MDC_ATTR_ID_LABEL_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_MODEL, "MDC_ATTR_ID_MODEL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_PHYSIO, "MDC_ATTR_ID_PHYSIO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_PROD_SPECN, "MDC_ATTR_ID_PROD_SPECN");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_TYPE, "MDC_ATTR_ID_TYPE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_LIMIT_CURR, "MDC_ATTR_LIMIT_CURR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_STORE_CAPAC_CNT, "MDC_ATTR_METRIC_STORE_CAPAC_CNT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_STORE_SAMPLE_ALG, "MDC_ATTR_METRIC_STORE_SAMPLE_ALG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_STORE_USAGE_CNT, "MDC_ATTR_METRIC_STORE_USAGE_CNT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_MSMT_STAT, "MDC_ATTR_MSMT_STAT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_ACCUR_MSMT, "MDC_ATTR_NU_ACCUR_MSMT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_CMPD_VAL_OBS, "MDC_ATTR_NU_CMPD_VAL_OBS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_VAL_OBS, "MDC_ATTR_NU_VAL_OBS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NUM_SEG, "MDC_ATTR_NUM_SEG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_OP_STAT, "MDC_ATTR_OP_STAT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_POWER_STAT, "MDC_ATTR_POWER_STAT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SA_SPECN, "MDC_ATTR_SA_SPECN");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCAN_REP_PD, "MDC_ATTR_SCAN_REP_PD");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SEG_USAGE_CNT, "MDC_ATTR_SEG_USAGE_CNT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SYS_ID, "MDC_ATTR_SYS_ID");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SYS_TYPE, "MDC_ATTR_SYS_TYPE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_ABS, "MDC_ATTR_TIME_ABS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_BATT_REMAIN, "MDC_ATTR_TIME_BATT_REMAIN");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_END_SEG, "MDC_ATTR_TIME_END_SEG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_PD_SAMP, "MDC_ATTR_TIME_PD_SAMP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_REL, "MDC_ATTR_TIME_REL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_ABS, "MDC_ATTR_TIME_STAMP_ABS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_REL, "MDC_ATTR_TIME_STAMP_REL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_START_SEG, "MDC_ATTR_TIME_START_SEG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TX_WIND, "MDC_ATTR_TX_WIND");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_UNIT_CODE, "MDC_ATTR_UNIT_CODE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_UNIT_LABEL_STRING, "MDC_ATTR_UNIT_LABEL_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_VAL_BATT_CHARGE, "MDC_ATTR_VAL_BATT_CHARGE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_VAL_ENUM_OBS, "MDC_ATTR_VAL_ENUM_OBS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_AL_COND, "MDC_ATTR_AL_COND");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_AL_OP_TEXT_STRING, "MDC_ATTR_AL_OP_TEXT_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_REL_HI_RES, "MDC_ATTR_TIME_REL_HI_RES");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_REL_HI_RES, "MDC_ATTR_TIME_STAMP_REL_HI_RES");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_DEV_CONFIG_ID, "MDC_ATTR_DEV_CONFIG_ID");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_MDS_TIME_INFO, "MDC_ATTR_MDS_TIME_INFO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_SPEC_SMALL, "MDC_ATTR_METRIC_SPEC_SMALL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SOURCE_HANDLE_REF, "MDC_ATTR_SOURCE_HANDLE_REF");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SIMP_SA_OBS_VAL, "MDC_ATTR_SIMP_SA_OBS_VAL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID, "MDC_ATTR_ENUM_OBS_VAL_SIMP_OID");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR, "MDC_ATTR_ENUM_OBS_VAL_SIMP_STR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_REG_CERT_DATA_LIST, "MDC_ATTR_REG_CERT_DATA_LIST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_VAL_OBS_BASIC, "MDC_ATTR_NU_VAL_OBS_BASIC");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_STORE_CAPAB, "MDC_ATTR_PM_STORE_CAPAB");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_SEG_MAP, "MDC_ATTR_PM_SEG_MAP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_SEG_PERSON_ID, "MDC_ATTR_PM_SEG_PERSON_ID");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SEG_STATS, "MDC_ATTR_SEG_STATS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SEG_FIXED_DATA, "MDC_ATTR_SEG_FIXED_DATA");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_SEG_ELEM_STAT_ATTR, "MDC_ATTR_PM_SEG_ELEM_STAT_ATTR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP, "MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCAN_REP_PD_MIN, "MDC_ATTR_SCAN_REP_PD_MIN");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ATTRIBUTE_VAL_MAP, "MDC_ATTR_ATTRIBUTE_VAL_MAP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_VAL_OBS_SIMP, "MDC_ATTR_NU_VAL_OBS_SIMP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_STORE_LABEL_STRING, "MDC_ATTR_PM_STORE_LABEL_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_PM_SEG_LABEL_STRING, "MDC_ATTR_PM_SEG_LABEL_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_PD_MSMT_ACTIVE, "MDC_ATTR_TIME_PD_MSMT_ACTIVE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SYS_TYPE_SPEC_LIST, "MDC_ATTR_SYS_TYPE_SPEC_LIST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_ID_PART, "MDC_ATTR_METRIC_ID_PART");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ENUM_OBS_VAL_PART, "MDC_ATTR_ENUM_OBS_VAL_PART");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SUPPLEMENTAL_TYPES, "MDC_ATTR_SUPPLEMENTAL_TYPES");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_ABS_ADJUST, "MDC_ATTR_TIME_ABS_ADJUST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_CLEAR_TIMEOUT, "MDC_ATTR_CLEAR_TIMEOUT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TRANSFER_TIMEOUT, "MDC_ATTR_TRANSFER_TIMEOUT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR, "MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR, "MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_METRIC_STRUCT_SMALL, "MDC_ATTR_METRIC_STRUCT_SMALL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP, "MDC_ATTR_NU_CMPD_VAL_OBS_SIMP");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC, "MDC_ATTR_NU_CMPD_VAL_OBS_BASIC");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_ID_PHYSIO_LIST, "MDC_ATTR_ID_PHYSIO_LIST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SCAN_HANDLE_LIST, "MDC_ATTR_SCAN_HANDLE_LIST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_CONTEXT_KEY, "MDC_ATTR_CONTEXT_KEY");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_SRC_HANDLE_REF_LIST, "MDC_ATTR_SRC_HANDLE_REF_LIST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_REG_CERT_DATA_AUTH_BODY, "MDC_REG_CERT_DATA_AUTH_BODY");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_CAP_STATE, "MDC_TIME_CAP_STATE");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_SYNC_PROTOCOL, "MDC_TIME_SYNC_PROTOCOL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_SYNC_ACCURACY, "MDC_TIME_SYNC_ACCURACY");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_ABS, "MDC_TIME_RES_ABS");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_REL, "MDC_TIME_RES_REL");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_REL_HI_RES, "MDC_TIME_RES_REL_HI_RES");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_BO, "MDC_ATTR_TIME_BO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_STAMP_BO, "MDC_ATTR_TIME_STAMP_BO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_START_SEG_BO, "MDC_ATTR_TIME_START_SEG_BO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TIME_END_SEG_BO, "MDC_ATTR_TIME_END_SEG_BO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_TICK_RES, "MDC_ATTR_TICK_RES");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_THRES_NOTIF_TEXT_STRING, "MDC_ATTR_THRES_NOTIF_TEXT_STRING");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_MSMT_CONFIDENCE_95, "MDC_ATTR_MSMT_CONFIDENCE_95");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ATTR_EVENT_CONTEXT, "MDC_ATTR_EVENT_CONTEXT");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_TIME_RES_BO, "MDC_TIME_RES_BO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ACT_SEG_CLR, "MDC_ACT_SEG_CLR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ACT_SEG_GET_INFO, "MDC_ACT_SEG_GET_INFO");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ACT_SET_TIME, "MDC_ACT_SET_TIME");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ACT_DATA_REQUEST, "MDC_ACT_DATA_REQUEST");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_ACT_SEG_TRIG_XFER, "MDC_ACT_SEG_TRIG_XFER");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_CONFIG, "MDC_NOTI_CONFIG");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_SCAN_REPORT_FIXED, "MDC_NOTI_SCAN_REPORT_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_SCAN_REPORT_VAR, "MDC_NOTI_SCAN_REPORT_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_SCAN_REPORT_MP_FIXED, "MDC_NOTI_SCAN_REPORT_MP_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_SCAN_REPORT_MP_VAR, "MDC_NOTI_SCAN_REPORT_MP_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_SEGMENT_DATA, "MDC_NOTI_SEGMENT_DATA");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_VAR, "MDC_NOTI_UNBUF_SCAN_REPORT_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_FIXED, "MDC_NOTI_UNBUF_SCAN_REPORT_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED, "MDC_NOTI_UNBUF_SCAN_REPORT_GROUPED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR, "MDC_NOTI_UNBUF_SCAN_REPORT_MP_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED, "MDC_NOTI_UNBUF_SCAN_REPORT_MP_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED, "MDC_NOTI_UNBUF_SCAN_REPORT_MP_GROUPED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_VAR, "MDC_NOTI_BUF_SCAN_REPORT_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_FIXED, "MDC_NOTI_BUF_SCAN_REPORT_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_GROUPED, "MDC_NOTI_BUF_SCAN_REPORT_GROUPED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_MP_VAR, "MDC_NOTI_BUF_SCAN_REPORT_MP_VAR");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED, "MDC_NOTI_BUF_SCAN_REPORT_MP_FIXED");
        addNewReferenceIdEntry(MDC_PART_OBJ, OBJ.MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED, "MDC_NOTI_BUF_SCAN_REPORT_MP_GROUPED");

        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_FALL, "MDC_AI_TYPE_SENSOR_FALL");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_PERS, "MDC_AI_TYPE_SENSOR_PERS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_SMOKE, "MDC_AI_TYPE_SENSOR_SMOKE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_CO, "MDC_AI_TYPE_SENSOR_CO");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_WATER, "MDC_AI_TYPE_SENSOR_WATER");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_GAS, "MDC_AI_TYPE_SENSOR_GAS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_MOTION, "MDC_AI_TYPE_SENSOR_MOTION");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_PROPEXIT, "MDC_AI_TYPE_SENSOR_PROPEXIT");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_ENURESIS, "MDC_AI_TYPE_SENSOR_ENURESIS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_CONTACTCLOSURE, "MDC_AI_TYPE_SENSOR_CONTACTCLOSURE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_USAGE, "MDC_AI_TYPE_SENSOR_USAGE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_SWITCH, "MDC_AI_TYPE_SENSOR_SWITCH");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_DOSAGE, "MDC_AI_TYPE_SENSOR_DOSAGE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_TYPE_SENSOR_TEMP, "MDC_AI_TYPE_SENSOR_TEMP");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION, "MDC_AI_LOCATION");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_START, "MDC_AI_LOCATION_START");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_START, "MDC_AI_LOCATION_UNKNOWN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_UNSPECIFIED, "MDC_AI_LOCATION_UNSPECIFIED");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_RESIDENT, "MDC_AI_LOCATION_RESIDENT");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_LOCALUNIT, "MDC_AI_LOCATION_LOCALUNIT");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_BEDROOM, "MDC_AI_LOCATION_BEDROOM");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_BEDROOMMASTER, "MDC_AI_LOCATION_BEDROOMMASTER");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_TOILET, "MDC_AI_LOCATION_TOILET");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_TOILETMAIN, "MDC_AI_LOCATION_TOILETMAIN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_OUTSIDETOILET, "MDC_AI_LOCATION_OUTSIDETOILET");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_SHOWERROOM, "MDC_AI_LOCATION_SHOWERROOM");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_KITCHEN, "MDC_AI_LOCATION_KITCHEN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_KITCHENMAIN, "MDC_AI_LOCATION_KITCHENMAIN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_LIVINGAREA, "MDC_AI_LOCATION_LIVINGAREA");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_LIVINGROOM, "MDC_AI_LOCATION_LIVINGROOM");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_DININGROOM, "MDC_AI_LOCATION_DININGROOM");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_STUDY, "MDC_AI_LOCATION_STUDY");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_HALL, "MDC_AI_LOCATION_HALL");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_LANDING, "MDC_AI_LOCATION_LANDING");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_STAIRS, "MDC_AI_LOCATION_STAIRS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_HALLLANDINGSTAIRS, "MDC_AI_LOCATION_HALLLANDINGSTAIRS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_GARAGE, "MDC_AI_LOCATION_GARAGE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_GARDENGARAGE, "MDC_AI_LOCATION_GARDENGARAGE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_GARDENGARAGEAREA, "MDC_AI_LOCATION_GARDENGARAGEAREA");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_FRONTGARDEN, "MDC_AI_LOCATION_FRONTGARDEN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_BACKGARDEN, "MDC_AI_LOCATION_BACKGARDEN");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_SHED, "MDC_AI_LOCATION_SHED");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_KETTLE, "MDC_AI_APPLIANCE_KETTLE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_TELEVISION, "MDC_AI_APPLIANCE_TELEVISION");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_STOVE, "MDC_AI_APPLIANCE_STOVE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_MICROWAVE, "MDC_AI_APPLIANCE_MICROWAVE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_TOASTER, "MDC_AI_APPLIANCE_TOASTER");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_VACUUM, "MDC_AI_APPLIANCE_VACUUM");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_APPLIANCE, "MDC_AI_APPLIANCE_APPLIANCE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_APPLIANCE_FAUCET, "MDC_AI_APPLIANCE_FAUCET");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_FRONTDOOR, "MDC_AI_LOCATION_FRONTDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_BACKDOOR, "MDC_AI_LOCATION_BACKDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_FRIDGEDOOR, "MDC_AI_LOCATION_FRIDGEDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_MEDCABDOOR, "MDC_AI_LOCATION_MEDCABDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_WARDROBEDOOR, "MDC_AI_LOCATION_WARDROBEDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_FRONTCUPBOARDDOOR, "MDC_AI_LOCATION_FRONTCUPBOARDDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_OTHERDOOR, "MDC_AI_LOCATION_OTHERDOOR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_BED, "MDC_AI_LOCATION_BED");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_CHAIR, "MDC_AI_LOCATION_CHAIR");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_SOFA, "MDC_AI_LOCATION_SOFA");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_TOILET_SEAT, "MDC_AI_LOCATION_TOILET_SEAT");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_LOCATION_STOOL, "MDC_AI_LOCATION_STOOL");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_DISPENSED_FIXED, "MDC_AI_MED_DISPENSED_FIXED");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_DISPENSED_VARIABLE, "MDC_AI_MED_DISPENSED_VARIABLE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_STATUS, "MDC_AI_MED_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_FEEDBACK, "MDC_AI_MED_FEEDBACK");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_UF_LOCATION, "MDC_AI_MED_UF_LOCATION");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_UF_RESPONSE, "MDC_AI_MED_UF_RESPONSE");
        addNewReferenceIdEntry(MDC_PART_PHD_AI, PHD_AI.MDC_AI_MED_UF_TYPE_YESNO, "MDC_AI_MED_UF_TYPE_YESNO");

        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PHD_DM_DEV_STAT, "MDC_PHD_DM_DEV_STAT");        // From SABTE

        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_DEV_STAT, "MDC_ECG_DEV_STAT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_EVT_CTXT_GEN, "MDC_ECG_EVT_CTXT_GEN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_EVT_CTXT_USER, "MDC_ECG_EVT_CTXT_USER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_EVT_CTXT_PERIODIC, "MDC_ECG_EVT_CTXT_PERIODIC");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_EVT_CTXT_DETECTED, "MDC_ECG_EVT_CTXT_DETECTED");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_EVT_CTXT_EXTERNAL, "MDC_ECG_EVT_CTXT_EXTERNAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_ECG_HEART_RATE_INSTANT, "MDC_ECG_HEART_RATE_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BLOOD_PRESSURE_MEASUREMENT_STATUS, "MDC_BLOOD_PRESSURE_MEASUREMENT_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_FLOW_GEN_TOTAL, "MDC_SABTE_TIME_PD_FLOW_GEN_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_TOTAL, "MDC_SABTE_TIME_PD_USAGE_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_W_HUM, "MDC_SABTE_TIME_PD_USAGE_W_HUM");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_USAGE_WO_HUM, "MDC_SABTE_TIME_PD_USAGE_WO_HUM");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_SNORING_TOTAL, "MDC_SABTE_TIME_PD_SNORING_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_CSR_TOTAL, "MDC_SABTE_TIME_PD_CSR_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_TIME_PD_RAMP_SET, "MDC_SABTE_TIME_PD_RAMP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_TOTAL, "MDC_SABTE_FLOW_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_WO_PURGE, "MDC_SABTE_FLOW_WO_PURGE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_FLOW_RESP, "MDC_SABTE_FLOW_RESP");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI, "MDC_SABTE_AHI");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_TOTAL, "MDC_SABTE_AHI_TOTAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_UNCLASS, "MDC_SABTE_AHI_UNCLASS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_OBSTRUC, "MDC_SABTE_AHI_OBSTRUC");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_AHI_CENT, "MDC_SABTE_AHI_CENT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_STAGE_SET, "MDC_SABTE_LVL_HUMID_STAGE_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_TEMP_SET, "MDC_SABTE_LVL_HUMID_TEMP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_HUMID_HUM_SET, "MDC_SABTE_LVL_HUMID_HUM_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_TRIG_SENS_SET, "MDC_SABTE_LVL_TRIG_SENS_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_INSP_PRESS_RISE_SET, "MDC_SABTE_LVL_INSP_PRESS_RISE_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_LVL_ADAPT_SET, "MDC_SABTE_LVL_ADAPT_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_SET, "MDC_SABTE_MODE_ADAPT_FREEZE_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_OFF, "MDC_SABTE_MODE_ADAPT_FREEZE_OFF");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_ADAPT_FREEZE_ON, "MDC_SABTE_MODE_ADAPT_FREEZE_ON");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_AUTOSTARTSTOP_SET, "MDC_SABTE_MODE_AUTOSTARTSTOP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_SET, "MDC_SABTE_MODE_DEV_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_UNDETERMINED, "MDC_SABTE_MODE_DEV_UNDETERMINED");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_STANDBY, "MDC_SABTE_MODE_DEV_STANDBY");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_THERAPY, "MDC_SABTE_MODE_DEV_THERAPY");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_MASK_FITTING, "MDC_SABTE_MODE_DEV_MASK_FITTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_DRYING, "MDC_SABTE_MODE_DEV_DRYING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_DEV_EXPORTING, "MDC_SABTE_MODE_DEV_EXPORTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_SET, "MDC_SABTE_MODE_THERAPY_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_UNDETERMINED, "MDC_SABTE_MODE_THERAPY_UNDETERMINED");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_CPAP, "MDC_SABTE_MODE_THERAPY_CPAP");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_CPAP_AUTO, "MDC_SABTE_MODE_THERAPY_CPAP_AUTO");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_S, "MDC_SABTE_MODE_THERAPY_BPAP_S");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_T, "MDC_SABTE_MODE_THERAPY_BPAP_T");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_ST, "MDC_SABTE_MODE_THERAPY_BPAP_ST");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_S_AUTO, "MDC_SABTE_MODE_THERAPY_BPAP_S_AUTO");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_T_AUTO, "MDC_SABTE_MODE_THERAPY_BPAP_T_AUTO");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_BPAP_ST_AUTO, "MDC_SABTE_MODE_THERAPY_BPAP_ST_AUTO");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_MODE_THERAPY_ACSV, "MDC_SABTE_MODE_THERAPY_ACSV");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PATT_COMPLIANCE_CLS, "MDC_SABTE_PATT_COMPLIANCE_CLS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PATT_EFFICACY_CLS, "MDC_SABTE_PATT_EFFICACY_CLS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS, "MDC_SABTE_PRESS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MIN, "MDC_SABTE_PRESS_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MAX, "MDC_SABTE_PRESS_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_MEAN, "MDC_SABTE_PRESS_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P50, "MDC_SABTE_PRESS_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P90, "MDC_SABTE_PRESS_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_P95, "MDC_SABTE_PRESS_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_INSTANT, "MDC_SABTE_PRESS_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_TARGET, "MDC_SABTE_PRESS_TARGET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_SET, "MDC_SABTE_PRESS_CPAP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_AUTO_MIN_SET, "MDC_SABTE_PRESS_CPAP_AUTO_MIN_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_CPAP_AUTO_MAX_SET, "MDC_SABTE_PRESS_CPAP_AUTO_MAX_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_IPAP_SET, "MDC_SABTE_PRESS_IPAP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_EPAP_SET, "MDC_SABTE_PRESS_EPAP_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_PRESS_RAMP_START_SET, "MDC_SABTE_PRESS_RAMP_START_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MIN, "MDC_SABTE_RESP_RATE_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MAX, "MDC_SABTE_RESP_RATE_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_MEAN, "MDC_SABTE_RESP_RATE_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P50, "MDC_SABTE_RESP_RATE_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P90, "MDC_SABTE_RESP_RATE_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_P95, "MDC_SABTE_RESP_RATE_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_INSTANT, "MDC_SABTE_RESP_RATE_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RESP_RATE_SET, "MDC_SABTE_RESP_RATE_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MIN, "MDC_SABTE_RATIO_IE_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MAX, "MDC_SABTE_RATIO_IE_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_MEAN, "MDC_SABTE_RATIO_IE_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P50, "MDC_SABTE_RATIO_IE_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P90, "MDC_SABTE_RATIO_IE_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_P95, "MDC_SABTE_RATIO_IE_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_INSTANT, "MDC_SABTE_RATIO_IE_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_RATIO_IE_SET, "MDC_SABTE_RATIO_IE_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK, "MDC_SABTE_VOL_LEAK");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MIN, "MDC_SABTE_VOL_LEAK_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MAX, "MDC_SABTE_VOL_LEAK_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_MEAN, "MDC_SABTE_VOL_LEAK_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P50, "MDC_SABTE_VOL_LEAK_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P90, "MDC_SABTE_VOL_LEAK_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_P95, "MDC_SABTE_VOL_LEAK_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_LEAK_INSTANT, "MDC_SABTE_VOL_LEAK_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MIN, "MDC_SABTE_VOL_MINUTE_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MAX, "MDC_SABTE_VOL_MINUTE_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_MEAN, "MDC_SABTE_VOL_MINUTE_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P50, "MDC_SABTE_VOL_MINUTE_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P90, "MDC_SABTE_VOL_MINUTE_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_P95, "MDC_SABTE_VOL_MINUTE_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_MINUTE_INSTANT, "MDC_SABTE_VOL_MINUTE_INSTANT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MIN, "MDC_SABTE_VOL_TIDAL_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MAX, "MDC_SABTE_VOL_TIDAL_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_MEAN, "MDC_SABTE_VOL_TIDAL_MEAN");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P50, "MDC_SABTE_VOL_TIDAL_P50");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P90, "MDC_SABTE_VOL_TIDAL_P90");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_P95, "MDC_SABTE_VOL_TIDAL_P95");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_SABTE_VOL_TIDAL_INSTANT, "MDC_SABTE_VOL_TIDAL_INSTANT");

        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_GLU_METER_DEV_STATUS, "MDC_GLU_METER_DEV_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_EXERCISE, "MDC_CTXT_GLU_EXERCISE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB, "MDC_CTXT_GLU_CARB");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_BREAKFAST, "MDC_CTXT_GLU_CARB_BREAKFAST");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_LUNCH, "MDC_CTXT_GLU_CARB_LUNCH");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_DINNER, "MDC_CTXT_GLU_CARB_DINNER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_SNACK, "MDC_CTXT_GLU_CARB_SNACK");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_DRINK, "MDC_CTXT_GLU_CARB_DRINK");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_SUPPER, "MDC_CTXT_GLU_CARB_SUPPER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_CARB_BRUNCH, "MDC_CTXT_GLU_CARB_BRUNCH");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION, "MDC_CTXT_MEDICATION");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_RAPIDACTING, "MDC_CTXT_MEDICATION_RAPIDACTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_SHORTACTING, "MDC_CTXT_MEDICATION_SHORTACTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_INTERMEDIATEACTING, "MDC_CTXT_MEDICATION_INTERMEDIATEACTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_LONGACTING, "MDC_CTXT_MEDICATION_LONGACTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_MEDICATION_PREMIX, "MDC_CTXT_MEDICATION_PREMIX");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH, "MDC_CTXT_GLU_HEALTH");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MINOR, "MDC_CTXT_GLU_HEALTH_MINOR");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MAJOR, "MDC_CTXT_GLU_HEALTH_MAJOR");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_MENSES, "MDC_CTXT_GLU_HEALTH_MENSES");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_STRESS, "MDC_CTXT_GLU_HEALTH_STRESS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_HEALTH_NONE, "MDC_CTXT_GLU_HEALTH_NONE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION, "MDC_CTXT_GLU_SAMPLELOCATION");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_UNDETERMINED, "MDC_CTXT_GLU_SAMPLELOCATION_UNDETERMINED");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_OTHER, "MDC_CTXT_GLU_SAMPLELOCATION_OTHER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_FINGER, "MDC_CTXT_GLU_SAMPLELOCATION_FINGER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_SUBCUTANEOUS, "MDC_CTXT_GLU_SAMPLELOCATION_SUBCUTANEOUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_AST, "MDC_CTXT_GLU_SAMPLELOCATION_AST");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_EARLOBE, "MDC_CTXT_GLU_SAMPLELOCATION_EARLOBE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_SAMPLELOCATION_CTRLSOLUTION, "MDC_CTXT_GLU_SAMPLELOCATION_CTRLSOLUTION");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL, "MDC_CTXT_GLU_MEAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_PREPRANDIAL, "MDC_CTXT_GLU_MEAL_PREPRANDIAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_BEDTIME, "MDC_CTXT_GLU_MEAL_BEDTIME");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_POSTPRANDIAL, "MDC_CTXT_GLU_MEAL_POSTPRANDIAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_FASTING, "MDC_CTXT_GLU_MEAL_FASTING");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_MEAL_CASUAL, "MDC_CTXT_GLU_MEAL_CASUAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER, "MDC_CTXT_GLU_TESTER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_SELF, "MDC_CTXT_GLU_TESTER_SELF");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_HCP, "MDC_CTXT_GLU_TESTER_HCP");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_GLU_TESTER_LAB, "MDC_CTXT_GLU_TESTER_LAB");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATCHCODE_COAG, "MDC_BATCHCODE_COAG");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INR_METER_DEV_STATUS, "MDC_INR_METER_DEV_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_TARGET_LEVEL_COAG, "MDC_TARGET_LEVEL_COAG");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_MED_CURRENT_COAG, "MDC_MED_CURRENT_COAG");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_MED_NEW_COAG, "MDC_MED_NEW_COAG");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_INR_TESTER, "MDC_CTXT_INR_TESTER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_INR_TESTER_SELF, "MDC_CTXT_INR_TESTER_SELF");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_INR_TESTER_HCP, "MDC_CTXT_INR_TESTER_HCP");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CTXT_INR_TESTER_LAB, "MDC_CTXT_INR_TESTER_LAB");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_TREND, "MDC_CONC_GLU_TREND");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLDS_LOW_HIGH, "MDC_CONC_GLU_PATIENT_THRESHOLDS_LOW_HIGH");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLDS_LOW_HIGH, "MDC_CONC_GLU_PATIENT_THRESHOLD_LOW");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_PATIENT_THRESHOLD_HIGH, "MDC_CONC_GLU_PATIENT_THRESHOLD_HIGH");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLDS_HYPO_HYPER, "MDC_CONC_GLU_THRESHOLDS_HYPO_HYPER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLD_HYPO, "MDC_CONC_GLU_THRESHOLD_HYPO");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_THRESHOLD_HYPER, "MDC_CONC_GLU_THRESHOLD_HYPER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLDS, "MDC_CONC_GLU_RATE_THRESHOLDS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLD_INCREASE, "MDC_CONC_GLU_RATE_THRESHOLD_INCREASE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CONC_GLU_RATE_THRESHOLD_DECREASE, "MDC_CONC_GLU_RATE_THRESHOLD_DECREASE");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_CALIBRATION, "MDC_CGM_SENSOR_CALIBRATION 29428");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_RUN_TIME, "MDC_CGM_SENSOR_RUN_TIME");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_SENSOR_SAMPLE_INTERVAL, "MDC_CGM_SENSOR_SAMPLE_INTERVAL");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_STAT, "MDC_CGM_DEV_STAT");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_SENSOR, "MDC_CGM_DEV_TYPE_SENSOR");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_TRANSMITTER, "MDC_CGM_DEV_TYPE_TRANSMITTER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_RECEIVER, "MDC_CGM_DEV_TYPE_RECEIVER");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_CGM_DEV_TYPE_OTHER, "MDC_CGM_DEV_TYPE_OTHER");

        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL, "MDC_INS_BASAL");    /* Delivered basal insulin */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_RATE_SETTING, "MDC_INS_BASAL_RATE_SETTING");    /* Current basal insulin rate setting*/
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_PRGM, "MDC_INS_BASAL_PRGM");    /* Programed basal insulin rate    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_TEMP_ABS, "MDC_INS_BASAL_TEMP_ABS");    /* Temporary basal rate, absolute*/
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_TEMP_REL, "MDC_INS_BASAL_TEMP_REL");    /* Temporary basal rate, relative*/
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_UNDETERMINED, "MDC_INS_BASAL_UNDETERMINED");    /* Undetermined basal*/
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_DEVICE, "MDC_INS_BASAL_DEVICE");    /* Insulin pump device set basal    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_REMOTE, "MDC_INS_BASAL_REMOTE");    /* Remote control set basal */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_AP_CTRL, "MDC_INS_BASAL_AP_CTRL");    /* Controller set basal insulin rate     */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_OTHER, "MDC_INS_BASAL_OTHER");    /* Rate set by an other source    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BASAL_RATE_SCHED, "MDC_INS_BASAL_RATE_SCHED");    /* Basal rate schedule setting    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_SETTING, "MDC_INS_BOLUS_SETTING");    /* Bolus amount set    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS, "MDC_INS_BOLUS");    /* Delivered bolus insulin  */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_FAST, "MDC_INS_BOLUS_FAST");    /* Fast bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_EXT, "MDC_INS_BOLUS_EXT");    /* Extended bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_CORR, "MDC_INS_BOLUS_CORR");    /* Correction bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MEAL, "MDC_INS_BOLUS_MEAL");    /* Meal bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_UNDETERMINED, "MDC_INS_BOLUS_UNDETERMINED");    /* Undetermined bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MANUAL, "MDC_INS_BOLUS_MANUAL");    /* Manual, user defined bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_RECOMMENDED, "MDC_INS_BOLUS_RECOMMENDED");    /* Recommended bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_MANUAL_CHANGE, "MDC_INS_BOLUS_MANUAL_CHANGE");    /* Recommended bolus Changed by user    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_COMMANDED, "MDC_INS_BOLUS_COMMANDED");    /* Commanded bolus    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_OTHER, "MDC_INS_BOLUS_OTHER");    /* Other bolus     */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_BOLUS_PENDING_DELAY, "MDC_INS_BOLUS_PENDING_DELAY");    /* Bolus pending delay    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_I2CHO_SCHED, "MDC_INS_I2CHO_SCHED");    /* I:CHO schedule setting*/
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_ISF_SCHED, "MDC_INS_ISF_SCHED");    /* ISF schedule setting    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_RESERVOIR, "MDC_INS_RESERVOIR");    /* Insulin reservoir remaining    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_CONC, "MDC_INS_CONC");    /* Insulin concentration    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_PUMP_OP_STAT, "MDC_INS_PUMP_OP_STAT");    /* Operational status    */
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_INS_PUMP_DEV_STAT, "MDC_INS_PUMP_DEV_STAT");    /* Insulin pump device status    */

        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_STATUS, "PHD_DM.MDC_BATTERY_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_1, "PHD_DM.MDC_BATTERY_1");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_2, "PHD_DM.MDC_BATTERY_2");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_3, "PHD_DM.MDC_BATTERY_3");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_4, "PHD_DM.MDC_BATTERY_4");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_5, "PHD_DM.MDC_BATTERY_5");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_6, "PHD_DM.MDC_BATTERY_6");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_7, "PHD_DM.MDC_BATTERY_7");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_8, "PHD_DM.MDC_BATTERY_8");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_9, "PHD_DM.MDC_BATTERY_9");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_10, "PHD_DM.MDC_BATTERY_10");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_11, "PHD_DM.MDC_BATTERY_11");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_12, "PHD_DM.MDC_BATTERY_12");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_13, "PHD_DM.MDC_BATTERY_13");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_14, "PHD_DM.MDC_BATTERY_14");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_15, "PHD_DM.MDC_BATTERY_15");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_BATTERY_16, "PHD_DM.MDC_BATTERY_16");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PEFM_READING_STATUS, "MDC_PEFM_READING_STATUS");
        addNewReferenceIdEntry(MDC_PART_PHD_DM, PHD_DM.MDC_PRIV_STETH_AUDIO_SIGNAL, "MDC_PRIV_STETH_AUDIO_SIGNAL");

        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT_GAIN, "MDC_HF_ALT_GAIN");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT_LOSS, "MDC_HF_ALT_LOSS");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ALT, "MDC_HF_ALT");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_DISTANCE, "MDC_HF_DISTANCE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ASC_TME_DIST, "MDC_HF_ASC_TME_DIST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_DESC_TIME_DIST, "MDC_HF_DESC_TIME_DIST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATITUDE, "MDC_HF_LATITUDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LONGITUDE, "MDC_HF_LONGITUDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_PROGRAM_ID, "MDC_HF_PROGRAM_ID");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SLOPES, "MDC_HF_SLOPES");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SPEED, "MDC_HF_SPEED");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAD, "MDC_HF_CAD");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_INCLINE, "MDC_HF_INCLINE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_HR_MAX_USER, "MDC_HF_HR_MAX_USER");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_HR, "MDC_HF_HR");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POWER, "MDC_HF_POWER");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESIST, "MDC_HF_RESIST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_STRIDE, "MDC_HF_STRIDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ENERGY, "MDC_HF_ENERGY");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAL_INGEST, "MDC_HF_CAL_INGEST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_CAL_INGEST_CARB, "MDC_HF_CAL_INGEST_CARB");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SUST_PA_THRESHOLD, "MDC_HF_SUST_PA_THRESHOLD");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SESSION, "MDC_HF_SESSION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SUBSESSION, "MDC_HF_SUBSESSION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACTIVITY_TIME, "MDC_HF_ACTIVITY_TIME");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_AGE, "MDC_HF_AGE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACTIVITY_INTENSITY, "MDC_HF_ACTIVITY_INTENSITY");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_SET, "MDC_HF_SET");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_REPETITION, "MDC_HF_REPETITION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_REPETITION_COUNT, "MDC_HF_REPETITION_COUNT");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESISTANCE, "MDC_HF_RESISTANCE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_POSITION, "MDC_HF_EXERCISE_POSITION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_LATERALITY, "MDC_HF_EXERCISE_LATERALITY");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_GRIP, "MDC_HF_EXERCISE_GRIP");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_EXERCISE_MOVEMENT, "MDC_HF_EXERCISE_MOVEMENT");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_AMB, "MDC_HF_ACT_AMB");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_REST, "MDC_HF_ACT_REST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MOTOR, "MDC_HF_ACT_MOTOR");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_LYING, "MDC_HF_ACT_LYING");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SLEEP, "MDC_HF_ACT_SLEEP");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_PHYS, "MDC_HF_ACT_PHYS");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SUS_PHYS, "MDC_HF_ACT_SUS_PHYS");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_UNKNOWN, "MDC_HF_ACT_UNKNOWN");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MULTIPLE, "MDC_HF_ACT_MULTIPLE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_MONITOR, "MDC_HF_ACT_MONITOR");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_SKI, "MDC_HF_ACT_SKI");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_RUN, "MDC_HF_ACT_RUN");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_BIKE, "MDC_HF_ACT_BIKE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_STAIR, "MDC_HF_ACT_STAIR");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_ROW, "MDC_HF_ACT_ROW");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_HOME, "MDC_HF_ACT_HOME");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_WORK, "MDC_HF_ACT_WORK");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_ACT_WALK, "MDC_HF_ACT_WALK");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_BOTH, "MDC_HF_LATERALITY_BOTH");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_RIGHT, "MDC_HF_LATERALITY_RIGHT");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_LATERALITY_LEFT, "MDC_HF_LATERALITY_LEFT");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_INCLINE, "MDC_HF_POSITION_INCLINE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_DECLINE, "MDC_HF_POSITION_DECLINE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_SEATED, "MDC_HF_POSITION_SEATED");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_STANDING, "MDC_HF_POSITION_STANDING");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_KNEELING, "MDC_HF_POSITION_KNEELING");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_BENTOVER, "MDC_HF_POSITION_BENTOVER");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_HANGING, "MDC_HF_POSITION_HANGING");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_OVERHEAD, "MDC_HF_POSITION_OVERHEAD");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_POSITION_LYING, "MDC_HF_POSITION_LYING");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_FLEXION, "MDC_HF_MOVEMENT_FLEXION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_EXTENSION, "MDC_HF_MOVEMENT_EXTENSION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ROTATION, "MDC_HF_MOVEMENT_ROTATION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ABDUCTION, "MDC_HF_MOVEMENT_ABDUCTION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MOVEMENT_ADDUCTION, "MDC_HF_MOVEMENT_ADDUCTION");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_PARALLEL, "MDC_HF_GRIP_PARALLEL");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_OVERHAND, "MDC_HF_GRIP_OVERHAND");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_UNDERHAND, "MDC_HF_GRIP_UNDERHAND");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_CLOSE, "MDC_HF_GRIP_CLOSE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_WIDE, "MDC_HF_GRIP_WIDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_GRIP_GRIPLESS, "MDC_HF_GRIP_GRIPLESS");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MEAN_NULL_INCLUDE, "MDC_HF_MEAN_NULL_INCLUDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MEAN_NULL_EXCLUDE, "MDC_HF_MEAN_NULL_EXCLUDE");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MAX, "MDC_HF_MAX");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_MIN, "MDC_HF_MIN");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RMS, "MDC_HF_RMS");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_INST, "MDC_HF_INST");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_X, "MDC_HF_3D_ACC_X");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_Y, "MDC_HF_3D_ACC_Y");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_3D_ACC_Z, "MDC_HF_3D_ACC_Z");
        addNewReferenceIdEntry(MDC_PART_PHD_HF, PHD_HF.MDC_HF_RESP_RATE, "MDC_HF_RESP_RATE");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL, "MDC_ECG_ELEC_POTL");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_I, "MDC_ECG_ELEC_POTL_I"); //	Lead I
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_II, "MDC_ECG_ELEC_POTL_II"); //	Lead II
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_III, "MDC_ECG_ELEC_POTL_III"); //	Lead III
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVR, "MDC_ECG_ELEC_POTL_AVR"); //	Augmented voltage right (aVR)
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVL, "MDC_ECG_ELEC_POTL_AVL"); //	Augmented voltage left (aVL)
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_AVF, "MDC_ECG_ELEC_POTL_AVF"); //	Augmented voltage foot (aVF)
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V1, "MDC_ECG_ELEC_POTL_V1"); //	Lead V1
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V2, "MDC_ECG_ELEC_POTL_V2"); //	Lead V2
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V3, "MDC_ECG_ELEC_POTL_V3"); //	Lead V3
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V4, "MDC_ECG_ELEC_POTL_V4"); //	Lead V4
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V5, "MDC_ECG_ELEC_POTL_V5"); //	Lead V5
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ELEC_POTL_V6, "MDC_ECG_ELEC_POTL_V6"); //	Lead V6

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_AMPL_ST, "MDC_ECG_AMPL_ST");           // ST amplitude

        //      addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ATTR_VAL_BATT_CHARGE, "MDC_ATTR_VAL_BATT_CHARGE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_QTc, "MDC_ECG_TIME_PD_QTc");     // Corrected QT
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_QT_GL, "MDC_ECG_TIME_PD_QT_GL");   // QRS
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TACHY, "MDC_ECG_TACHY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_SV_BRADY, "MDC_ECG_SV_BRADY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_ATR_FIB, "MDC_ECG_ATR_FIB");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_TIME_PD_RR_GL, "MDC_ECG_TIME_PD_RR_GL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_SINUS_RHY, "MDC_ECG_SINUS_RHY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_HEART_RATE, "MDC_ECG_HEART_RATE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_V_P_C_CNT, "MDC_ECG_V_P_C_CNT");       // PVC Premature Ventricular Count in beats
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PULS_RATE, "MDC_PULS_OXIM_PULS_RATE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_RATE_NON_INV, "MDC_PULS_RATE_NON_INV");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV, "MDC_PRESS_BLD_NONINV");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_SYS, "MDC_PRESS_BLD_NONINV_SYS");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_DIA, "MDC_PRESS_BLD_NONINV_DIA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_BLD_NONINV_MEAN, "MDC_PRESS_BLD_NONINV_MEAN");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_SAT_O2_QUAL, "MDC_SAT_O2_QUAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_BODY, "MDC_TEMP_BODY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_TYMP, "MDC_TEMP_TYMP");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PERF_REL, "MDC_PULS_OXIM_PERF_REL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PLETH, "MDC_PULS_OXIM_PLETH");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_SAT_O2, "MDC_PULS_OXIM_SAT_O2");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_FAST, "MDC_MODALITY_FAST");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_SLOW, "MDC_MODALITY_SLOW");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_PULS_CHAR, "MDC_PULS_OXIM_PULS_CHAR"); // Changed from 19512 ... can't be right.
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MODALITY_SPOT, "MDC_MODALITY_SPOT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PULS_OXIM_DEV_STATUS, "MDC_PULS_OXIM_DEV_STATUS");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RESP_RATE, "MDC_RESP_RATE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_AWAY_RESP_RATE, "MDC_AWAY_RESP_RATE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_MAX, "MDC_PRESS_AWAY_MAX");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_MIN, "MDC_PRESS_AWAY_MIN");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_IE, "MDC_RATIO_IE");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_AVG, "MDC_PRESS_AWAY_AVG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_MEAN, "MDC_PRESS_AWAY_MEAN 21396");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_P50, "MDC_PRESS_AWAY_P50");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_P90, "MDC_PRESS_AWAY_P90");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_P95, "MDC_PRESS_AWAY_P95");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AWAY_WAVE, "MDC_PRESS_AWAY_WAVE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_FLOW_AWAY_WAVE, "MDC_PRESS_FLOW_AWAY_WAVE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_GEN, "MDC_CONC_GLU_GEN");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD, "MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CAPILLARY_PLASMA, "MDC_CONC_GLU_CAPILLARY_PLASMA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_VENOUS_WHOLEBLOOD, "MDC_CONC_GLU_VENOUS_WHOLEBLOOD");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_VENOUS_PLASMA, "MDC_CONC_GLU_VENOUS_PLASMA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD, "MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ARTERIAL_PLASMA, "MDC_CONC_GLU_ARTERIAL_PLASMA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_CONTROL, "MDC_CONC_GLU_CONTROL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_ISF, "MDC_CONC_GLU_ISF");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_HBA1C, "MDC_CONC_HBA1C");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_URINE, "MDC_CONC_GLU_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_PH_URINE, "MDC_CONC_PH_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_SPEC_GRAV_URINE, "MDC_SPEC_GRAV_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_BILIRUBIN_URINE, "MDC_CONC_BILIRUBIN_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_KETONE_URINE, "MDC_CONC_KETONE_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_LEUK_ESTE_URINE, "MDC_CONC_LEUK_ESTE_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_NITRITE_URINE, "MDC_CONC_NITRITE_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_OCCULT_BLOOD_URINE, "MDC_CONC_OCCULT_BLOOD_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_PROTEIN_URINE, "MDC_CONC_PROTEIN_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_UROBILINOGEN_URINE, "MDC_CONC_UROBILINOGEN_URINE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_INR_COAG, "MDC_RATIO_INR_COAG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TIME_PD_COAG, "MDC_TIME_PD_COAG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_QUICK_VALUE_COAG, "MDC_QUICK_VALUE_COAG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ISI_COAG, "MDC_ISI_COAG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_COAG_CONTROL, "MDC_COAG_CONTROL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_UNDETERMINED_WHOLEBLOOD, "MDC_CONC_GLU_UNDETERMINED_WHOLEBLOOD");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CONC_GLU_UNDETERMINED_PLASMA, "MDC_CONC_GLU_UNDETERMINED_PLASMA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_ECG_STAT_RHY, "MDC_ECG_STAT_RHY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TRIG, "MDC_TRIG");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TRIG_BEAT, "MDC_TRIG_BEAT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TRIG_BEAT_MAX_INRUSH, "MDC_TRIG_BEAT_MAX_INRUSH");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_RECT, "MDC_TEMP_RECT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_ORAL, "MDC_TEMP_ORAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_EAR, "MDC_TEMP_EAR");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_FINGER, "MDC_TEMP_FINGER");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_TOE, "MDC_TEMP_TOE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_AXILLA, "MDC_TEMP_AXILLA");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_GIT, "MDC_TEMP_GIT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_ACTUAL, "MDC_MASS_BODY_ACTUAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_LEN_BODY_ACTUAL, "MDC_LEN_BODY_ACTUAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_BODY_FAT, "MDC_BODY_FAT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_MASS_BODY_LEN_SQ, "MDC_RATIO_MASS_BODY_LEN_SQ");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_METRIC_NOS, "MDC_METRIC_NOS");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_FAT_FREE, "MDC_MASS_BODY_FAT_FREE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_SOFT_LEAN, "MDC_MASS_BODY_SOFT_LEAN");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_BODY_WATER, "MDC_BODY_WATER");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_BASAL_METABOLISM, "MDC_BASAL_METABOLISM");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_BODY_MUSCLE, "MDC_BODY_MUSCLE");         // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_MASS_BODY_MUSCLE, "MDC_MASS_BODY_MUSCLE");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_BODY_ELECTRICAL_IMPEDANCE, "MDC_BODY_ELECTRICAL_IMPEDANCE");      // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_BASAL, "MDC_INS_DELIV_BASAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_BASAL_TEMP_REL, "MDC_INS_DELIV_BASAL_TEMP_REL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_BASAL_TEMP_ABS, "MDC_INS_DELIV_BASAL_TEMP_ABS");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_BOLUS_FAST, "MDC_INS_DELIV_BOLUS_FAST");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_BOLUS_SLOW, "MDC_INS_DELIV_BOLUS_SLOW");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_DAILY_DOSE, "MDC_INS_DELIV_DAILY_DOSE");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_DAILY_DOSE_TOTAL, "MDC_INS_DELIV_DAILY_DOSE_TOTAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_DAILY_DOSE_BASAL, "MDC_INS_DELIV_DAILY_DOSE_BASAL");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_INS_DELIV_DAILY_DOSE_BOLUS, "MDC_INS_DELIV_DAILY_DOSE_BOLUS");

        // Peak flow and spirometry
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY, "MDC_FLOW_AWAY");   // Gas flow in airway
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP, "MDC_FLOW_AWAY_INSP");  // Inspiratory gas flow in airway
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_TIDAL, "MDC_VOL_AWAY_TIDAL");  // Volume of gas leaving the patient through the patient connection port during an expiratory phase.
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_MINUTE_AWAY, "MDC_VOL_MINUTE_AWAY");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY, "MDC_VOL_AWAY");    // Integral flow of gas in airway, typically as a waveform or spirometry waveform segment

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_PEAK, "MDC_FLOW_AWAY_EXP_FORCED_PEAK");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_PEAK_PB, "MDC_FLOW_AWAY_EXP_FORCED_PEAK_PB");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_1S, "MDC_VOL_AWAY_EXP_FORCED_1S");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_6S, "MDC_VOL_AWAY_EXP_FORCED_6S");      // Ref id corrected (had extra EXP)
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_CAPAC_VITAL, "MDC_CAPAC_VITAL");         // Difference in volume between maximum inspiration and maximum expiration

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_CAPACITY, "MDC_VOL_AWAY_EXP_FORCED_CAPACITY");    // FVC  Forced vital capacity. liters
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_SLOW_CAPACITY, "MDC_VOL_AWAY_SLOW_CAPACITY");          // SVC  Slow vital capacity. liters
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_FEV1_FEV6, "MDC_RATIO_AWAY_EXP_FORCED_FEV1_FEV6"); // FEV1/FEV6    ratio forced expiratory volume ratio FEV1/FEV6. %
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_0_5S, "MDC_VOL_AWAY_EXP_FORCED_0_5S");        // FEV0.5   forced expiratory volume after .5 sec. liters
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_0_75S, "MDC_VOL_AWAY_EXP_FORCED_0_75S");       // FEV0.75  forced expiratory volume after .75 sec. liters
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_1S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_1S_FVC");       // FEV1/FVC  ratio forced expiratory volume ratio after 1 sec/FVC. %
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_0_5S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_0_5S_FVC");     // FEV0.5/FVC ratio forced expiratory volume ratio after 0.5 sec/FVC. %
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_0_75S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_0_75S_FVC");    // FEV0.75/FVC ratio forced expiratory volume ratio after 0.5 sec/FVC. %
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_75_FVC, "MDC_FLOW_AWAY_EXP_FORCED_25_75_FVC");     // FEF25-75  forced expiratory flow mean 25 to 75% of FVC. liters/sec
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_FVC, "MDC_FLOW_AWAY_EXP_FORCED_25_FVC");     // FEF25 l/s after 25% of FVC
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_50_FVC, "MDC_FLOW_AWAY_EXP_FORCED_50_FVC");     // FEF50 l/s
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_75_FVC, "MDC_FLOW_AWAY_EXP_FORCED_75_FVC");     // FEF75 l/s

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_PEAK, "MDC_FLOW_AWAY_INSP_FORCED_PEAK");     // PIF Peak inspiratory flow l/s
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_FORCED_CAPACITY, "MDC_VOL_AWAY_INSP_FORCED_CAPACITY");  // FIVC forced inspiratory vital capacity l/s
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_FORCED_1S, "MDC_VOL_AWAY_INSP_FORCED_1S");      // FIV1 forced inspiratory volume after 1 sec  l
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25, "MDC_FLOW_AWAY_INSP_FORCED_25");     // FIF25
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_50, "MDC_FLOW_AWAY_INSP_FORCED_50");     // FIF50
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_75, "MDC_FLOW_AWAY_INSP_FORCED_75");     // FIF75

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_CAPACITY, "MDC_VOL_AWAY_INSP_CAPACITY");       // IC slow inspiratory capacity
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_RESERVE, "MDC_VOL_AWAY_EXP_RESERVE");    // ERV Expratory Reserve Volume
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_RESERVE, "MDC_VOL_AWAY_INSP_RESERVE");    // Inspiratory Reserve Volume
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_SLOW_CAPACITY, "MDC_VOL_AWAY_INSP_SLOW_CAPACITY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_TIME, "MDC_VOL_AWAY_EXP_FORCED_TIME");    // FET forced expiratory time
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXTRAP, "MDC_VOL_AWAY_EXTRAP");       // Extrapolated volume
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_AWAY_BTPS, "MDC_AWAY_BTPS");                   //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRESS_AIR_AMBIENT, "MDC_PRESS_AIR_AMBIENT");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_TEMP_ROOM, "MDC_TEMP_ROOM");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_SLOW_CAPACITY, "MDC_VOL_AWAY_EXP_SLOW_CAPACITY");     // Slow expiratory vital capacity
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_2S, "MDC_VOL_AWAY_EXP_FORCED_2S");       //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_3S, "MDC_VOL_AWAY_EXP_FORCED_3S");       //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_FORCED_5S, "MDC_VOL_AWAY_EXP_FORCED_5S");       //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_2S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_2S_FVC");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_3S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_3S_FVC");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_5S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_5S_FVC");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_EXP_FORCED_6S_FVC, "MDC_RATIO_AWAY_EXP_FORCED_6S_FVC");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_MAX, "MDC_FLOW_AWAY_EXP_FORCED_MAX");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_25_50, "MDC_FLOW_AWAY_EXP_FORCED_25_50");   //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_75_85, "MDC_FLOW_AWAY_EXP_FORCED_75_85");   //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_0_2L_1_2L, "MDC_FLOW_AWAY_EXP_FORCED_0_2L_1_2L");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_FORCED_85, "MDC_FLOW_AWAY_EXP_FORCED_85");      //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_TIDAL_TIME, "MDC_VOL_AWAY_EXP_TIDAL_TIME");      //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_INSP_TIDAL_TIME, "MDC_VOL_AWAY_INSP_TIDAL_TIME");      //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_TIN_TEX, "MDC_RATIO_AWAY_TIN_TEX");           //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25_50, "MDC_FLOW_AWAY_INSP_FORCED_25_50");   //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_INSP_FORCED_25_75, "MDC_FLOW_AWAY_INSP_FORCED_25_75");   //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_RATIO_AWAY_INSP_FORCED_1S_FIVC, "MDC_RATIO_AWAY_INSP_FORCED_1S_FIVC");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_12S, "MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_12S");     //
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_15S, "MDC_VOL_AWAY_CAPACITY_VOLUNTARY_MAX_15S");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_VOL_AWAY_EXP_25_75_TIME, "MDC_VOL_AWAY_EXP_25_75_TIME");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_PEAK_TIME, "MDC_FLOW_AWAY_EXP_PEAK_TIME");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_FLOW_AWAY_EXP_TIDAL_MEAN, "MDC_FLOW_AWAY_EXP_TIDAL_MEAN");

        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_HR_BP_PROD, "MDC_PRIVATE_HR_BP_PROD");      // Bodimetrics Not in 20601 Rate-Pressure product
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_HR_VARIABILITY, "MDC_PRIVATE_HR_VARIABILITY");
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_STATUS, "MDC_PRIVATE_SPIROMETRY_STATUS");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE, "MDC_PRIVATE_SPIROMETRY_TYPE");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_FVC, "MDC_PRIVATE_SPIROMETRY_TYPE_FVC");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_VC, "MDC_PRIVATE_SPIROMETRY_TYPE_VC");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_SPIROMETRY_TYPE_FVC_VC, "MDC_PRIVATE_SPIROMETRY_TYPE_FVC_VC");    // Not in 20601
        addNewReferenceIdEntry(MDC_PART_SCADA, SCADA.MDC_PRIVATE_EMPATICA_CONDUCTANCE, "MDC_PRIVATE_EMPATICA_CONDUCTANCE");      // Not in 20601

        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_SKELETAL, "MDC_MUSC_SKELETAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD, "MDC_MUSC_HEAD");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_EYE, "MDC_MUSC_HEAD_EYE");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_RECT_SUP, "MDC_MUSC_HEAD_RECT_SUP");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_RECT_INF, "MDC_MUSC_HEAD_RECT_INF");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_RECT_MED, "MDC_MUSC_HEAD_RECT_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_RECT_LAT, "MDC_MUSC_HEAD_RECT_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_OBLIQ_SUP, "MDC_MUSC_HEAD_OBLIQ_SUP");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_OBLIQ_INF, "MDC_MUSC_HEAD_OBLIQ_INF");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_FACIAL, "MDC_MUSC_HEAD_FACIAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_OCCIPITOFRONT_VENTER, "MDC_MUSC_HEAD_OCCIPITOFRONT_VENTER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_ORBIC_OCUL, "MDC_MUSC_HEAD_ORBIC_OCUL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_ORBIC_OCUL_PARS_ORBIT, "MDC_MUSC_HEAD_ORBIC_OCUL_PARS_ORBIT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_AURIC_POST, "MDC_MUSC_HEAD_AURIC_POST");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_ORBIC_ORIS, "MDC_MUSC_HEAD_ORBIC_ORIS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_DEPRESSOR_ANGUL_ORIS, "MDC_MUSC_HEAD_DEPRESSOR_ANGUL_ORIS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_RISOR, "MDC_MUSC_HEAD_RISOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_ZYGOMATIC_MAJOR, "MDC_MUSC_HEAD_ZYGOMATIC_MAJOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_ZYGOMATIC_MINOR, "MDC_MUSC_HEAD_ZYGOMATIC_MINOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_LEVATOR_LAB_SUP, "MDC_MUSC_HEAD_LEVATOR_LAB_SUP");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_LEVATOR_LAB_SUP_AL_NASI, "MDC_MUSC_HEAD_LEVATOR_LAB_SUP_AL_NASI");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_DEPRESSOR_LAB_INF, "MDC_MUSC_HEAD_DEPRESSOR_LAB_INF");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_LEVATOR_ANGUL_ORIS, "MDC_MUSC_HEAD_LEVATOR_ANGUL_ORIS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_BUCCINATOR, "MDC_MUSC_HEAD_BUCCINATOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_MENTAL, "MDC_MUSC_HEAD_MENTAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_MASSETER, "MDC_MUSC_HEAD_MASSETER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_TEMPOR, "MDC_MUSC_HEAD_TEMPOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_PTERYGOID, "MDC_MUSC_HEAD_PTERYGOID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_PTERYGOID_LAT, "MDC_MUSC_HEAD_PTERYGOID_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_PTERYGOID_MED, "MDC_MUSC_HEAD_PTERYGOID_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_LING, "MDC_MUSC_HEAD_LING");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_GENIOGLOSS, "MDC_MUSC_HEAD_GENIOGLOSS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_LARING, "MDC_MUSC_HEAD_LARING");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_CRICOTHYROID, "MDC_MUSC_HEAD_CRICOTHYROID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_HEAD_THYROARYTEROID, "MDC_MUSC_HEAD_THYROARYTEROID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK, "MDC_MUSC_NECK");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_PLATYSMA, "MDC_MUSC_NECK_PLATYSMA");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_CAPT_LONG, "MDC_MUSC_NECK_CAPT_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_STERNOCLEIDOMASTOID, "MDC_MUSC_NECK_STERNOCLEIDOMASTOID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_DIGRASTRIC, "MDC_MUSC_NECK_DIGRASTRIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_DIGRASTRIC_VENTER_ANT, "MDC_MUSC_NECK_DIGRASTRIC_VENTER_ANT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_DIGRASTRIC_VENTER_POST, "MDC_MUSC_NECK_DIGRASTRIC_VENTER_POST");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_NECK_MYLOHYOID, "MDC_MUSC_NECK_MYLOHYOID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_TRUNK, "MDC_MUSC_TRUNK");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK, "MDC_MUSC_BACK");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_UPPER, "MDC_MUSC_BACK_UPPER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_LOWER, "MDC_MUSC_BACK_LOWER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_TRAPEZ, "MDC_MUSC_BACK_TRAPEZ");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_LASTISSIM_DORS, "MDC_MUSC_BACK_LASTISSIM_DORS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_RHOMB_MAJOR, "MDC_MUSC_BACK_RHOMB_MAJOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_RHOMB_MINOR, "MDC_MUSC_BACK_RHOMB_MINOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SCAP_LEVATOR, "MDC_MUSC_BACK_SCAP_LEVATOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SERRAT_POST, "MDC_MUSC_BACK_SERRAT_POST");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPLEN_CAPT, "MDC_MUSC_BACK_SPLEN_CAPT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPLEN_CERVIC, "MDC_MUSC_BACK_SPLEN_CERVIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPLEN, "MDC_MUSC_BACK_SPLEN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPINAL_ERECTOR, "MDC_MUSC_BACK_SPINAL_ERECTOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPINAL, "MDC_MUSC_BACK_SPINAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPINAL_THORAC, "MDC_MUSC_BACK_SPINAL_THORAC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPINAL_CERVIC, "MDC_MUSC_BACK_SPINAL_CERVIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SPINAL_CAPIT, "MDC_MUSC_BACK_SPINAL_CAPIT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SEMISPINAL, "MDC_MUSC_BACK_SEMISPINAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SEMISPINAL_THOR, "MDC_MUSC_BACK_SEMISPINAL_THOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SEMISPINAL_CERV, "MDC_MUSC_BACK_SEMISPINAL_CERV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_SEMISPINAL_CAPIT, "MDC_MUSC_BACK_SEMISPINAL_CAPIT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_MULTIFID, "MDC_MUSC_BACK_MULTIFID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_INTERSPINAL, "MDC_MUSC_BACK_INTERSPINAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_INTERSPINAL_CERVIC, "MDC_MUSC_BACK_INTERSPINAL_CERVIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_INTERSPINAL_THORAC, "MDC_MUSC_BACK_INTERSPINAL_THORAC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_BACK_INTERSPINAL_LUMBOR, "MDC_MUSC_BACK_INTERSPINAL_LUMBOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX, "MDC_MUSC_THORAX");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_PECTORAL_MAJOR, "MDC_MUSC_THORAX_PECTORAL_MAJOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_PECTORAL_MINOR, "MDC_MUSC_THORAX_PECTORAL_MINOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_SUBCLAV, "MDC_MUSC_THORAX_SUBCLAV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_SERRAT_ANT, "MDC_MUSC_THORAX_SERRAT_ANT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_INTERCOSTAL, "MDC_MUSC_THORAX_INTERCOSTAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_THORAX_DIAPHRAGM, "MDC_MUSC_THORAX_DIAPHRAGM");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM, "MDC_MUSC_ABDOM");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_ABDOMIN, "MDC_MUSC_ABDOM_ABDOMIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_OBLIQ_EXT, "MDC_MUSC_ABDOM_OBLIQ_EXT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_OBLIQ_INT, "MDC_MUSC_ABDOM_OBLIQ_INT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_ABDOM_TRANSVERS, "MDC_MUSC_ABDOM_ABDOM_TRANSVERS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_LUMBOR_QUADRAT, "MDC_MUSC_ABDOM_LUMBOR_QUADRAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_PELV, "MDC_MUSC_ABDOM_PELV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_PUBORECT, "MDC_MUSC_ABDOM_PUBORECT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_COCCYG, "MDC_MUSC_ABDOM_COCCYG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_ANI_SPHINCTER, "MDC_MUSC_ABDOM_ANI_SPHINCTER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_ABDOM_ANI_SPHINCTER_EXT, "MDC_MUSC_ABDOM_ANI_SPHINCTER_EXT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT, "MDC_MUSC_UPEXT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_DELTOID, "MDC_MUSC_UPEXT_DELTOID");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_SUPRASPINAT, "MDC_MUSC_UPEXT_SUPRASPINAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_INFRASPINAT, "MDC_MUSC_UPEXT_INFRASPINAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_TERES_MINOR, "MDC_MUSC_UPEXT_TERES_MINOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_TERES_MAJOR, "MDC_MUSC_UPEXT_TERES_MAJOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_SUBSCAP, "MDC_MUSC_UPEXT_SUBSCAP");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACHI_BICEPS, "MDC_MUSC_UPEXT_BRACHI_BICEPS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACHIAL, "MDC_MUSC_UPEXT_BRACHIAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_CORACOBRACH, "MDC_MUSC_UPEXT_CORACOBRACH");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACH_TRICEPS, "MDC_MUSC_UPEXT_BRACH_TRICEPS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LONG, "MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LAT, "MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_MED, "MDC_MUSC_UPEXT_BRACH_TRICEPS_CAP_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_ANCON, "MDC_MUSC_UPEXT_ANCON");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_PRONATOR, "MDC_MUSC_UPEXT_PRONATOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_CARPI_RADIAL, "MDC_MUSC_UPEXT_FLEX_CARPI_RADIAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_PALMAR_LONG, "MDC_MUSC_UPEXT_PALMAR_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_CARPI_ULNAR, "MDC_MUSC_UPEXT_FLEX_CARPI_ULNAR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_DIGIT_SUPERF, "MDC_MUSC_UPEXT_FLEX_DIGIT_SUPERF");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_DIGIT_PROFUND, "MDC_MUSC_UPEXT_FLEX_DIGIT_PROFUND");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_POLLIC_LONG, "MDC_MUSC_UPEXT_FLEX_POLLIC_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_PRONATOR_QUADRAT, "MDC_MUSC_UPEXT_PRONATOR_QUADRAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_BRACHIORADIAL, "MDC_MUSC_UPEXT_BRACHIORADIAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_LONG, "MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_BREV, "MDC_MUSC_UPEXT_EXTENS_CARP_RADIAL_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_DIGIT, "MDC_MUSC_UPEXT_EXTENS_DIGIT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_DIGIT_MIN, "MDC_MUSC_UPEXT_EXTENS_DIGIT_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_CARP_ULNAR, "MDC_MUSC_UPEXT_EXTENS_CARP_ULNAR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_SUPINATOR, "MDC_MUSC_UPEXT_SUPINATOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_ABDUC_POLLIC_LONG, "MDC_MUSC_UPEXT_ABDUC_POLLIC_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_POLLIC_BREV, "MDC_MUSC_UPEXT_EXTENS_POLLIC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_POLLIC_LONG, "MDC_MUSC_UPEXT_EXTENS_POLLIC_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_EXTENS_INDIC, "MDC_MUSC_UPEXT_EXTENS_INDIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_PALMAR_BREV, "MDC_MUSC_UPEXT_PALMAR_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_ABDUC_POLLIC_BREV, "MDC_MUSC_UPEXT_ABDUC_POLLIC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_POLLIC_BREV, "MDC_MUSC_UPEXT_FLEX_POLLIC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_OPPON_POLLIC, "MDC_MUSC_UPEXT_OPPON_POLLIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_ADDUC_POLLIC, "MDC_MUSC_UPEXT_ADDUC_POLLIC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_ABDUC_DIGIT_MIN, "MDC_MUSC_UPEXT_ABDUC_DIGIT_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_FLEX_DIGIT_BREV_MIN, "MDC_MUSC_UPEXT_FLEX_DIGIT_BREV_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_OPPON_DIGIT_MIN, "MDC_MUSC_UPEXT_OPPON_DIGIT_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_LUMBRICAL, "MDC_MUSC_UPEXT_LUMBRICAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_INTEROSS_DORSAL, "MDC_MUSC_UPEXT_INTEROSS_DORSAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_UPEXT_INTEROSS_PALMAR, "MDC_MUSC_UPEXT_INTEROSS_PALMAR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_HIP_THIGH, "MDC_MUSC_LOEXT_HIP_THIGH");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_LEG, "MDC_MUSC_LOEXT_LEG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_FOOT, "MDC_MUSC_LOEXT_FOOT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ILLIOPS, "MDC_MUSC_LOEXT_ILLIOPS");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GLUT_MAX, "MDC_MUSC_LOEXT_GLUT_MAX");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GLUT_MED, "MDC_MUSC_LOEXT_GLUT_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GLUT_MIN, "MDC_MUSC_LOEXT_GLUT_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_TENSOR_FASC_LAT, "MDC_MUSC_LOEXT_TENSOR_FASC_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PIRIFORM, "MDC_MUSC_LOEXT_PIRIFORM");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_OBTURATOR, "MDC_MUSC_LOEXT_OBTURATOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GEMEL, "MDC_MUSC_LOEXT_GEMEL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_QUADRAT_FEMOR, "MDC_MUSC_LOEXT_QUADRAT_FEMOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_SARTOR, "MDC_MUSC_LOEXT_SARTOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_QUADRICEPS_FEMOR, "MDC_MUSC_LOEXT_QUADRICEPS_FEMOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_RECT_FEMOR, "MDC_MUSC_LOEXT_RECT_FEMOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_VAST_LAT, "MDC_MUSC_LOEXT_VAST_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_VAST_INTERMED, "MDC_MUSC_LOEXT_VAST_INTERMED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_VAST_MED, "MDC_MUSC_LOEXT_VAST_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PECTIN, "MDC_MUSC_LOEXT_PECTIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ABDUC_LONG, "MDC_MUSC_LOEXT_ABDUC_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ABDUC_BREV, "MDC_MUSC_LOEXT_ABDUC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ABDUC_MAGN, "MDC_MUSC_LOEXT_ABDUC_MAGN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GRACIL, "MDC_MUSC_LOEXT_GRACIL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_BICEPS_FEMOR, "MDC_MUSC_LOEXT_BICEPS_FEMOR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_BICEPS_FEMOR_LONG, "MDC_MUSC_LOEXT_BICEPS_FEMOR_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_BICEPS_FEMOR_BREV, "MDC_MUSC_LOEXT_BICEPS_FEMOR_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_SEMITENDIN, "MDC_MUSC_LOEXT_SEMITENDIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_SEMIMEMBRAN, "MDC_MUSC_LOEXT_SEMIMEMBRAN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_TIBIAL_ANT, "MDC_MUSC_LOEXT_TIBIAL_ANT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_EXTENS_DIGIT_LONG, "MDC_MUSC_LOEXT_EXTENS_DIGIT_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_EXTENS_HALLUC_LONG, "MDC_MUSC_LOEXT_EXTENS_HALLUC_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PERON, "MDC_MUSC_LOEXT_PERON");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PERON_LONG, "MDC_MUSC_LOEXT_PERON_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PERON_BREV, "MDC_MUSC_LOEXT_PERON_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_TRICEPS_SUR, "MDC_MUSC_LOEXT_TRICEPS_SUR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GASTROCNEM, "MDC_MUSC_LOEXT_GASTROCNEM");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GASTROCNEM_LAT, "MDC_MUSC_LOEXT_GASTROCNEM_LAT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_GASTROCNEM_MED, "MDC_MUSC_LOEXT_GASTROCNEM_MED");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_SOL, "MDC_MUSC_LOEXT_SOL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_PLANTAR, "MDC_MUSC_LOEXT_PLANTAR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_POPLIT, "MDC_MUSC_LOEXT_POPLIT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_TIBIAL_POST, "MDC_MUSC_LOEXT_TIBIAL_POST");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_FLEX_DIGIT_LONG, "MDC_MUSC_LOEXT_FLEX_DIGIT_LONG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_EXTENS_HALLUC_BREV, "MDC_MUSC_LOEXT_EXTENS_HALLUC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_EXTENS_DIGIT_BREV, "MDC_MUSC_LOEXT_EXTENS_DIGIT_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ABDUC_HALLUC, "MDC_MUSC_LOEXT_ABDUC_HALLUC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_FLEX_HALLUC_BREV, "MDC_MUSC_LOEXT_FLEX_HALLUC_BREV");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ADDUC_HALLUC, "MDC_MUSC_LOEXT_ADDUC_HALLUC");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_ABDUC_DIGIT_MIN, "MDC_MUSC_LOEXT_ABDUC_DIGIT_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_FLEX_DIGIT_BREV_MIN, "MDC_MUSC_LOEXT_FLEX_DIGIT_BREV_MIN");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_QUADRAT_PLANT, "MDC_MUSC_LOEXT_QUADRAT_PLANT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_LUMBRICAL, "MDC_MUSC_LOEXT_LUMBRICAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_INTEROSS_DORSAL, "MDC_MUSC_LOEXT_INTEROSS_DORSAL");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_MUSC_LOEXT_INTEROSS_PLANTAR, "MDC_MUSC_LOEXT_INTEROSS_PLANTAR");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_TRUNK_BREAST, "MDC_TRUNK_BREAST");    // BTLE HR monitor Chest
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_WRIST, "MDC_UPEXT_WRIST");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FINGER, "MDC_UPEXT_FINGER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_HAND, "MDC_UPEXT_HAND");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_HEAD_EAR, "MDC_HEAD_EAR");        // ear lobe
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_FOOT, "MDC_LOEXT_FOOT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_FOOT, "MDC_LOEXT_FOOT");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG, "MDC_LOEXT_LEG");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG_L, "MDC_LOEXT_LEG_L");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_LEG_R, "MDC_LOEXT_LEG_R");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH, "MDC_LOEXT_THIGH");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH_L, "MDC_LOEXT_THIGH_L");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_LOEXT_THIGH_R, "MDC_LOEXT_THIGH_R");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM, "MDC_UPEXT_FOREARM");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM_L, "MDC_UPEXT_FOREARM_L");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_FOREARM_R, "MDC_UPEXT_FOREARM_R");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER, "MDC_UPEXT_ARM_UPPER");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER_L, "MDC_UPEXT_ARM_UPPER_L");
        addNewReferenceIdEntry(MDC_PART_SITES, SITES.MDC_UPEXT_ARM_UPPER_R, "MDC_UPEXT_ARM_UPPER_R");

    }

    public static String getUnitsAsUCUMString(String codeStr) {
        int code = Integer.valueOf(codeStr);
        code = (int) (code & 0xFFFFL); // Remove the partition code
        return getUnitsAsUCUMString(code);
    }

    public static String getUnitsAsUCUMString(long code32) {
        int code = (int) (code32 & 0xFFFFL); // Remove the partition code
        return getUnitsAsUCUMString(code);
    }

    /**
     * There is a fairly comprhensive table of UCUM codes here https://hl7.org/fhir/us/core/2017Jan/ValueSet-us-core-ucum.html
     *
     * @param code MDC term code
     * @return a UCUM code, empty if not in the map.
     */
    public static String getUnitsAsUCUMString(int code) {
        switch (code) {
            case 512:
                return "{unitless} 1"; // (dimensionless)
            case 544:
                return "%";
            case DIM.MDC_DIM_PH:
                return "[pH]";
            case 1280:
                return "m";
            case 1297:
                return "cm"; // cm
            case 1344:
                return "[ft_i]";    // feet international
            case 1728:
                return "g";
            case DIM.MDC_DIM_MILLI_G:
                return "mg";
            case 1952:
                return "kg/m2"; // kg/m^2
            case 2368:
                return "a";     // years
            case DIM.MDC_DIM_INR:
                return "{INR} 1";    // iU international units
            case DIM.MDC_DIM_STEP:
                return "{unitless} 1";  // steps
            case 1376:
                return "[in_i]";
            case DIM.MDC_DIM_CAL:
                return "cal"; // cal
            case DIM.MDC_DIM_NUTRI_CAL:
                return "[Cal]"; // cal
            case 4724:
                return "mg/dL/min"; // mg/dL per minute
            case 1731:
                return "kg"; // kg
            case 2208:
                return "min";
            case 2240:
                return "h";     // hour
            case 2272:
                return "d";     // day
            case 3057:
                return "cL/s";
            case 6048:
                return "Cel"; // Degree Centigrade
            case 4384:
                return "K"; // Kelvin
            case 4416:
                return "[degF]"; // Degree Fahrenheit
            case 2720:
                return "{beat}/min"; // not bpm?
            case 3872:
                return "mm[Hg]"; // mmHg
            case 3843:
                return "kPa"; // kPa
            case 1600:
                return "L";   // Liters
            case 1617:
                return "cL";  // centi Liters
            case DIM.MDC_DIM_JOULES: // 3968
                return "J"; // energy expended
            case 2848:
                return "L/min"; // liters per minute
            case 2130:
                return "mg/dL"; // mg/dl
            case DIM.MDC_DIM_LB:    //1760:
                return "[lb_av]";
            case DIM.MDC_DIM_MILLI_L: //1618:
                return "mL";
            case DIM.MDC_DIM_MICRO_SEC:   //2195:
                return "us"; // microseconds
            case DIM.MDC_DIM_SEC:
                return "s"; // MDC_DIM_SEC
            case 2194:
                return "ms";
            case DIM.MDC_DIM_MILLI_MOLE_PER_L:
                return "mmol/L";
            case DIM.MDC_DIM_MICRO_MOLE_PER_L:
                return "umol/L";
            default:
                return "";
        }
    }
}
