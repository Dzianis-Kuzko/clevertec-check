package ru.clevertec.check.suite;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("ru.clevertec.check.formatter")
public class FormatterTestSuite {
}
