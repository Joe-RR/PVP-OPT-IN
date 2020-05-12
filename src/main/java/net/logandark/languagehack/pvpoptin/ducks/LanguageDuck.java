package net.logandark.languagehack.pvpoptin.ducks;

import java.util.Map;
import java.util.regex.Pattern;

public interface LanguageDuck {
	Map<String, String> getTranslations();

	Pattern getTokenPattern();
}
