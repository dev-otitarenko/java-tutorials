package com.maestro.lib.excel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.platform.commons.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtils {
    public  InputStream getResourceFile(final String fname) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream excelFile = classLoader.getResourceAsStream(fname);

        return excelFile;
    }

    public static String prettyJson(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(obj);
    }

    public static byte[] getFileContent (final InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static void checkExcelHeadersWithEtalonHeaders(final List<String> sourceParams, final List<String> targetParams) {
        List<String> resp =
                sourceParams
                        .stream()
                        .filter(h -> !h.startsWith("GENERATE") && targetParams.contains(h)).collect(Collectors.toList());
        if (resp.size() == 0 || (resp.size() != targetParams.size())) {
            fail("resp.size() != targetParams.size()");
        }
    }

    public static void checkExcelRowWithEtalonRows(final Map<String, String>  sourceRow, final List<Map<String, String>> docsToCheck) {
        String sourcePk = sourceRow.get("participant_id");
        boolean fnd = false;
        for (Map<String, String> data : docsToCheck) {
            String targetPk = data
                    .entrySet()
                    .stream()
                    .filter(row -> row.getKey().equals("participant_id")).map(Map.Entry::getValue).findAny().orElse(null);
            if (!StringUtils.isBlank(targetPk)) {
                if (sourcePk.equals(targetPk)) {
                    fnd = true;
                    sourceRow.forEach((key, sourceVal) -> {
                        String targetVal = data
                                .entrySet()
                                .stream()
                                .filter(row -> row.getKey().equals(key)).map(Map.Entry::getValue).findAny().orElse(null);
                        if (!StringUtils.isBlank(targetVal)) {
                            if (!sourceVal.equals(targetVal)) {
                                fail("'" + sourceVal + "' != '" + targetVal + "'");
                            }
                        } else {
                            fail("Could not find ['" + key + "', '" + sourceVal+ "']");
                        }
                    });
                    break;
                }
            }
        }
        if (!fnd) {
            fail("Could not find '" + sourcePk + "'");
        }
    }

    public static void checkExcelRowWithEtalonRows1(final Map<String, String>  sourceRow, final List<Map<String, Object>> docsToCheck) {
        String sourcePk = sourceRow.get("participant_id");
        boolean fnd = false;
        for (Map<String, Object> data : docsToCheck) {
            Object targetPk = data
                    .entrySet()
                    .stream()
                    .filter(row -> row.getKey().equals("participant_id")).map(Map.Entry::getValue).findAny().orElse(null);
            if (!StringUtils.isBlank((String)targetPk)) {
                if (sourcePk.equals(targetPk)) {
                    fnd = true;
                    sourceRow.forEach((key, sourceVal) -> {
                        Object targetVal = data
                                .entrySet()
                                .stream()
                                .filter(row -> row.getKey().equals(key)).map(Map.Entry::getValue).findAny().orElse(null);
                        if (!StringUtils.isBlank((String)targetVal)) {
                            if (!sourceVal.equals(targetVal)) {
                                fail("'" + sourceVal + "' != '" + targetVal + "'");
                            }
                        } else {
                            fail("Could not find ['" + key + "', '" + sourceVal+ "']");
                        }
                    });
                    break;
                }
            }
        }
        if (!fnd) {
            fail("Could not find '" + sourcePk + "'");
        }
    }

    @Data
    @AllArgsConstructor
    public static class TCheckFileWithJson {
        private String sourceFilePath;
        private String targetFilePath;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TJsonFile {
        private List<String> headers;
        private List<Map<String, String>> data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TJsonFileObject {
        private List<String> headers;
        private List<Map<String, Object>> data;
    }
}
