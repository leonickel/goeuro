package com.leonickel;

import static com.leonickel.util.DefaultProperties.CSV_FILE_NAME;
import static com.leonickel.util.DefaultProperties.CSV_OUTPUT_PATH;
import static com.leonickel.util.DefaultProperties.CSV_SEPARATOR_VALUE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leonickel.util.PropertyFinder;

public class BootstrapTest {

	@Before
	public void setUp() throws Exception {
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		file.deleteOnExit();
	}
	
	@After
	public void tearDown() throws Exception {
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		file.deleteOnExit();
	}
	
	@Test
	public void test_startup_valid_arguments() {
		Bootstrap.main(new String[]{"berlin"});
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void test_startup_invalid_arguments() {
		Bootstrap.main(new String[]{});
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		Assert.assertFalse(file.exists());
	}
	
	@Test
	public void test_startup_valid_arguments_specific_output_path() {
		System.setProperty(CSV_OUTPUT_PATH.property(), "/tmp/");
		Bootstrap.main(new String[]{"berlin"});
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void test_startup_valid_arguments_specific_file_name() {
		System.setProperty(CSV_FILE_NAME.property(), "test.csv");
		Bootstrap.main(new String[]{"berlin"});
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		Assert.assertTrue(file.exists());
	}
	
	@Test
	public void test_startup_valid_arguments_specific_separator_value() throws IOException {
		System.setProperty(CSV_SEPARATOR_VALUE.property(), "#");
		Bootstrap.main(new String[]{"berlin"});
		final File file = new File(PropertyFinder.getPropertyValue(CSV_OUTPUT_PATH) + PropertyFinder.getPropertyValue(CSV_FILE_NAME));
		final BufferedReader br = new BufferedReader(new FileReader(file));
		final String line = br.readLine();
		br.close();
		
		Assert.assertTrue(line.contains(PropertyFinder.getPropertyValue(CSV_SEPARATOR_VALUE)));
	}
}
