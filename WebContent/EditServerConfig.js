jQuery(document).ready(function() {
	jQuery("#compiler input[name='compiler_enable']").each(function() {
		if ($(this).attr("language") == $(this).val()) {
			$(this).attr("checked", "true");
		}
	});

	jQuery("button[id=newCompiler]").click(function() {
		var compiler = $(this).closest("#compiler");
		compiler.after(compiler.clone(true));
	});
	jQuery("button[id=deleteCompiler]").click(function() {
		if (jQuery("button[id=deleteCompiler]").size() > 1) {
			var compiler = $(this).closest("#compiler");
			compiler.remove();
		}
	});

	jQuery("input[name='isCleanTmpFile']").each(function() {
		if ($(this).attr("isCleanTmpFile") == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

	jQuery("select[name=compiler_language]").children().each(function() {
		if ($(this).parent().attr("compiler_language") == $(this).val()) {
			$(this).attr("selected", true);
		}
	});

});
