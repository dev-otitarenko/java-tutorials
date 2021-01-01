package com.maestro.lib.excel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class ExcelLoaderTest {
	@ParameterizedTest
	@MethodSource("getArgumentsStream")
	public void testOutputXlsFile(String sourceFileName, String targetFileName) throws IOException {
		InputStream excelFile = (new TestUtils()).getResourceFile(sourceFileName);
		byte[] content = TestUtils.getFileContent((new TestUtils()).getResourceFile(targetFileName));
		Gson gson = new Gson();
		Type typeListOfMap = new TypeToken<TestUtils.TJsonFile>() {}.getType();
		TestUtils.TJsonFile targetData = gson.fromJson(new String(content), typeListOfMap);

		try {
			ExcelLoader xlsUtil = new ExcelLoader();
			xlsUtil.parse(excelFile);
			List<Map<String, String>> docs = xlsUtil.getData();
			List<String> params = xlsUtil.getParams();

			System.out.printf("<< Fields: %s >>\n", TestUtils.prettyJson(params));
			System.out.println(" << Records count: " + docs.size());

			docs.forEach(sourceData -> {
				StringBuilder sb = new StringBuilder();

				sb.append("\n\t Data:[");
				sourceData.forEach((nm, val) -> sb.append(String.format("\n\t\t\t \"%s\":\"%s\"", nm, val)));
				sb.append("\n\t ]");

				checkWithEtalonData(sourceData, targetData.getData());

				System.out.printf("<< Document data %s%n", sb.toString());
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkHeaders(final List<String> sourceParams, final List<String> targetParams) {
		List<String> resp =
		sourceParams
				.stream()
				.filter(h -> !h.startsWith("GENERATE") && targetParams.contains(h)).collect(Collectors.toList());
		if (resp.size() == 0 || (resp.size() != targetParams.size())) {
			fail("resp.size() != targetParams.size()");
		}
	}

	private void checkWithEtalonData(final Map<String, String>  sourceRow, final List<Map<String, String>> docsToCheck) {
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

	private static Stream<Arguments> getArgumentsStream() {
		List<TestUtils.TCheckFileWithJson> files = new ArrayList<>();
		files.add(new TestUtils.TCheckFileWithJson("documents_1.xlsx", "documents_1.json"));
		files.add(new TestUtils.TCheckFileWithJson("documents_2.xlsx", "documents_2.json"));

		List<Arguments> listOfLists = new LinkedList<>();
		files.forEach((file) ->
				listOfLists.add(Arguments.of(file.getSourceFilePath(), file.getTargetFilePath())));
		return listOfLists.stream();
	}
}
