function Html() {
}

Html.prototype = {

	constructor : Html,

	reloadBody : function(url) {
		$.ajax({
			url : url,
			context : document.body,
			success : function(s) {
				$(this).html(s);
			}
		});
	}
};