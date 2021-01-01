package com.maestro.lib.excel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ExcelWriterTest {
    @ParameterizedTest
    @CsvSource(value = { "documents_1.json", "documents_2.json" })
    public void outputXlsFile(String fileNm) throws IOException {
        byte[] content = TestUtils.getFileContent((new TestUtils()).getResourceFile(fileNm));
        Gson gson = new Gson();
        Type typeListOfMap = new TypeToken<TestUtils.TJsonFileObject>() {}.getType();
        TestUtils.TJsonFileObject targetData = gson.fromJson(new String(content), typeListOfMap);

        byte[] bytes = ExcelWriter.writeFile(targetData.getHeaders(), targetData.getData());
        InputStream excelFile = new ByteArrayInputStream(bytes);

        try {
            ExcelLoader xlsUtil = new ExcelLoader();
            xlsUtil.parse(excelFile);
            List<Map<String, String>> docs = xlsUtil.getData();
            List<String> params = xlsUtil.getParams();

            TestUtils.checkExcelHeadersWithEtalonHeaders(params, targetData.getHeaders());

            docs.forEach(sourceData -> {
                StringBuilder sb = new StringBuilder();

                sb.append("\n\t Data:[");
                sourceData.forEach((nm, val) -> sb.append(String.format("\n\t\t\t \"%s\":\"%s\"", nm, val)));
                sb.append("\n\t ]");

                TestUtils.checkExcelRowWithEtalonRows1(sourceData, targetData.getData());

                System.out.printf("<< Document data %s%n", sb.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
