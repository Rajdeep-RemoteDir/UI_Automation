package com.utils;

import com.base.DriverManager;
import com.config.EnvironmentConfig;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils extends TestBase {

    private static final Logger LOGGER = LoggerUtils.getLogger(ExcelUtils.class);
    public static final String DEFAULT_EXCEL_PATH = EnvironmentConfig.readData("qa","testData.path")+"MyResume_Details.xlsx";

    public ExcelUtils() {
        super(DriverManager.getDriver());
    }

    public static List<Map<String, String>> getTriggeredProfileDetails() {
        return getTriggeredProfileDetails(DEFAULT_EXCEL_PATH);
    }

    public static List<Map<String, String>> getTriggeredProfileDetails(String excelPath) {
        File file = resolveFile(excelPath);
        LOGGER.info("Reading triggered profile details from excel: {}", file.getAbsolutePath());

        try (FileInputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet personalSheet = workbook.getSheet("MyDetails_Personal");
            if (personalSheet == null) {
                throw new IllegalArgumentException("Sheet 'MyDetails_Personal' not found in workbook: " + file.getAbsolutePath());
            }

            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Map<String, Integer> profileColumnMap = getTriggeredProfileColumnMap(personalSheet, formulaEvaluator);

            List<Map<String, String>> triggeredProfiles = new ArrayList<>();

            for (String profileId : profileColumnMap.keySet()) {
                Map<String, String> profileDetails = new LinkedHashMap<>();
                profileDetails.put("Profile_ID", profileId);
                profileDetails.put("Trigger_flag", "Yes");

                for (Sheet sheet : workbook) {
                    if (sheet == null) {
                        continue;
                    }
                    addSheetDetails(profileDetails, sheet, profileId, formulaEvaluator);
                }


                triggeredProfiles.add(profileDetails);
            }

            LOGGER.info("Total triggered profiles found: {}", triggeredProfiles.size());
            return triggeredProfiles;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read Excel file: " + file.getAbsolutePath(), e);
        }
    }

    public static Object[][] getTriggeredProfileDataProvider() {
        return getTriggeredProfileDataProvider(DEFAULT_EXCEL_PATH);
    }

    public static Object[][] getTriggeredProfileDataProvider(String excelPath) {
        List<Map<String, String>> profiles = getTriggeredProfileDetails(excelPath);
        Object[][] data = new Object[profiles.size()][1];
        for (int i = 0; i < profiles.size(); i++) {
            data[i][0] = profiles.get(i);
        }
        return data;
    }

    private static File resolveFile(String excelPath) {
        if (excelPath == null || excelPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Excel path cannot be null or empty");
        }

        File file = new File(excelPath);
        if (!file.exists()) {
            file = new File(DEFAULT_EXCEL_PATH);
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("Excel file not found at: " + excelPath + " or default path: " + DEFAULT_EXCEL_PATH);
        }
        return file;
    }

    private static Map<String, Integer> getTriggeredProfileColumnMap(Sheet sheet, FormulaEvaluator formulaEvaluator) {
        if (sheet == null) {
            return Collections.emptyMap();
        }

        int triggerRowIndex = -1;
        int profileRowIndex = -1;

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
                String cellValue = getCellValue(row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);
                if ("Trigger_flag".equalsIgnoreCase(cellValue)) {
                    triggerRowIndex = rowIndex;
                    profileRowIndex = rowIndex - 1;
                    break;
                }
            }

            if (triggerRowIndex >= 0) {
                break;
            }
        }

        if (triggerRowIndex < 0 || profileRowIndex < 0) {
            return Collections.emptyMap();
        }

        Row profileRow = sheet.getRow(profileRowIndex);
        Row triggerRow = sheet.getRow(triggerRowIndex);
        if (profileRow == null || triggerRow == null) {
            return Collections.emptyMap();
        }

        Map<String, Integer> profileColumns = new LinkedHashMap<>();
        int lastColumn = Math.max(profileRow.getLastCellNum(), triggerRow.getLastCellNum());

        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            String profileId = getCellValue(profileRow.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);
            String triggerFlag = getCellValue(triggerRow.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);

            if (!profileId.isEmpty() && isYes(triggerFlag)) {
                profileColumns.put(profileId, columnIndex);
                LOGGER.debug("Profile '{}' marked as Yes at column index {}", profileId, columnIndex);
            }
        }

        return profileColumns;
    }

    private static void addSheetDetails(Map<String, String> profileDetails, Sheet sheet, String profileId,
                                        FormulaEvaluator formulaEvaluator) {
        if (sheet == null) {
            return;
        }

        int profileHeaderRowIndex = -1;
        int labelColumnIndex = -1;
        int profileColumnIndex = -1;

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
                String header = getCellValue(row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);
                if (!"Profile_ID".equalsIgnoreCase(header)) {
                    continue;
                }

                profileHeaderRowIndex = rowIndex;
                labelColumnIndex = columnIndex;
                for (int profileIndex = columnIndex + 1; profileIndex < row.getLastCellNum(); profileIndex++) {
                    String sheetProfileId = getCellValue(
                            row.getCell(profileIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);
                    if (profileId.equalsIgnoreCase(sheetProfileId)) {
                        profileColumnIndex = profileIndex;
                        break;
                    }
                }
                break;
            }

            if (profileHeaderRowIndex >= 0) {
                break;
            }
        }

        if (profileHeaderRowIndex < 0 || profileColumnIndex < 0) {
            LOGGER.debug("Profile '{}' not found on sheet '{}'; skipping sheet", profileId, sheet.getSheetName());
            return;
        }

        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            if (rowIndex == profileHeaderRowIndex) {
                continue;
            }

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }

            String label = getCellValue(row.getCell(labelColumnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);
            String value = getCellValue(row.getCell(profileColumnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL), formulaEvaluator);

            if (!label.isEmpty()
                    && !"Trigger_flag".equalsIgnoreCase(label)
                    && !"Profile_ID".equalsIgnoreCase(label)
                    && !value.isEmpty()) {
                profileDetails.putIfAbsent(label, value);
            }
        }
    }

    private static boolean isYes(String value) {
        return value != null && value.equalsIgnoreCase("Yes");
    }

    private static String getCellValue(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell, formulaEvaluator).trim();
    }
}
