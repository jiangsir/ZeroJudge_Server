jQuery(document).ready(function() {
	jQuery("#compiler input[name='compiler_enable']").each(function() {
		if ($(this).attr("compiler_enable") == $(this).val()) {
			$(this).attr("checked", "true");
		}
	});

	jQuery("button[id=newCompiler]").click(function() {
		var compiler = $(this).closest(".panel");
		compiler.after(compiler.clone(true));
		jQuery(".panel").each(function(index){
			$(this).find("[data-toggle='collapse']").attr("href", "#collapse"+(index+1));			
			$(this).find(".panel-collapse").attr("id", "collapse"+(index+1));
		});
	});
	jQuery("button[id=deleteCompiler]").click(function() {
		if (jQuery("button[id=deleteCompiler]").size() > 1) {
			var panel = $(this).closest(".panel");
			panel.remove();
			jQuery(".panel").each(function(index){
				$(this).find("[data-toggle='collapse']").attr("href", "#collapse"+(index+1));
				$(this).find(".panel-collapse").attr("id", "collapse"+(index+1));
			});
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
