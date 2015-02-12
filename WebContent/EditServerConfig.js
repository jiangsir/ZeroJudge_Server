jQuery(document).ready(function() {
	jQuery("#compiler input[name='compiler_enable']").each(function() {
		if ($(this).attr("language") == $(this).val()) {
			$(this).attr("checked", "true");
		}
	});

	jQuery("#newCompiler").click(function() {
		var compiler = $(this).closest("#compiler");
		compiler.after(compiler.clone());
	});

	jQuery("input[name='isCleanTmpFile']").each(function() {
		if ($(this).attr("isCleanTmpFile") == $(this).val()) {
			$(this).attr("checked", true);
		}
	});

});
