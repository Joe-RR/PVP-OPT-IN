package net.logandark.languagehack.pvpoptin.mixin;

import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.regex.Pattern;

@Mixin(Language.class)
public interface MixinLanguage {
	@Accessor
	Map<String, String> getTranslations();

	@Accessor("TOKEN_PATTERN")
	Pattern getTokenPattern();
}
